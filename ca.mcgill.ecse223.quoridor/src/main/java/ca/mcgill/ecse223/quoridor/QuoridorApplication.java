
package ca.mcgill.ecse223.quoridor;

import ca.mcgill.ecse223.quoridor.view.QuoridorWindow;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

public class QuoridorApplication {

	private static Quoridor quoridor;
	public static QuoridorWindow quoridorWindow;

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
}