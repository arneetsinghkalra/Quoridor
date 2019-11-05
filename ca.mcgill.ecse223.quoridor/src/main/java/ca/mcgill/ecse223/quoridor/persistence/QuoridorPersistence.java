package ca.mcgill.ecse223.quoridor.persistence;

import ca.mcgill.ecse223.quoridor.model.*;

public class QuoridorPersistence {
private static String filename = "data.quoridor";
	
	public static void save(Quoridor quoridor) {
		PersistenceObjectStream.serialize(quoridor);
	}
	
	public static Quoridor load() {
		PersistenceObjectStream.setFilename(filename);
		Quoridor quoridor = (Quoridor) PersistenceObjectStream.deserialize();
		// model cannot be loaded - create empty BTMS
		if (quoridor == null) {
			quoridor = new Quoridor();
		}
		else {
			quoridor.reinitialize();
		}
		return quoridor;
	}
	
	public static void setFilename(String newFilename) {
		filename = newFilename;
	}


}
