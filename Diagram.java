import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

import javax.swing.*;
import java.util.*;

public class Diagram extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	
	private Window window;// diagram's window
	public Class clase;

	private Vector<Class> classes = new Vector(); // it stores the classes
	private Vector<Association> associations = new Vector(); // it stores the associations

	// attributes which are used in keyTyped()
	private boolean preselectedClassBool = false; // true when a class is preselected
	
	private Class preselectedClass; // the class which is preselected

	// attributes which are used in addClass()
	private String newClassName;
	private int classCounter = 0;
	private int x = 50;
	private int y = 50;

	// attributes which are used in mouseOn()
	private Rectangle rec;
	private Class classOn;
	private Class classOnPrev = null;
	private int indexClassOn;
	private int xCoor;
	private int yCoor;
	private int wid;
	private int hei;

	// attributes which are used in keyTypped()
	private boolean sTyped;
	private boolean STyped;

	// attributes which are used in mouseDragged()
	private boolean assocToCrea = false;
	private boolean unionBool = false;

	// attributes which are used to clear associations
	private Vector<Association> assocToRemove = new Vector();

	Association newAssoc;
	private MouseEvent e;

	int coorX1;
	int coorY1;
	int coorX2;
	int coorY2;

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
		
		super.paint(g);
		// go through the association's vector, to draw associations
		for (int i = 0; i < associations.size(); i++) {
			associations.get(i).draw(g);
		}
		
		if (unionBool) {
			Graphics2D g2 = (Graphics2D) g;
			Line2D union = new Line2D.Double(coorX1, coorY1, coorX2, coorY2);
			g2.draw(union);
		}
		
		// go through the class's vector, to draw classes
		for (int i = 0; i < classes.size(); i++) {
			classes.get(i).draw(g);
		}
		repaint();
	}

	/*
	 * This method is called every time the mouse passes over a class 
	 * and this is not the last one in the array of classes.
	 */
	
	public void overlap() {
		// It goes through the vector of classes rearranging it so that the class
		// on which the mouse is located is the last one in the vector.
		for (int j = indexClassOn; j < classes.size() - 1; j++) {
			classes.set(j, classes.elementAt(j + 1));
		}
		classes.set(classes.size() - 1, classOn);
	}

	/********************************************/
	/********* MouseListener's methods **********/
	/********************************************/

	/* This method is called when a mouse button is pressed */
	public void mousePressed(MouseEvent e) {
		if ((classOn == preselectedClass) && preselectedClassBool) {
			assocToCrea = true;
			unionBool = true;
		} else {
			assocToCrea = false;
			unionBool = false;

		}
	}

	/* This method is called when a mouse button is released */
	public void mouseReleased(MouseEvent e) {
		unionBool = false;
		// to create an association:
		// 1. A class must be selected (preselectedClassBool = true)
		// 2. You must drag the mouse with the left click starting from the selected class (assocToCrea == true)
		// 3. The mouse must be released on top of an existing class (classOn != null)
		if (preselectedClassBool && (classOn != null) && assocToCrea) {
			// no more associations can be made until another class is selected
			assocToCrea = false;
			// class backgroud = white
			preselectedClass.isPreselected(false);
			classOn.isSelected(false);
			// association is created
			newAssoc = new Association(preselectedClass, classOn);
			associations.add(newAssoc);
			// we add the association to the implied classes
			preselectedClass.newAssociation(newAssoc);
			if (preselectedClass != classOn) {
				classOn.newAssociation(newAssoc);
			}
			// there is no class selected
			preselectedClass = null;
			preselectedClassBool = false;
		}
		// Refresh the association count
		window.updateNAssociations(this);

	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	/* This method is called when a mouse button is clicked */
	public void mouseClicked(MouseEvent e) {
		// If there isn't a class selected and the right button is clicked on a class, the class is removed
		if (!preselectedClassBool && (e.getButton() == MouseEvent.BUTTON3) && (classOn != null)) {
			// Remove associatons
			assocToRemove = classOn.getAssociations();
			for (int i = 0; i < assocToRemove.size(); i++) {
				associations.remove(assocToRemove.get(i));
			}
			// Remove class
			classes.remove(classOn);
			// Refresh counters
			window.updateNClasses(this);
			window.updateNAssociations(this);
		}
	}

	/********************************************/
	/******* MouseMotionListener's methods ******/
	/********************************************/

	/*
	 * This method is called when the mouse is moved
	 */
	
	public void mouseMoved(MouseEvent e) {
		for (int i = classes.size() - 1; i >= 0; i--) {
			
			xCoor = classes.get(i).getPosX(); // x axis
			yCoor = classes.get(i).getPosY(); // y axis
			
			wid = classes.get(i).getWidth(); // rectangle width
			hei = classes.get(i).getHeight(); // rectangle height

			rec = new Rectangle(xCoor, yCoor, wid, hei);
			
			// the mouse is over a class, we call that class classOn
			
			if (rec.contains(e.getPoint())) {
				classOn = classes.get(i);
				indexClassOn = i;
				//If the class the mouse is on is not drawn on top of the other classes, overlap() method is called
				if (i != classes.size() - 1) {
					overlap();
				}
				break;
			} else {
				// the mouse isn't over a class
				classOn = null;
			}
		}
	}

	/*
	 * this method is called when the mouse is dragged
	 */
	
	public void mouseDragged(MouseEvent e) {

		if (!preselectedClassBool && (classOn != null)) {			
			// if a class is selected, we disable other actions (preselectedClassBool == false). 
			// for a class to be dragged, the mouse must be on it (classOn != null)
			classOn.moveClass(e.getX(), e.getY());	
		} else if (preselectedClassBool) {
			mouseMoved(e);			
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
	/*********** KeyListener's methods **********/
	/********************************************/

	/*
	 * this method is called when a key is typed
	 */
	public void keyTyped(KeyEvent e) {
		sTyped = Character.compare('s', e.getKeyChar()) == 0;
		STyped = Character.compare('S', e.getKeyChar()) == 0;
		// s is typed
		if (sTyped || STyped) {
			// there is no class selected
			if (!preselectedClassBool && (classOn != null)) {
				// select class
				preselectedClassBool = true;
				// save class selected
				preselectedClass = classOn;
				preselectedClass.isPreselected(preselectedClassBool);
			//there is class selected
			} else if (preselectedClassBool) {
				// unselect the class
				preselectedClassBool = false;
				preselectedClass.isPreselected(preselectedClassBool);
				preselectedClass = null;
			}
		}
	}

	
	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}
}
