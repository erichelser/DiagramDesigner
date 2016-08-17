package DiagramDesigner;
import javax.swing.*;

public class MenuButton extends JButton implements Command
{
	private String displayText;
	private Board parentBoard;

	public MenuButton(){this("");}
	public MenuButton(String s)
	{
		super();
		displayText=s;
		setText(displayText);
	}
	public void setParentBoard(Board b)
	{
		parentBoard=b;
	}
	public Board getParentBoard()
	{
		return parentBoard;
	}
	public void execute()
	{
		parentBoard.getBackgroundOptionsMenu().setColor(200,200,200);
	}
	public static final long serialVersionUID=0;
}