package DiagramDesigner;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.RenderingHints.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

class ColorBox extends DraggableBox
{
	private int colorR;
	private int colorG;
	private int colorB;

	public ColorBox(int x, int y, int w, int h, int c)
	{
		super();
		setPosition(new OrderedPair(x, y));
		setSize(new OrderedPair(w, h));
		colorR = c;
		colorG = c;
		colorB = c;
	}
	public void onClickAction(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
			colorR += 10;
		else if (e.getButton() == MouseEvent.BUTTON2)
			colorG += 10;
		else if (e.getButton() == MouseEvent.BUTTON3)
			colorB += 10;

		if (colorR > 255) colorR = 255;
		if (colorG > 255) colorG = 255;
		if (colorB > 255) colorB = 255;
	}
	public void drawMe(Graphics g)
	{
		g.setColor(new Color(colorR,colorG,colorB));
		OrderedPair pixel = getBoard().posToPixel(getPosition());
		g.fillRect((int)(pixel.getX() + getPixelDelta().getX()),
				   (int)(pixel.getY() + getPixelDelta().getY()),
				   (int)(Math.round(getSize().getX() * getBoard().getZoomFactor())),
				   (int)(Math.round(getSize().getY() * getBoard().getZoomFactor())));
	}
	public void zoomNotify() { }
}
