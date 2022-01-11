import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

import javax.swing.*;
import java.util.*;

public class Diagram extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	// atributos
	private Window window;// Ventana en la que est√° el diagrama
	public Class clase;

	private Vector<Class> classes = new Vector(); // las clases que crea el
													// usuario
	private Vector<Association> associations = new Vector(); // las asociaciones
																// que crea el
	// usuario

	// Variables metodo keyTyped()
	private boolean preselectedClassBool = false; // Lo inicializamos a false
													// pero en un
	// futuro
	// se cambia en metodo
	private Class preselectedClass;

	// Variables globales (addClass)
	private String newClassName;
	private int classCounter = 0;
	private int x = 50;
	private int y = 50;

	// Atributos metodo mouseOn()
	private Rectangle rec;
	private Class classOn;
	private Class classOnPrev = null;
	private int indexClassOn;
	private int xCoor;
	private int yCoor;
	private int wid;
	private int hei;

	// Atributos del metodo keyTypped()
	private boolean sTyped;
	private boolean STyped;

	// Atributos metodo mouseDragged()
	private boolean assocToCrea = false;
	private boolean unionBool = false;

	// Atributos para borrar asoc
	private Vector<Association> assocToRemove = new Vector();

	Association newAssoc;
	private MouseEvent e;

	int coorX1;
	int coorY1;
	int coorX2;
	int coorY2;

	// metodos
	public Diagram(Window theWindow) {
		window = theWindow;
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void addClass() {
		if (!preselectedClassBool) {
			newClassName = "Class " + classCounter;
			classes.add(new Class(x, y, newClassName, false, false, new Vector()));
			classCounter++;
			window.updateNClasses(this);
			x = x + 20;
			y = y + 20;
		}
	}

	public int getNClasses() {
		return classes.size();
	}

	public int getNAssociations() {
		return associations.size();
	}

	public void paint(Graphics g) {
		// Dibuja el diagrama de clases
		super.paint(g);
		// Recorre el vector de asociaciones y las dibuja una a una
		for (int i = 0; i < associations.size(); i++) {
			associations.get(i).draw(g);
		}
		if (unionBool) {
			Graphics2D g2 = (Graphics2D) g;
			Line2D union = new Line2D.Double(coorX1, coorY1, coorX2, coorY2);
			g2.draw(union);
		}
		// Recorre el vector de clases y las dibuja una a una
		for (int i = 0; i < classes.size(); i++) {
			classes.get(i).draw(g);
		}
		repaint();
	}

	/*
	 * A este metodo se le llama cada vez que el raton pasa por encima de una
	 * clase y esta no es la ultima del vector de clases
	 */
	public void overlap() {
		// Recorre el vector de clases reorganizandolo de forma que la clase
		// sobre la que esta situada el raton sea la ultima del vector
		for (int j = indexClassOn; j < classes.size() - 1; j++) {
			classes.set(j, classes.elementAt(j + 1));
		}
		classes.set(classes.size() - 1, classOn);
	}

	/********************************************/
	/********* Metodos de MouseListener *********/
	/********************************************/

	/* A este metodo se le llama cuando un boton de nuestro raton es pulsado */
	public void mousePressed(MouseEvent e) {
		if ((classOn == preselectedClass) && preselectedClassBool) {
			assocToCrea = true;
			unionBool = true;
		} else {
			assocToCrea = false;
			unionBool = false;

		}
	}

	/*
	 * A este metodo se le llama cuando un boton de nuestro raton deja de ser
	 * pulsado
	 */
	public void mouseReleased(MouseEvent e) {
		unionBool = false;
		// Cuando arrastramos para crear una asociacion de forma valida. Para
		// crear una asoc. de forma valida:
		// 1. Debe de haber una clase seleccionada (preselectedClassBool ==
		// true)
		// 2. Se debe de arrastar el raton con el click izquierdo empezando
		// desde la clase seleccionada (assocToCrea == true)
		// 3. Se debe de soltar el raton encima de una clase existente
		// (classOn != null)
		if (preselectedClassBool && (classOn != null) && assocToCrea) {
			// Ya no se pueden hacer mas asociaciones hasta que se seleccione
			// otra clase
			assocToCrea = false;
			// unionBool = false;
			// Pintamos las clases de blanco
			preselectedClass.isPreselected(false);
			classOn.isSelected(false);
			// Creamos asociacion y la anadimos al vector de asociaciones
			newAssoc = new Association(preselectedClass, classOn);
			associations.add(newAssoc);
			// Anadimos la asociacion a las clases implicadas
			preselectedClass.newAssociation(newAssoc);
			// En el caso de que no sea una autoAsociacion se aÒadira esta a las
			// dos clases
			if (preselectedClass != classOn) {
				classOn.newAssociation(newAssoc);
			}
			// Ya no hay ninguna clase seleccionada
			preselectedClass = null;
			preselectedClassBool = false;
		}
		// Actualizamos la etiqueta que lleva el recuento de asociaciones
		window.updateNAssociations(this);

	}

	/* A este metodo se le llama cuando el raton entra en nuestro diagrama */
	public void mouseEntered(MouseEvent e) {
	}

	/* A este metodo se le llama cuando el raton sale de nuestro diagrama */
	public void mouseExited(MouseEvent e) {
		// String a = classPressed.getName();
	}

	/*
	 * A este metodo se le llama cuando es clickado (pulsar + soltar) algun
	 * boton del raton de nuestro ordenador
	 */
	public void mouseClicked(MouseEvent e) {
		// Si no hay ninguna clase seleccionada, y hacemos click derecho sobre
		// una clase, esta se eliminara
		if (!preselectedClassBool && (e.getButton() == MouseEvent.BUTTON3) && (classOn != null)) {
			// Eliminamos asociaciones, para ello pedimos que se nos
			// proporcionen las asociaciones
			assocToRemove = classOn.getAssociations();
			for (int i = 0; i < assocToRemove.size(); i++) {
				associations.remove(assocToRemove.get(i));
			}
			// Eliminamos clase a la que le hemos hecho click dcho
			classes.remove(classOn);
			// Actualizamos contadores
			window.updateNClasses(this);
			window.updateNAssociations(this);
		}
	}

	/********************************************/
	/******* Metodos de MouseMotionListener *****/
	/********************************************/

	/*
	 * A este metodo se le llama siempre que el raton se mueve por el diagrama
	 */
	public void mouseMoved(MouseEvent e) {
		for (int i = classes.size() - 1; i >= 0; i--) {

			// Coordenadas de la esquina superior del rectangulo
			xCoor = classes.get(i).getPosX(); // Eje de la x
			yCoor = classes.get(i).getPosY(); // Eje de la y
			// Parametros del rectangulo
			wid = classes.get(i).getWidth(); // Ancho del rectangulo
			hei = classes.get(i).getHeight(); // Alto del rectangulo

			rec = new Rectangle(xCoor, yCoor, wid, hei);

			// Si el rectangulo contiene al raton guardamos esa clase en la
			// variables classOn
			if (rec.contains(e.getPoint())) {
				classOn = classes.get(i);
				indexClassOn = i;
				// Si la clase sobre la que esta el raton no se encuentra
				// dibujada por encima de las demas clases se llama al metodo
				// overlap() y hace que se redibuje por encima reorganizando el
				// vector de clases
				if (i != classes.size() - 1) {
					overlap();
				}
				break;
			} else {
				// Si el raton no esta sobre ninguna clase, en la variable
				// classOn no se guardara nada
				classOn = null;
			}
		}
	}

	/*
	 * A este metodo se le llama siempre que se mueve el raton con algun boton
	 * del raton pulsado
	 */
	public void mouseDragged(MouseEvent e) {

		if (!preselectedClassBool && (classOn != null)) {
			// Si hay alguna clase seleccionada no se podra mover ningun
			// elemento
			// del diagrama (preselectedClassBool == false). Ademas, para que se
			// pueda arrastrar una clase el raton debera estar sobre ella
			// (classOn != null)

			// Le cambiamos las coordenadas a la clase que queremos mover
			classOn.moveClass(e.getX(), e.getY());
		} else if (preselectedClassBool) {
			mouseMoved(e);
			// !!!!!!!!!SEGUIR AQUIIIIIII
			coorX1 = preselectedClass.getPosX() + (int) (preselectedClass.getWidth() / 2);
			coorY1 = preselectedClass.getPosY() + (int) (preselectedClass.getHeight() / 2);
			coorX2 = e.getX();
			coorY2 = e.getY();
			if (classOn != null && assocToCrea) {
				if (classOnPrev != null) {
					classOnPrev.isSelected(false);
				}
				classOnPrev = classOn;
				classOn.isSelected(true);



			} else {
				if (classOnPrev != null) {
					classOnPrev.isSelected(false);
				}
			}

		}

	}

	/********************************************/
	/*********** Metodos de KeyListener *********/
	/********************************************/

	/*
	 * A este metodo se le llama siempre que el raton este sobre una clase
	 * creada y se escriba una tecla del teclado
	 */
	public void keyTyped(KeyEvent e) {
		sTyped = Character.compare('s', e.getKeyChar()) == 0;
		STyped = Character.compare('S', e.getKeyChar()) == 0;
		if (sTyped || STyped) {
			if (!preselectedClassBool && (classOn != null)) {
				preselectedClassBool = true;
				preselectedClass = classOn;
				preselectedClass.isPreselected(preselectedClassBool);
			} else if (preselectedClassBool) {
				preselectedClassBool = false;
				preselectedClass.isPreselected(preselectedClassBool);
				preselectedClass = null;
			}
		}
	}

	/*
	 * A este metodo se le llama siempre que el raton este sobre una clase
	 * creada y se presione una tecla del teclado
	 */
	public void keyPressed(KeyEvent e) {
	}

	/*
	 * A este metodo se le llama siempre que el raton este sobre una clase
	 * creada y se deje de presionar una tecla del teclado
	 */
	public void keyReleased(KeyEvent e) {
	}
}
