import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;

class ImageBox extends DraggableBox
{
	private BufferedImage img;
	private BufferedImage rotatedImg;

	private OrderedPair axisPoint;			//relative to UL corner of original img, how far in is the axis of rotation?
	private OrderedPair positionOffset; //how far does rotated img have to move to keep axisPoint in the same spot?
	private OrderedPair imgCorner;		//offset from $position of UL corner of actual rotated image
	private OrderedPair pixelDrawLocation; //the pixel coords of the UL corner of the normal rectangle that encloses the rotated shape

	private boolean rotateOnClick;

	private double angleDegrees;
	public ImageBox(String imgFile)
	{
		this(0, 0, imgFile);
	}
	public ImageBox(int x, int y, String imgFile)
	{
		super();

		positionOffset = new OrderedPair(0, 0);
		img = null;
		try { img = ImageIO.read(new File(imgFile)); }
		catch (IOException e) { System.out.println(e); }
		setAxisAtRatio(0.5, 0.5);
		angleDegrees = 0;
		imgCorner = new OrderedPair();
		pixelDrawLocation = new OrderedPair();

		setPosition(new OrderedPair(x, y));
		setSize(new OrderedPair(img.getWidth(), img.getHeight()));

		rotateOnClick = false;
	}
	public void setRotateOnClick(boolean x) { rotateOnClick = x; }
	public void setAxisAtPixel(double x, double y)
	{
		axisPoint = new OrderedPair(x, y);
	}
	public void setAxisAtRatio(double x, double y)
	{
		axisPoint = new OrderedPair(img.getWidth() * x, img.getHeight() * y);
	}
	public OrderedPair getCenterPosition()
	{
		return getPosition().replicate().add(axisPoint);
	}
	public void onClickAction(MouseEvent e)
	{
		if (rotateOnClick)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
				angleDegrees -= 10;
			else if (e.getButton() == MouseEvent.BUTTON3)
				angleDegrees += 10;
			updateRotatedImage();
		}
	}
	public void zoomNotify()
	{
		updateRotatedImage();
	}
	
	private void updateRotatedImage()
	{
		double theta = Math.toRadians(angleDegrees); //angle of rotation of object
		double sinTheta = Math.sin(theta);
		double cosTheta = Math.cos(theta);
		double absSinTheta = Math.abs(sinTheta);
		double absCosTheta = Math.abs(cosTheta);

		//dimensions of the rotated image w.r.t. our current/standard axes
		OrderedPair newSize = new OrderedPair(getSize().getX() * absCosTheta + getSize().getY() * absSinTheta,
											   getSize().getY() * absCosTheta + getSize().getX() * absSinTheta);

		//assuming that the rotated image's North and West boundaries coincide with its original
		//boundaries, determine the location of the rotated image's top left corner w.r.t. its original position
		imgCorner = new OrderedPair();
		while (angleDegrees < 0) angleDegrees += 360;
		while (angleDegrees >= 360) angleDegrees -= 360;
		if (0 <= angleDegrees && angleDegrees <= 90)
			imgCorner.set(getSize().getY() * absSinTheta, 0);
		else if (90 <= angleDegrees && angleDegrees <= 180)
			imgCorner.set(newSize.getX() * 1, getSize().getY() * absCosTheta);
		else if (180 <= angleDegrees && angleDegrees <= 270)
			imgCorner.set(newSize.getX() - getSize().getY() * absSinTheta, newSize.getY());
		else if (270 <= angleDegrees && angleDegrees <= 360)
			imgCorner.set(0, newSize.getY() - getSize().getY() * absCosTheta);
		
		//calculate how much to adjust the pos of the rotatedImg obj based on axisPoint point
		positionOffset.setZero();
		positionOffset.add(axisPoint);
		positionOffset.rotateAboutOrigin(angleDegrees);
		positionOffset.add(imgCorner);
		positionOffset.subtract(axisPoint);

		//draw rotated image to semi-transparent rotatedImg object
		rotatedImg = new BufferedImage((int)(Math.round(newSize.getX() * getBoard().getZoomFactor())),
									   (int)(Math.round(newSize.getY() * getBoard().getZoomFactor())),
									   BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = rotatedImg.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		AffineTransform at = new AffineTransform();
		at.setToRotation(theta, imgCorner.getX() * getBoard().getZoomFactor(), imgCorner.getY() * getBoard().getZoomFactor());
		at.translate(imgCorner.getX() * getBoard().getZoomFactor(), imgCorner.getY() * getBoard().getZoomFactor());
		at.scale(getBoard().getZoomFactor(), getBoard().getZoomFactor());
		g2d.drawImage(img, at, null);
		g2d.dispose();
	}

	//Overridden method to account for rotated boundaries of graphic
	public boolean isClicked(MouseEvent e)
	{
		OrderedPair boardPos = new OrderedPair(e.getX(), e.getY());
		boardPos.subtract(new OrderedPair(pixelDrawLocation.getXInt(),pixelDrawLocation.getYInt()));
		boardPos.multiply(1 / getBoard().getZoomFactor());
		boardPos.subtract(imgCorner);
		boardPos.rotateAboutOrigin(-angleDegrees);
		return (0 <= boardPos.getX() && boardPos.getX() <= getSize().getX()
			 && 0 <= boardPos.getY() && boardPos.getY() <= getSize().getY());
	}

	public void drawMe(Graphics g)
	{
		if (rotatedImg == null)
			updateRotatedImage();

		pixelDrawLocation = new OrderedPair(getPosition());
		pixelDrawLocation.subtract(positionOffset);
		pixelDrawLocation = getBoard().posToPixel(pixelDrawLocation);
		pixelDrawLocation.add(getPixelDelta());

		////"Helper" rectangle to define the outer boundary of the rotated image
		//g.setColor(new Color(0, 0, 0));
		//g.drawRect(pixelDrawLocation.getXInt(), pixelDrawLocation.getYInt(), rotatedImg.getWidth(), rotatedImg.getHeight());

		g.drawImage(rotatedImg, pixelDrawLocation.getXInt(), pixelDrawLocation.getYInt(), null);
	}
}