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

class TextImage
{
	private BufferedImage textImage;
	private OrderedPair size;

	private String message;
	private String fontName;
	private int fontSize;
	private Color backgroundColor;
	private Color textColor;

	private boolean isDirty;

	private TextImage()
	{
		fontName = "Monospaced";
		backgroundColor = new Color(222, 222, 222);
		textColor = new Color(0, 0, 0);
		fontSize = 14;
		isDirty = true;
	}

	public TextImage(String text)
	{
		this();
		setMessage(text);
	}
	public void setMessage(String text)
	{
		isDirty = true;
		message = text;
	}

	public BufferedImage getImage(Graphics g)
	{
		if (isDirty) renderText(g);
		return textImage;
	}

	public OrderedPair getSize(Graphics g)
	{
		if (isDirty) renderText(g);
		return size;
	}

	private void renderText(Graphics g)
	{
		isDirty = false;

		Font baseFont = new Font(fontName, Font.PLAIN, fontSize);
		FontMetrics baseFontMetrics = g.getFontMetrics(baseFont);
		size = new OrderedPair(baseFontMetrics.stringWidth(message),
			                   baseFontMetrics.getAscent() + baseFontMetrics.getDescent());

		textImage = new BufferedImage(size.getXInt(), size.getYInt(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D textImageGraphics = textImage.createGraphics();
		//textImageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		//textImageGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		textImageGraphics.setColor(backgroundColor);
		textImageGraphics.fillRect(0, 0, size.getXInt(), size.getYInt());
		textImageGraphics.setColor(textColor);
		textImageGraphics.setFont(baseFont);
		textImageGraphics.drawString(message, 0, size.getYInt() - baseFontMetrics.getDescent());
	}
}
