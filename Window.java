import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window implements ActionListener {

	// atributos
	private Diagram diagram; // superficie de dibujo
	private JPanel panel;
	private GridBagConstraints c;
	private JButton button; // para añadir clases
	private JLabel labelNClasses;
	private JLabel labelNAssociations;

	// metodos
	public Window() {
		super();
		// creamos el panel y el grid
		panel = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
	}

	public Component createComponents() {
		// creamos el boton de añadir clases y lo ponemos en el panel
		button = new JButton("Add Class");
		button.setMnemonic(KeyEvent.VK_I);
		button.addActionListener(this);
		setGridProperties(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE);
		panel.add(button, c);

		// creamos el diagrama y lo ponemos en el panel
		diagram = new Diagram(this);
		setGridProperties(0, 1, 4, 3, 1.0, 1.0, GridBagConstraints.BOTH);
		panel.add(diagram, c);

		// creamos la etiqueta para contar clases y lo ponemos en el panel
		labelNClasses = new JLabel("Classes: " + diagram.getNClasses());
		setGridProperties(0, 4, 2, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		panel.add(labelNClasses, c);

		// creamos la etiqueta para contar asociaciones y lo ponemos en el panel
		labelNAssociations = new JLabel("Associations: " + diagram.getNAssociations());
		setGridProperties(2, 4, 2, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		panel.add(labelNAssociations, c);

		// ultimos ajustes del panel
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

	public void actionPerformed(ActionEvent e) {
		diagram.addClass();
		diagram.requestFocusInWindow();
	}

	public void updateNClasses(Diagram d) {
		labelNClasses.setText("Classes: " + d.getNClasses());
	}

	public void updateNAssociations(Diagram d) {
		labelNAssociations.setText("Associations: " + diagram.getNAssociations());
	}
}
