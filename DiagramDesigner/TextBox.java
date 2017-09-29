package DiagramDesigner;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;

public class TextBox extends DraggableBox
{
	private TextImage textImage;
	private TextBox()
	{
		super();
	}
	public TextBox(String text)
	{
		this();
		textImage = new TextImage(text);
	}
	public TextBox(int x, int y, String text)
	{
		this(text);
		setPosition(new OrderedPair(x, y));
	}
	public void onClickAction(MouseEvent e)
	{
		//do nothing
	}
	public void drawMe(Graphics g)
	{
		setSize(textImage.getSize(g));

		BufferedImage messageImg = new BufferedImage((int)(getSize().getX() * getBoard().getZoomFactor()),
													 (int)(getSize().getY() * getBoard().getZoomFactor()),
			                                         BufferedImage.TYPE_INT_ARGB);

		Graphics2D messageImgG2D = messageImg.createGraphics();
		AffineTransform at = new AffineTransform();
		at.scale(getBoard().getZoomFactor(), getBoard().getZoomFactor());
		messageImgG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		messageImgG2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		messageImgG2D.drawImage(textImage.getImage(g), at, null);

		OrderedPair pixel = getBoard().posToPixel(getPosition());
		g.drawImage(messageImg,
					(int)(pixel.getX() + getPixelDelta().getX()),
					(int)(pixel.getY() + getPixelDelta().getY()),
					null);
	}
	public void zoomNotify() { }
}
