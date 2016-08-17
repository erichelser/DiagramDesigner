package DiagramDesigner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public abstract class Tangible
{
	private Board board;
	protected OptionsMenu optionsMenu;

	abstract public boolean isClicked(MouseEvent e);
	abstract public void onClickAction(MouseEvent e);
	abstract public void drawMe(Graphics g);
	abstract public void setPixelDelta(OrderedPair d);
	abstract public void applyPixelDelta();
	abstract public void zoomNotify(); //this is called whenever a zoom in/out occurs, just in case stuff has to get refreshed

	//adds Tangible elements of this item that have their own isClicked implementation, so
	//main function doesn't get cluttered with Board::addTangible calls for small stuff.
	//e.g. Anchor class for Buckets
	private static final ArrayList<Tangible> EMPTY_LIST = new ArrayList<Tangible>(0);
	
	public ArrayList<? extends Tangible> getTangibleChildren(){return EMPTY_LIST;}

	public OptionsMenu getOptionsMenu() { return optionsMenu; }

	protected Tangible()
	{
		setBlankOptionsMenu();
	}
	private void setBlankOptionsMenu()
	{
		optionsMenu = new OptionsMenu();

		JLabel label = new JLabel();
		label.setText("[Blank]");
		optionsMenu.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;

		optionsMenu.add(label, c);
	}
	public void setBoard(Board b)
	{
		board = b;
	}
	public Board getBoard() { return board; }
	public Tangible getClickedComponent(MouseEvent e)
	{
		return this;
	}
}

