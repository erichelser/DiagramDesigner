import javax.swing.*;
import java.awt.*;

class BackgroundOptionsMenu extends OptionsMenu
{
	private SpinnerNumberModel colorRModel, colorGModel, colorBModel;
	private JSpinner colorR, colorG, colorB;
	private JLabel colorRLabel, colorGLabel, colorBLabel;
	public BackgroundOptionsMenu()
	{
		super();
		colorRModel = new SpinnerNumberModel(255, 0, 255, 1);
		colorGModel = new SpinnerNumberModel(255, 0, 255, 1);
		colorBModel = new SpinnerNumberModel(255, 0, 255, 1);

		colorR = new JSpinner(colorRModel);
		colorG = new JSpinner(colorGModel);
		colorB = new JSpinner(colorBModel);

		colorRLabel = new JLabel("Red");
		colorGLabel = new JLabel("Green");
		colorBLabel = new JLabel("Blue");

		colorRLabel.setLabelFor(colorR);
		colorGLabel.setLabelFor(colorG);
		colorBLabel.setLabelFor(colorB);

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		add(colorRLabel, c);

		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 0;
		add(colorR, c);

		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		add(colorGLabel, c);

		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 1;
		add(colorG, c);

		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 2;
		add(colorBLabel, c);

		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 2;
		c.weighty = 1;
		add(colorB, c);
	}

	public Color getColor()
	{
		int r = ((Integer)colorRModel.getValue()).intValue();
		int g = ((Integer)colorGModel.getValue()).intValue();
		int b = ((Integer)colorBModel.getValue()).intValue();
		return new Color(r, g, b);
	}
	private static final long serialVersionUID=0;
}
