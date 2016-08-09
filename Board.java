import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

//Board class coordinates View and Controller aspects of game.
//Handles all mouse/keyboard input and dispatches control to
//whichever on-screen object is clicked.
//Also provides methods for translating internal "position" to
//actual pixels so each individual object can draw itself.
class Board extends JPanel implements Runnable, MouseListener, ActionListener, MouseMotionListener,
                                      MouseWheelListener
{
	//Visual environment parameters
	private OrderedPair pixelDim;
	private OrderedPair pixelMouseDownAt;
	private OrderedPair posCenter;

	private boolean mouseDown;
	private OrderedPair pixelDelta;
	private double zoomFactor;

	//Tangibles are the on-screen elements. They can be a static size,
	//dynamically sized, or anchored to a specific point on the board.
	//Tangibles contain their own code for reacting to clicks and 
	//drawing themselves.
	//Note: ArrayList is ordered from background (index 0) to foreground (index n-1)
	private ArrayList<Tangible> tangibles;
	private Tangible clickedTangible;

	private Periodic periodic;
	public class Repainter implements Command
	{ public void execute() { summonRepaint(); } }

	//private JPanel optionsMenu;
	//private JPanel backgroundOM;
	private OptionsMenu activeOptionsMenu;
	private BackgroundOptionsMenu myBackgroundOptionsMenu;

	private GameWindow containingGameWindow;

	public Board()
	{
		pixelDim = new OrderedPair();
		posCenter = new OrderedPair();

		zoomFactor = 1;
		//Initialize mouse-control variables...
		mouseDown = false;
		pixelMouseDownAt = new OrderedPair(0, 0);

		clickedTangible = null;
		pixelDelta = new OrderedPair(0, 0);

		//Initialize tangibles container
		tangibles = new ArrayList<Tangible>(0);

		myBackgroundOptionsMenu = new BackgroundOptionsMenu();

		setOptionsMenu(myBackgroundOptionsMenu);
	}

	public Board(int xDim, int yDim)
	{
		this();
		setPixelDim(xDim, yDim);
	}

	public void setGameWindow(GameWindow gw)
	{
		containingGameWindow = gw;
	}

	public void run()
	{
		setVisible(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		periodic = new Periodic(60, new Repainter());
		periodic.start();

		//Initialize graphics parameters...
		pixelDim = new OrderedPair(this.getWidth(), this.getHeight());
		posCenter = new OrderedPair(this.getWidth() / 2, this.getHeight() / 2);
	}

	public void addTangible(Tangible t)
	{
		if(t!=null)
		{
			t.setBoard(this);
			tangibles.add(t);
			ArrayList<? extends Tangible> list = t.getTangibleChildren();
			for (int i = 0; i < list.size(); i++)
				addTangible(list.get(i)); //recursive add
		}
	}

	public OrderedPair pixelToPos(OrderedPair pixel)
	{
		OrderedPair position = new OrderedPair();
		position.add(pixel);
		position.subtract(pixelDim.getHalf());
		position.subtract(pixelDelta);
		position.multiply(1 / zoomFactor);
		position.add(posCenter);
		return position;
	}

	public OrderedPair posToPixel(OrderedPair pos)
	{
		OrderedPair pixel = new OrderedPair();
		pixel.add(pos);
		pixel.subtract(posCenter);
		pixel.multiply(zoomFactor);
		pixel.add(pixelDim.getHalf());
		pixel.add(pixelDelta);
		return pixel;
	}

	public double getZoomFactor() { return zoomFactor; }

	public void paintComponent(Graphics g)
	{
		//Fill background color in
		g.setColor(myBackgroundOptionsMenu.getColor());
		g.fillRect(0,0,pixelDim.getXInt(),pixelDim.getYInt());

		//Draw each Tangible object from background to foreground
		for(int i=0; i<tangibles.size(); i++)
			tangibles.get(i).drawMe(g);
	}

	//Determine which object on the screen was clicked.
	public Tangible getClickedTangible(MouseEvent e)
	{
		for (int i = tangibles.size() - 1; i >= 0; i--)
		{
			if (tangibles.get(i).isClicked(e))
			{
				setOptionsMenu(tangibles.get(i).getOptionsMenu());
				return tangibles.get(i);
			}
		}
		setOptionsMenu(myBackgroundOptionsMenu);
		return null;
	}

	public void mousePressed(MouseEvent e)
	{
		clickedTangible = getClickedTangible(e);
		mouseDown = true;
		pixelMouseDownAt.set(getMouseOrderedPairPixel(e));

		//System.out.println(e);
		//System.out.println(clickedTangible);
	}

	public void mouseReleased(MouseEvent e)
	{
		//System.out.println(e);
		mouseDown = false;
		if (clickedTangible != null)
		{
			clickedTangible.applyPixelDelta();
		}
		else
		{
			posCenter.subtract(pixelDelta.multiply(1 / zoomFactor));
			pixelDelta.setZero();
		}
	}

	public void mouseEntered(MouseEvent e)
	{
		//System.out.println(e);
	}
	public void mouseExited(MouseEvent e)
	{
		//System.out.println(e);
	}

	//Determine which object was clicked (from foreground to background)
	//and send that object a notification that they are to react to a click.
	public void mouseClicked(MouseEvent e)
	{
		//System.out.println(e);
		boolean objectFound = false;
		for (int i = tangibles.size() - 1; i >= 0 && !objectFound; i--)
		{
			if (tangibles.get(i).isClicked(e))
			{
				tangibles.get(i).onClickAction(e);
				objectFound = true;
			}
		}
		repaint();
	}

	public void mouseDragged(MouseEvent e)
	{
		if (mouseDown)
		{
			//System.out.println(e);
			if (clickedTangible != null)
			{
				clickedTangible.setPixelDelta(getMouseOrderedPairPixel(e).subtract(pixelMouseDownAt));
			}
			else
			{
				pixelDelta.set(getMouseOrderedPairPixel(e).subtract(pixelMouseDownAt));
			}
			repaint();
		}
	}	
	public void mouseMoved(MouseEvent e) { }
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if (clickedTangible != null)
		{
			clickedTangible.applyPixelDelta();
			pixelMouseDownAt.set(getMouseOrderedPairPixel(e));
			clickedTangible.setPixelDelta(new OrderedPair());
		}

		double oldZoomFactor = zoomFactor;
		if (e.getWheelRotation() < 0)
		{
			zoomFactor *= 1.1;
		}
		else
		{
			zoomFactor /= 1.1;
		}
		if (zoomFactor < 0.1) zoomFactor = 0.1;
		if (zoomFactor > 9.9) zoomFactor = 9.9;

		OrderedPair mousePos = getMouseOrderedPairPos(e);
		posCenter.subtract(mousePos);
		posCenter.multiply(oldZoomFactor / zoomFactor);
		posCenter.add(mousePos);

		for (int i = tangibles.size() - 1; i >= 0; i--)
		{
			tangibles.get(i).zoomNotify();
		}
	}

	public void actionPerformed(ActionEvent e) { repaint(); }
	public void summonRepaint() { repaint(); }
	public OrderedPair getMouseOrderedPairPixel(MouseEvent e)
	{
		return new OrderedPair(e.getX(), e.getY());
	}
	public OrderedPair getMouseOrderedPairPos(MouseEvent e)
	{
		return pixelToPos(getMouseOrderedPairPixel(e));
	}
	public OrderedPair getPixelDim()
	{
		return pixelDim;
	}
	public void setPixelDim(int xDim, int yDim)
	{
		OrderedPair centerDiff = new OrderedPair(xDim, yDim);
		centerDiff.subtract(pixelDim);
		centerDiff.multiply(0.5);
		posCenter.add(centerDiff);

		pixelDim.setX(xDim);
		pixelDim.setY(yDim);
		setPreferredSize(new Dimension(xDim, yDim));
	}

	public void setOptionsMenu(OptionsMenu o)
	{
		activeOptionsMenu = o;
		if (containingGameWindow != null)
			containingGameWindow.alertOptionsMenuChanged();
	}

	public OptionsMenu getOptionsMenu()
	{
		return activeOptionsMenu;
	}
	private static final long serialVersionUID=0;
}