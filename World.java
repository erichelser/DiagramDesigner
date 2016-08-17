import DiagramDesigner.*;
import java.awt.*;
import java.awt.event.*;import java.util.*;

//This class simply launches the JFrame in a non-static context
public class World
{
	public static void main(String[] args)
	{
		Board b = new Board(800, 600);
		new DDWindow(b);

		LightGrid grid=new LightGrid(20,30);
		grid.setBoard(b);

		Bucket bucket=new Bucket();
		bucket.setPosition(100,100);
		bucket.addItem(grid);
		b.addTangible(bucket);

		Bucket letterABucket = new Bucket();
		letterABucket.addImage("images/A_50x50.png");
		letterABucket.setPosition(210,110);
		b.addTangible(letterABucket);

		Periodic p=new Periodic(2,grid);
		p.start();

/*
		Bucket letterABucket = new Bucket();
		letterABucket.addImage("images/A_50x50.png");
		letterABucket.setPosition(210,110);
		letterABucket.createAnchors(2,3);

		Bucket letterBBucket = new Bucket();
		letterBBucket.addImage("images/B_50x50.png");
		letterBBucket.setPosition(110,110);
		letterBBucket.createAnchors(1,1);

		Bucket letterCBucket = new Bucket();
		letterCBucket.addImage("images/C_50x50.png");
		letterCBucket.setPosition(110,210);
		letterCBucket.createAnchors(2,5);

		Bucket.link(letterABucket,0,letterBBucket,0);
		Bucket.link(letterBBucket,0,letterCBucket,0);
		Bucket.link(letterABucket,1,letterCBucket,1);

		b.addTangible(letterABucket);
		b.addTangible(letterBBucket);
		b.addTangible(letterCBucket);

		MenuButton mb1=new MenuButton("MB1");
		b.addMenuButton(mb1);
*/
	}
}

class LightGrid extends DraggableBox implements Command
{
	private ArrayList<Light> lights;
	private int rows, cols;
	private int lightSize;
	public LightGrid(int r, int c)
	{
		lights=new ArrayList<Light>(0);
		rows=r;
		cols=c;
		for(int i=0; i<r; i++)
		for(int j=0; j<c; j++)
		{
			lights.add(new Light(this,i,j));
		}
		lightSize=15;
	}
	public int getLightSize() { return lightSize; }
	public void setLightSize(int x) { lightSize=x; }
	public void execute()
	{
		double[] temp=new double[rows*cols];
		for(int i=0; i<rows*cols; i++)
			temp[i]=lights.get(i).getColor();
		for(int i=0; i<rows; i++)
		for(int j=0; j<cols; j++)
		{
			for(int di=-1; di<=1; di++)
			for(int dj=-1; dj<=1; dj++)
			{
				if((di!=0 || dj!=0) && (0<=i+di && i+di<rows) && (0<=j+dj && j+dj<cols))
					temp[(i+di)*cols+(j+dj)]+=lights.get(i*cols+j).getColor()*.95;
			}	
		}
		for(int i=0; i<rows*cols; i++)
			lights.get(i).setColor(temp[i]);
	}
	public boolean isClicked(MouseEvent e)
	{
		boolean clicked=false;
		for(int i=0; i<rows*cols && !clicked; i++)
			clicked=lights.get(i).isClicked(e);
		return clicked;
	}
	public void drawMe(Graphics g)
	{
		for(int i=0; i<rows*cols; i++)
			lights.get(i).drawMe(g);
	}
	public void zoomNotify()
	{
		for(int i=0; i<lights.size(); i++) lights.get(i).zoomNotify();
	} 
	public void onClickAction(MouseEvent e)
	{
		for(int i=0; i<lights.size(); i++)
			lights.get(i).onClickAction(e);
	}
	public Tangible getClickedComponent(MouseEvent e)
	{
		for(int i=0; i<rows*cols; i++)
			if(lights.get(i).isClicked(e))
				return lights.get(i).getClickedComponent(e);
		return this;
	}
}
class Light extends DraggableBox
{
	private int color;
	private LightGrid parent;
	int x, y;
	public Light(LightGrid p, int _x, int _y)
	{
		color=0;
		parent=p;
		x=_x;
		y=_y;
	}
	public void onClickAction(MouseEvent e)
	{
		if(isClicked(e))
		{
			System.out.println("Clicked "+x+","+y);
			if (e.getButton() == MouseEvent.BUTTON1)
				color += 1;
			if (color > 255)
				color = 255;
		}
	}
	public int getColor() { return color; }
	public void setColor(double x)
	{
		if(x<0)
			color=0;
		else if(0<=x && x<=255)
			color=(int)(x);
		else
			color=255;
		
	}
	public OrderedPair getPosition()
	{
		return new OrderedPair(parent.getPosition()).add(
			new OrderedPair(
				x*parent.getLightSize(),
				y*parent.getLightSize()
			)
		);
	}
	public OrderedPair getSize() { return new OrderedPair(parent.getLightSize(),parent.getLightSize()); }
	public Board getBoard() { return parent.getBoard(); }
	public OrderedPair getPixelDelta() { return parent.getPixelDelta(); }
	public void drawMe(Graphics g)
	{
		g.setColor(new Color(color,color,color));
		OrderedPair pixel = getBoard().posToPixel(getPosition());
		g.fillRect((int)(pixel.getX() + getPixelDelta().getX()),
				   (int)(pixel.getY() + getPixelDelta().getY()),
				   (int)(Math.round(getSize().getX() * getBoard().getZoomFactor())),
				   (int)(Math.round(getSize().getY() * getBoard().getZoomFactor())));
	}
	/*public boolean isClicked(MouseEvent e)
	{
		OrderedPair boardPos = getBoard().pixelToPos(new OrderedPair(e.getX(), e.getY()));
		boardPos.subtract(getPosition());
		return (0 <= boardPos.getX() && boardPos.getX() < getSize().getX()
			 && 0 <= boardPos.getY() && boardPos.getY() < getSize().getY());
	}*/
	public void setPixelDelta(OrderedPair x)
	{
		parent.setPixelDelta(x);
	}
	public void applyPixelDelta()
	{
		parent.applyPixelDelta();
	}
	public void zoomNotify() {}
}


		/*
		ColorBox A = new ColorBox(10, 10, 110, 110, 80);
		b.addTangible(A);

		ColorBox B = new ColorBox(60, 60, 65, 65, 0);
		b.addTangible(B);
		
		Link l1 = new Link(A, B);
		b.addTangible(l1);

		ImageBox thing = new ImageBox(100, 100, "box3.png");
		thing.setAxisAtPixel(200, 100);
		b.addTangible(thing);

		Link l2 = new Link(B, thing);
		b.addTangible(l2);

		TextBox tb = new TextBox(150, 150, "The quick brown fox jumped over the lazy dog.");
		Link l3 = new Link(B, tb);
		b.addTangible(tb);
		b.addTangible(l3);

		TextBox t1 = new TextBox("ABCDEF");
		TextBox t2 = new TextBox("GHI");
		TextBox t3 = new TextBox("JKLMNOPQ");
		Bucket bucket = new Bucket(10, 10);
		bucket.addItem(t1);
		bucket.addItem(t2);
		bucket.addItem(t3);
		Anchor t1a = new Anchor(t1);
		t1a.alignRight();

		bucket.addAnchor(t1a);
		b.addTangible(bucket);

		Link l4 = new Link(A, t1a);
		b.addTangible(l4);
		*/
