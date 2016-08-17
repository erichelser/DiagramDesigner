import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.*;

class Link extends Tangible
{
	DraggableBox pointA;
	DraggableBox pointB;
	int thickness;
	public Link()
	{
		super();
		thickness = 3;
	}
	public Link(DraggableBox A, DraggableBox B)
	{
		this();
		pointA = A;
		pointB = B;
	}
	public boolean isClicked(MouseEvent e) { return false; }
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
			g.setColor(new Color(0, 0, 0));
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(thickness));
			g2.draw(new Line2D.Double(pointACenterPx.getX(),
				                      pointACenterPx.getY(),
									  pointBCenterPx.getX(),
									  pointBCenterPx.getY()));
		}
	}
}
