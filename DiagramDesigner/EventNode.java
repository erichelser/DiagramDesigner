package DiagramDesigner;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.event.*;

public class EventNode extends Bucket
{
	private int borderSize;
	public EventNode(int x, int y, int leftAnchors, int rightAnchors)
	{
		super(x,y);
		borderSize=20;
		this.setFillColor(new Color(100,200,0));

		this.setSize(new OrderedPair(borderSize,MAX(leftAnchors,rightAnchors)*borderSize));
		this.createAnchors(leftAnchors, rightAnchors);
	}
	
	public void setImage(String file)
	{
		clearItems();
		addImage(file);
		this.setSize(new OrderedPair(borderSize+items.get(0).getSize().getX(),
		                             MAX(borderSize+items.get(0).getSize().getY(),MAX(leftAnchors.size(),rightAnchors.size())*borderSize)));
	}
	
	private int MAX(int a, int b)
	{
		return (a>b)?a:b;
	}
	private double MAX(double a, double b)
	{
		return (a>b)?a:b;
	}

	//Anchors in EventNodes are top-aligned
	public double calculateFractionalPosition(Anchor a)
	{
		if(isLeftAnchor(a))
			return 1.0*(leftAnchors.indexOf(a)+1)/(MAX(leftAnchors.size(),rightAnchors.size())+1);
		else if(isRightAnchor(a))
			return 1.0*(rightAnchors.indexOf(a)+1)/(MAX(leftAnchors.size(),rightAnchors.size())+1);
		return 0.5;
	}

	public boolean isClicked(MouseEvent e)
	{
		OrderedPair boardPos = getBoard().pixelToPos(new OrderedPair(e.getX(), e.getY()));
		boardPos.subtract(getPosition());
		return (0 <= boardPos.getX() && boardPos.getX() < getSize().getX()
			 && 0 <= boardPos.getY() && boardPos.getY() < getSize().getY());
	}

	public void drawMe(Graphics g)
	{
		g.setColor(fillColor);
		Graphics2D g2 = (Graphics2D)g;
		OrderedPair pixel = getBoard().posToPixel(getPosition());
		pixel.add(getPixelDelta());
		g2.fill(new RoundRectangle2D.Double(pixel.getX(),
			                            pixel.getY(),
						    getSize().getX() * getBoard().getZoomFactor(),
						    getSize().getY() * getBoard().getZoomFactor(),
						    10,10));
							
		if(items.size()>0)
		{
			items.get(0).setPosition(getPosition().replicate().add(new OrderedPair(borderSize/2,borderSize/2)));
			items.get(0).drawMe(g2);
		}
							
		for(int i=0; i<leftAnchors.size(); i++)
			leftAnchors.get(i).drawMe(g);
		for(int i=0; i<rightAnchors.size(); i++)
			rightAnchors.get(i).drawMe(g);
	}
}