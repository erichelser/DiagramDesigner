package DiagramDesigner;

public class OrderedPair
{
	private double X;
	private double Y;

	public OrderedPair()
		{ set(0, 0); }
	public OrderedPair(double initX, double initY)
		{ set(initX, initY); }
	public OrderedPair(OrderedPair rhs)
		{ set(rhs); }

	public double getX()
		{ return X; }
	public double getY()
		{ return Y; }
	public int getXInt()
		{ return (int)(Math.round(X)); }
	public int getYInt()
		{ return (int)(Math.round(Y)); }

	public void setX(double V)
		{ X = V; }
	public void setY(double V)
		{ Y = V; }
	public void set(double A, double B)
		{ X = A; Y = B; }
	public void set(OrderedPair rhs)
		{ set(rhs.getX(),rhs.getY()); }
	public void setZero()
		{ set(0.0, 0.0); }

	public OrderedPair add(OrderedPair rhs)
	{
		X += rhs.getX();
		Y += rhs.getY();
		return this;
	}
	public OrderedPair subtract(OrderedPair rhs)
	{ 
		X -= rhs.getX();
		Y -= rhs.getY();
		return this;
	}
	public OrderedPair multiply(double c)
	{
		X *= c;
		Y *= c;
		return this;
	}
	public OrderedPair rotateAboutOrigin(double theta)
	{
		double H = Math.sqrt(X * X + Y * Y); //length of hypotenuse
		double A = Math.atan2(Y, X); //angle formed by vector to (X,Y)
		X = H * Math.cos(A + Math.toRadians(theta));
		Y = H * Math.sin(A + Math.toRadians(theta));
		return this;
	}
	public OrderedPair getHalf()
	{
		return new OrderedPair(X / 2, Y / 2);
	}

	public String toString()
	{
		return "[ " + X + " , " + Y + " ]";
	}

	public OrderedPair replicate()
	{
		return new OrderedPair(X, Y);
	}
}