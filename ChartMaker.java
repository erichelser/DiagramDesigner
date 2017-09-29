import DiagramDesigner.*;

//This class simply launches the JFrame in a non-static context
public class ChartMaker
{
	private static Preferences prefs;

	public static void main(String[] args)
	{
		Board b = new Board(800, 600);

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

		//Bucket.link(letterABucket,0,letterBBucket,0);
		//Bucket.link(letterBBucket,0,letterCBucket,0);
		//Bucket.link(letterABucket,1,letterCBucket,1);

		Bucket text=new Bucket();
		TextBox t=new TextBox(0,0,"Test");
		text.addItem(t);
		text.setPosition(210,210);
		text.createAnchors(1,0);

		//Bucket.link(letterCBucket,2,text,0);

		EventNode e=new EventNode(300,300,3,6);
		EventNode e2=new EventNode(400,300,5,1);
		EventNode e3=new EventNode(500,400,3,3);
		
		Bucket.link(e,0,e2,0);
		Bucket.link(e2,0,e3,1);

		prefs=new Preferences("ChartMakerPrefs.txt");
		OpenButton mb1=new OpenButton("Open",prefs);
		SaveButton mb2=new SaveButton("Save",prefs);
		b.addMenuButton(mb1);
		b.addMenuButton(mb2);
		
		e3.setImage("images/box2.png");

		//Must happen last - otherwise Link objects may not get drawn
		b.addTangible(letterABucket);
		b.addTangible(letterBBucket);
		b.addTangible(letterCBucket);
		b.addTangible(text);
		b.addTangible(e);
		b.addTangible(e2);
		b.addTangible(e3);

	}
}
