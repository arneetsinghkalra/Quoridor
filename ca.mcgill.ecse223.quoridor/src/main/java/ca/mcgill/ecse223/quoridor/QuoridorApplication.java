package ca.mcgill.ecse223.quoridor;

import ca.mcgill.ecse223.quoridor.view.QuoridorWindow;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.persistence.QuoridorPersistence;

public class QuoridorApplication {

	private static Quoridor quoridor;
	/** @author Luke Barber */
	public static QuoridorWindow quoridorWindow = new QuoridorWindow();
	public static Quoridor getQuoridor() {
		if (quoridor == null) {
			quoridor = new Quoridor();
		}
 		return quoridor;
	}

	// TODO add main method here
	public static void main(String[] args) {
	java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            quoridorWindow = new QuoridorWindow();
            quoridorWindow.setVisible(true);
        }
    });
	}
	
	public static Quoridor getQuordior() {
		if (quoridor == null) {
			// load model
			quoridor = QuoridorPersistence.load();
		}
 		return quoridor;
	}
}
