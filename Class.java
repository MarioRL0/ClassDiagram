// imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

public class Class {

	// attributes
	private int _x;
	private int _y;
	private int width = 175;
	private int height = 250;
	private String _name;
	private boolean _selected;
	private boolean _preselected;
	private Vector<Association> _associations = new Vector();

	public Class(int x, int y, String name, boolean selected, boolean preselected, Vector<Association> associations) {
		_x = x;
		_y = y;
		_name = name;
		_selected = selected;
		_preselected = preselected;
		_associations = associations;
	}

	public void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		// ponemos el color con el que rellenamos el rectangulo
		Rectangle2D rectangulo = new Rectangle2D.Double(_x, _y, width, height);
		Line2D linea1 = new Line2D.Double(_x, _y + 50, _x + width, _y + 50);
		Line2D linea2 = new Line2D.Double(_x, _y + 150, _x + width, _y + 150);
		// class is selected, cyan
		if (!_selected && _preselected)
			g2.setColor(Color.cyan);
		// class is preselected
		else if (_selected)
			g2.setColor(Color.green);
		// class is not selected
		else
			g2.setColor(Color.white);
		g2.fillRect(_x, _y, width, height);
		g2.setColor(Color.black);
		g2.drawString(_name, _x + 5, _y + 20);
		g2.drawString("attributes", _x + 5, _y + 70);
		g2.drawString("operations", _x + 5, _y + 170);
		g2.draw(rectangulo);
		g2.draw(linea1);
		g2.draw(linea2);
	}

	public int getPosX() {
		return _x;
	}

	public void setPosX(int x) {
		_x = x;
	}

	public int getPosY() {
		return _y;
	}

	public void setPosY(int y) {
		_y = y;
	}

	public void moveClass(int x, int y) {
		_x = x;
		_y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getName() {
		return _name;
	}

	public void isSelected(boolean selected) {
		_selected = selected;
	}

	public void isPreselected(boolean preselected) {
		_preselected = preselected;
	}

	public void newAssociation(Association assoc) {
		_associations.add(assoc);
	}

	public Vector<Association> getAssociations() {
		return _associations;
	}

}
