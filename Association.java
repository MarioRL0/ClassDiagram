import java.awt.Graphics;
import java.awt.Graphics2D;
//otros imports …
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Association {

	// Atributos
	private int id;
	private Class class1;
	private Class class2;

	// Constructores
	Association(Class _class1, Class _class2) {
		class1 = _class1;
		class2 = _class2;
	}

	public void draw(Graphics graphics) {
		// Dibuja la asociación
		int class1_center_X = class1.getPosX() + (int) (class1.getWidth() / 2);
		int class1_center_Y = class1.getPosY() + (int) (class1.getHeight() / 2);
		int class2_center_X = class2.getPosX() + (int) (class2.getWidth() / 2);
		int class2_center_Y = class2.getPosY() + (int) (class2.getHeight() / 2);
		Graphics2D g2 = (Graphics2D) graphics;
		Line2D asociacion = new Line2D.Double(class1_center_X, class1_center_Y, class2_center_X, class2_center_Y);
		// un cuadrado para auto asociacion
		Rectangle2D auto_asociacion = new Rectangle2D.Double(class1_center_X, class1.getPosY()-(int)class1.getWidth()/3,
				class1.getWidth(),class1.getWidth());
		//Line2D auto_asociacion = new Line2D.Double();
		if (!class2.getName().equals(class1.getName())) {
			// es una asociacion entre 2 clases
			g2.draw(asociacion);
		} else {
			// es una autoasociacion, las coordenadas para pintarla son
			// distintas
			g2.draw(auto_asociacion);
		}
	}

	// Más métodos
	public Class getClass1() {
		return class1;
	}

	public Class getClass2() {
		return class2;
	}
	// eliminar despues
	public int getID() {
		return id;
	}
}
