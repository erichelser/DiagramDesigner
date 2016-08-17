package DiagramDesigner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Window-based container for Board
//Sets initial size of screen and starts up Board thread.
public class DDWindow implements ComponentListener, ActionListener
{
	private Board myBoard;
	private JPanel windowNorth;
	private JPanel windowEast;
	private JFrame window;
	private JPanel container;
	public DDWindow(Board paramBoard)
	{
		myBoard = paramBoard;
		myBoard.setGameWindow(this);
		window = new JFrame("Main Window");

		FlowLayout myFL = new FlowLayout();
		myFL.setAlignment(FlowLayout.LEFT);
		windowNorth = new JPanel(myFL);

		for(MenuButton mb:myBoard.getMenuButtons())
		{
			windowNorth.add(mb);
			mb.addActionListener(this);
		}

		windowEast = myBoard.getOptionsMenu();

		container = new JPanel();
		container.setPreferredSize(new Dimension(paramBoard.getPixelDim().getXInt(), paramBoard.getPixelDim().getYInt()));
		container.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		container.add(windowNorth, c);

		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		container.add(myBoard, c);

		c.gridx = 1;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		container.add(windowEast, c);

		window.add(container);
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		(new Thread(myBoard)).start();

		window.addComponentListener(this);
	}

	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentShown(ComponentEvent e) { }
	public void componentResized(ComponentEvent e)
	{
		myBoard.setPixelDim(myBoard.getSize().width, myBoard.getSize().height);
		container.setPreferredSize(container.getSize());
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof Command)
			((Command)(e.getSource())).execute();
	}

	public void alertOptionsMenuChanged()
	{
		container.remove(windowEast);
		windowEast = myBoard.getOptionsMenu();

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0;
		c.weighty = 0;
		container.add(windowEast, c);
		window.pack();
		myBoard.setPixelDim(myBoard.getSize().width, myBoard.getSize().height);
		window.repaint();
	}
}