import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window implements ActionListener {

	// attributes
	private Diagram diagram; // draw management
	private JPanel panel;
	private GridBagConstraints c;
	private JButton button; // used to add class
	private JLabel labelNClasses;
	private JLabel labelNAssociations;

	// methods
	public Window() {
		super();
		// JPanel creation and grid
		panel = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
	}

	public Component createComponents() {
		// Add class JButton to Jpanel
		button = new JButton("Add Class");
		button.setMnemonic(KeyEvent.VK_I);
		button.addActionListener(this);
		setGridProperties(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE);
		panel.add(button, c);

		// Diagram creation
		diagram = new Diagram(this);
		setGridProperties(0, 1, 4, 3, 1.0, 1.0, GridBagConstraints.BOTH);
		panel.add(diagram, c);

		// Class counter creation
		labelNClasses = new JLabel("Classes: " + diagram.getNClasses());
		setGridProperties(0, 4, 2, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		panel.add(labelNClasses, c);

		// Association counter creation
		labelNAssociations = new JLabel("Associations: " + diagram.getNAssociations());
		setGridProperties(2, 4, 2, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		panel.add(labelNAssociations, c);

		// border settings
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return panel;
	}

	private void setGridProperties(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty,
			int fill) {
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		c.gridheight = gridheight;
		c.weightx = weightx;
		c.weighty = weighty;
		c.fill = fill;
	}
	// Define JButton interaction
	public void actionPerformed(ActionEvent e) {
		diagram.addClass();
		diagram.requestFocusInWindow();
	}
	// Update class count
	public void updateNClasses(Diagram d) {
		labelNClasses.setText("Classes: " + d.getNClasses());
	}
	// Update associations count
	public void updateNAssociations(Diagram d) {
		labelNAssociations.setText("Associations: " + diagram.getNAssociations());
	}
}
