import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Bucket extends DraggableBox
{
	private ArrayList<DraggableBox> items;
	private ArrayList<Anchor> leftAnchors;
	private ArrayList<Anchor> rightAnchors;

	public static Bucket createFromImageBox(String imgFile, int posX, int posY, int leftAnchors, int rightAnchors)
	{
		Bucket item=new Bucket(posX, posY);
		item.addItem(new ImageBox(imgFile));
		for(int i=0; i<leftAnchors; i++)
			item.addLeftAnchor();
		for(int i=0; i<rightAnchors; i++)
			item.addRightAnchor();
		return item;
	}

	private Bucket(int x, int y)
	{
		super();
		setSize(new OrderedPair(0, 0));
		setPosition(new OrderedPair(x, y));
		items = new ArrayList<DraggableBox>(0);
		leftAnchors = new ArrayList<Anchor>(0);
		rightAnchors=new ArrayList<Anchor>(0);
	}
	public void addItem(DraggableBox b)
	{
		items.add(b);
		b.linkToParentContainer(this);
	}
	private void rearrangeItems()
	{
		OrderedPair newItemPosition = new OrderedPair(getPosition());
		getSize().set(0, 0);
		for (int i = 0; i < items.size(); i++)
		{
			items.get(i).setPosition(newItemPosition);
			newItemPosition.add(new OrderedPair(0, items.get(i).getSize().getY()));
			if (getSize().getX() < items.get(i).getSize().getX())
				getSize().setX(items.get(i).getSize().getX());
			getSize().setY(getSize().getY() + items.get(i).getSize().getY());
		}
	}
	public boolean isClicked(MouseEvent e)
	{
		for (int i = 0; i < items.size(); i++)
			if (items.get(i).isClicked(e))
				return true;
		return false;
	}
	public void applyPixelDelta()
	{
		super.applyPixelDelta();
		for (int i = 0; i < items.size(); i++)
			items.get(i).applyPixelDelta();
		for (int i = 0; i < leftAnchors.size(); i++)
			leftAnchors.get(i).applyPixelDelta();
		for (int i = 0; i < rightAnchors.size(); i++)
			rightAnchors.get(i).applyPixelDelta();
	}
	public void linkTo(Bucket rhs, int lhsIndex, int rhsIndex)
	{
		Anchor anchor1=this.getRightAnchor(lhsIndex);
		Anchor anchor2=rhs.getLeftAnchor(rhsIndex);
		Link L=new Link(anchor1,anchor2);
		anchor1.setLink(L);
		anchor2.setLink(L);
	}

	public void addLeftAnchor()
	{
		Anchor a=new Anchor(this);
		a.setBoard(getBoard());
		leftAnchors.add(a);
	}
	public void addRightAnchor()
	{
		Anchor a=new Anchor(this);
		a.setBoard(getBoard());
		rightAnchors.add(a);
	}
	public void drawMe(Graphics g)
	{
		rearrangeItems();
		for (int i = 0; i < items.size(); i++)
			items.get(i).drawMe(g);
		for(int i=0; i<leftAnchors.size(); i++)
			leftAnchors.get(i).drawMe(g);
		for(int i=0; i<rightAnchors.size(); i++)
			rightAnchors.get(i).drawMe(g);
	}
	public void setBoard(Board b)
	{
		super.setBoard(b);
		for (int i = 0; i < items.size(); i++)
			items.get(i).setBoard(b);
		for (int i = 0; i < leftAnchors.size(); i++)
			leftAnchors.get(i).setBoard(b);
		for (int i = 0; i < rightAnchors.size(); i++)
			rightAnchors.get(i).setBoard(b);
	}

	public boolean isLeftAnchor(Anchor a) { return leftAnchors.contains(a); }
	public boolean isRightAnchor(Anchor a) { return rightAnchors.contains(a); }

	public Anchor getLeftAnchor(int i){	return leftAnchors.get(i);}
	public Anchor getRightAnchor(int i){ return rightAnchors.get(i);}

	public double calculateFractionalPosition(Anchor a)
	{
		if(isLeftAnchor(a))
			return 1.0*(leftAnchors.indexOf(a)+1)/(leftAnchors.size()+1);
		else if(isRightAnchor(a))
			return 1.0*(rightAnchors.indexOf(a)+1)/(rightAnchors.size()+1);
		return 0.5;
	}

	public void zoomNotify()
	{
		for (int i = 0; i < items.size(); i++)
			items.get(i).zoomNotify();
		for (int i = 0; i < leftAnchors.size(); i++)
			leftAnchors.get(i).zoomNotify();
		for (int i = 0; i < rightAnchors.size(); i++)
			rightAnchors.get(i).zoomNotify();
	}

	public void onClickAction(MouseEvent e) { }

	public ArrayList<? extends Tangible> getTangibleChildren()
	{
		ArrayList<Anchor> ret=new ArrayList<Anchor>(0);
		for(int i=0; i<leftAnchors.size(); i++)
			ret.add(leftAnchors.get(i));
		for(int i=0; i<rightAnchors.size(); i++)
			ret.add(rightAnchors.get(i));
		return ret;
	}
}