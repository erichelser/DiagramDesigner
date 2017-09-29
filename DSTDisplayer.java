import DiagramDesigner.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.util.*;


//This class simply launches the JFrame in a non-static context
public class DSTDisplayer implements Command
{
	static ArrayList<Stitch> sl =new ArrayList<Stitch>();
	static ArrayList<TextBox> tb=new ArrayList<TextBox>();
	static ArrayList<Double> xc=new ArrayList<Double>();
	static ArrayList<Double> yc=new ArrayList<Double>();
	static ArrayList<Double> zc=new ArrayList<Double>();
	static int LIMIT=3;
	static double offset=0;
	static final double TAU=3.1415926*2;
	Periodic p;

	public static void main(String[] args)
	{
		new DSTDisplayer();
	}
	public DSTDisplayer()
	{
		Board b = new Board(800, 600);
		b.showGrid(100);


		for(int i=0; i<LIMIT; i++)
		{
			Stitch ns=new Stitch(100*Math.cos(TAU*i/LIMIT),
			                     100*Math.sin(TAU*i/LIMIT),
			                     2*100*Math.sin(TAU/2/LIMIT),
			                     90+(0.5+i)*(360/LIMIT),
			                     new Color(0,0,255));

			b.addTangible(ns);
			sl.add(ns);

			xc.add(100*Math.cos(TAU*i/LIMIT));
			yc.add(100*Math.sin(TAU*i/LIMIT));
			zc.add(0.0);
			TextBox t=new TextBox((int)xc.get(xc.size()-1).doubleValue(),(int)yc.get(yc.size()-1).doubleValue(),""+i);
			b.addTangible(t);
			tb.add(t);
		}
		p=new Periodic(60,this);
		p.start();
	}
	public void execute()
	{
		offset+=0.02;
		for(int i=0; i<LIMIT; i++)
		{
			sl.get(i).setPosition(100*Math.cos(TAU*i/LIMIT+offset),
			                      100*Math.sin(TAU*i/LIMIT+offset));
			sl.get(i).setAngle(90+(0.5+i)*(360.0/LIMIT)+offset*360/TAU);
			sl.get(i).updateRotatedImage();

			xc.set(i,100*Math.cos(TAU*i/LIMIT+offset));
			yc.set(i,100*Math.sin(TAU*i/LIMIT+offset));
			zc.set(i,4*Math.cos((i+5)*offset));
			tb.get(i).setPosition(renderpos(xc.get(i),yc.get(i),zc.get(i)));
		}
	}
	public OrderedPair renderpos(double x, double y, double z)
	{
		double Px=0, Py=0, Pz=5; //perspective point
		return new OrderedPair(x*Pz/(Pz-z),y*Pz/(Pz-z));
	}
}

class Stitch extends ImageBox
{
	private BufferedImage img;
	private double length, thickness;
	private Color threadColor;
	private Stitch()
	{
		super();
	}
	public Stitch(double posX, double posY, double _length, double angle, Color c)
	{
		this();
		thickness=4;
		length=_length;

		threadColor=c;
		renderThread();

		super.setBufferedImage(img);
		super.setAxisAtRatio(0.0,0.5);
		setPosition(posX,posY);
		setAngle(angle);
		setAxisAsPosition();
	}
	private void renderThread()
	{
		img=ThreadRenderer.renderThread((int)length,(int)thickness,threadColor);
	}
	public void zoomNotify()
	{
		super.zoomNotify();
	}
}

class ThreadRenderer
{
	//Memoization of previously created thread images
	private static HashMap<String,BufferedImage> cache=new HashMap<String,BufferedImage>();

	public static BufferedImage renderThread(int length, int thickness, Color threadColor)
	{
		String hashKey=length+"."+thickness+"."+threadColor.getRed()+"."+threadColor.getGreen()+"."+threadColor.getBlue();
		if(cache.get(hashKey)!=null)
			return cache.get(hashKey);

		BufferedImage img=new BufferedImage((int)length,(int)thickness,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d=img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		g2d.setColor(threadColor);
		g2d.fill(new Rectangle2D.Double(0,0,length,thickness));

		
		//Add contrasting color for edges/reflections
		g2d.setStroke(new BasicStroke(1));
		for(double i=-thickness; i<=length; i+=2)
		{
			double pos=i/(length-thickness);
			g2d.setColor(ColorAdjust.shade(threadColor,0.5*cube(2*pos-1)));
			g2d.draw(new Line2D.Double(i,0,i+thickness,thickness));
		}
		
		g2d.dispose();
		cache.put(hashKey,img);

		return img;
	}
	private static double cube(double x){return x*x*x;}
}

class ColorAdjust
{
	public static Color shade(Color c, double amount)
	{
		if(amount==0) return c;
		if(amount<0) return darken(c,-amount);
		return lighten(c,amount);
	}
	public static Color darken(Color c, double amount)
	//Brings color provided closer to black (0,0,0) by a certain factor.
	//An amount of 0.0 has no effect, while 1.0 will return black.
	//Any value between 0-1 will darken the color by that proportion.
	{
		if(amount<=0) return c;
		if(amount>=1) amount=1;

		int R=(int)Math.round(c.getRed() * (1-amount));
		int G=(int)Math.round(c.getGreen() * (1-amount));
		int B=(int)Math.round(c.getBlue() * (1-amount));
		int A=(int)Math.round(c.getAlpha());

		return new Color(Math.round(R), (int)Math.round(G), Math.round(B), Math.round(A));
	}
	public static Color lighten(Color c, double amount)
	{
		if(amount<=0) return c;
		if(amount>=1) amount=1;

		int R=(int)Math.round(c.getRed() * (1-amount) + 255*amount);
		int G=(int)Math.round(c.getGreen() * (1-amount) + 255*amount);
		int B=(int)Math.round(c.getBlue() * (1-amount) + 255*amount);
		int A=(int)Math.round(c.getAlpha());

		return new Color(Math.round(R), (int)Math.round(G), Math.round(B), Math.round(A));
	}
}