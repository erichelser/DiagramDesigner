package DiagramDesigner;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.*;

class Anchor extends DraggableBox
{
	private final static int DIM = 6;//width of anchor box
	private Link link;
	private Bucket parent;    // separate just in case there are multiple anchors

	private Anchor()
	{
		super();
		link = null;
		setSize(new OrderedPair(DIM, DIM));
	}
	public Anchor(Bucket _parent)
	{
		this();
		setParent(_parent);
	}

	public void setParent(Bucket _parent)
	{
		parent=_parent;
	}

	public void setLink(Link x)
	{
		link = x;
	}

	public void drawMe(Graphics g)
	{
		
		g.setColor(new Color(0, 0, 0));
		OrderedPair pixel = getBoard().posToPixel(this.getPosition());
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(1));

		g.drawRect((int)(pixel.getX() + getPixelDelta().getX()),
			   (int)(pixel.getY() + getPixelDelta().getY()),
			   (int)(Math.round(getSize().getX() * getBoard().getZoomFactor())),
			   (int)(Math.round(getSize().getY() * getBoard().getZoomFactor())));
	}

	public ArrayList<? extends Tangible> getTangibleChildren()
	{
		ArrayList<Link> ret=new ArrayList<Link>(0);
		ret.add(link);
		return ret;
	}

	public void setBoard(Board b)
	{
		super.setBoard(b);
		if(link!=null)
			link.setBoard(b);
	}


	public OrderedPair getPosition()
	{
		OrderedPair ret;
		double fractionalPosition = parent.calculateFractionalPosition(this);
		ret = new OrderedPair(
			parent.getPosition().getX() - DIM / 2
			+ (parent.isLeftAnchor(this) ? 0 : parent.getSize().getX())
			,
			parent.getPosition().getY() - DIM / 2 + parent.getSize().getY() * fractionalPosition);
		return ret;
	}

	public OrderedPair getCenterPosition()
	{
		return this.getPosition().add(new OrderedPair(DIM/2,DIM/2));
	}

	//Pixel delta is derived from parent object
	public OrderedPair getPixelDelta() { return parent.getPixelDelta(); }
	public void setPixelDelta(OrderedPair d) { }
	public void applyPixelDelta() { }


	public void zoomNotify() { }
	public void onClickAction(MouseEvent e)
	{
		System.out.println(e);
		if(link==null)
		{
			Anchor endpoint=new Anchor();
			link=new Link(this,endpoint);
			setBoardClickedTangible(e,endpoint);
		}
	}
}

