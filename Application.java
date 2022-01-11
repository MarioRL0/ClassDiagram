import javax.swing.*;
import java.awt.*;


public class Application {

	// methods
	private static void createAndShowGUI() {			
		// JFrame creation
		JFrame.setDefaultLookAndFeelDecorated(true); 
		JFrame frame = new JFrame("Practica 5");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Window and components creation	
		Window app = new Window();
		Component contents = app.createComponents();
		// add on JFrame
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		frame.pack();
		
		// JFrame attributes established
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {createAndShowGUI();}
		});
	}
}
