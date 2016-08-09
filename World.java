//This class simply launches the JFrame in a non-static context
public class World
{
	public static void main(String[] args)
	{
		Board b = new Board(800, 600);
		new GameWindow(b);

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

		Bucket letterBBucket = Bucket.createFromImageBox("B_50x50.png", 110, 110, 1, 1);
		Bucket letterABucket = Bucket.createFromImageBox("A_50x50.png", 210, 110, 2, 1);
		Bucket letterCBucket = Bucket.createFromImageBox("C_50x50.png", 110, 210, 1, 5);

		letterBBucket.linkTo(letterABucket, 0, 1);
		letterABucket.linkTo(letterCBucket, 0, 0);
		letterCBucket.linkTo(letterBBucket, 4, 0);

		b.addTangible(letterABucket);
		b.addTangible(letterBBucket);
		b.addTangible(letterCBucket);
	}
}
