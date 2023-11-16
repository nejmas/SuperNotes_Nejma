package supernotes.notes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NoteManager {
	

    public NoteManager() {
    	//TODO
    }
    
    public static void addNote(String title, String description) {
        try {
            // Create file with title
            String fileName = title.replaceAll("\\s+", "_") + ".txt";
            FileWriter writer = new FileWriter(fileName);

            // Write title and description 
            writer.write("Titre : " + title + "\n\n");
            writer.write("Description :\n" + description);

            writer.close();
            
            //Get file Path
            String filePath = new File(fileName).getAbsolutePath();
            System.out.println("La note a été ajoutée avec succès : " + filePath);
            
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ajout de la note : " + e.getMessage());
        }    }

    public void displayNotes() {
    	//TODO
    }

    public void editNote() {
    	//TODO
    }

    public void deleteNote() {
    	//TODO
    }
    
    public void findNote() {
    	//TODO
    }

}
