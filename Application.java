import javax.swing.*;
import java.awt.*;


public class Application {

	//metodos
	private static void createAndShowGUI() {			
		//creamos el JFrame
		JFrame.setDefaultLookAndFeelDecorated(true); 
		JFrame frame = new JFrame("Practica 5");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//creamos la ventana y sus componentes y los anyadimos al JFrame	
		Window app = new Window();
		Component contents = app.createComponents();
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		frame.pack();
		
		//establecemos los atributos del JFrame
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {createAndShowGUI();}
		});
	}
}
