package DiagramDesigner;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.*;

class Link extends Tangible
{
	private Anchor pointA;
	private Anchor pointB;
	private float thickness;
	private Color color;
	
	public Link()
	{
		super();
		thickness = 3;
		color=new Color(0,0,200);

		
	}
	public Link(Anchor A, Anchor B)
	{
		this();
		pointA = A;
		pointB = B;
		pointA.setLink(this);
		pointB.setLink(this);
	}
	public boolean isClicked(MouseEvent e)
	{
		//accounts for floating-point precision errors
		final double DBL_TOLERANCE=0.0001;

		//how close does the mouse need to be to trigger a 'true' result?
		//note: measured in pixels
		double ABS_TOLERANCE=1.8 / getBoard().getZoomFactor(); 

		//this is how far the click can be located from the center-line of the link
		double TOLERANCE = (ABS_TOLERANCE + thickness/2);

		OrderedPair mPos = getBoard().pixelToPos(new OrderedPair(e.getX(), e.getY()));
		OrderedPair ptA=pointA.getCenterPosition().replicate();
		OrderedPair ptB=pointB.getCenterPosition().replicate();

		mPos.subtract(ptA);
		ptB.subtract(ptA);
		ptA.subtract(ptA);

		if(ptB.getX() < 0)
		{
			ptB.setX(ptB.getX()*-1);
			mPos.setX(mPos.getX()*-1);
		}
		if(ptB.getY() < 0)
		{
			ptB.setY(ptB.getY()*-1);
			mPos.setY(mPos.getY()*-1);
		}

		//ptA should now be at the origin
		//ptB should now be in Quadrant 1

		//point is too low or too far left
		if(mPos.getX() < -TOLERANCE || mPos.getY() < -TOLERANCE)
			return false;

		//point is too high or too far right
		if(ptB.getX()+TOLERANCE < mPos.getX() || ptB.getY() + TOLERANCE < mPos.getY())
			return false;

		//check if line segment is horizontal
		if(ptB.getY() <= DBL_TOLERANCE)
		{
			return (-TOLERANCE <= mPos.getX() && mPos.getX() <= ptB.getX() + TOLERANCE
			     && -TOLERANCE <= mPos.getY() && mPos.getY() <= TOLERANCE);
		}

		//check if line segment is vertical
		if(ptB.getX() <= DBL_TOLERANCE)
		{
			return (-TOLERANCE <= mPos.getX() && mPos.getX() <= TOLERANCE
			     && -TOLERANCE <= mPos.getY() && mPos.getY() <= ptB.getY() + TOLERANCE);
		}

		//do geometry if line is not along an axis
		double interceptX=ptB.getX() * (mPos.getX() * ptB.getX() + mPos.getY() * ptB.getY() )
		                        / (ptB.getX() * ptB.getX() + ptB.getY() * ptB.getY());
		double interceptY=interceptX * ptB.getY() / ptB.getX();

		double deltaX=mPos.getX() - interceptX;
		double deltaY=mPos.getY() - interceptY;
		double dist  =Math.sqrt(deltaX*deltaX+deltaY*deltaY);

		return dist<TOLERANCE;
	}
	public void onClickAction(MouseEvent e) { }
	public void setPixelDelta(OrderedPair d) { }
	public void applyPixelDelta() { }
	public void zoomNotify() { }

	public void drawMe(Graphics g)
	{
		if (pointA != null && pointB != null)
		{
			OrderedPair pointACenterPx = getBoard().posToPixel(pointA.getCenterPosition()).add(pointA.getPixelDelta());
			OrderedPair pointBCenterPx = getBoard().posToPixel(pointB.getCenterPosition()).add(pointB.getPixelDelta());
			g.setColor(color);
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke((float)(thickness*getBoard().getZoomFactor())));

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			Path2D.Double path=new Path2D.Double();
			path.moveTo(pointACenterPx.getX(),pointACenterPx.getY());
			path.lineTo(pointBCenterPx.getX(),pointBCenterPx.getY());
			g2.draw(path);
		}
	}
}