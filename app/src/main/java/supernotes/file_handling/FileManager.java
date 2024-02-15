package supernotes.file_handling;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import supernotes.management.DBManager;
import supernotes.management.SQLiteDBManager;
import supernotes.notes.ImageNote;
import supernotes.notes.Note;
import supernotes.notes.TextNote;

import java.util.List;
import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileManager implements FileHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);

    public void exportPdfFile(String filePath, String tag) {

        try {
            List<Note> result = null;

            DBManager dbManager = new SQLiteDBManager();
            if (tag == null || tag.isEmpty()) {
                result = dbManager.getAllNotes();
            } else {
                result = dbManager.getAllNotesByTag(tag);
            }

            exportPdf(filePath, result);
        } catch (Exception e) {
            LOGGER.error("Une erreur s'est produite.", e);
        }
    }

    public void exportPdfFileUsingFilter(String filePath, String filter) {
        try {
            List<Note> result = null;

            DBManager dbManager = new SQLiteDBManager();
            if (filter == null || filter.isEmpty()) {
                result = dbManager.getAllNotes();
            } else {
                result = dbManager.getAllNotesLike(filter);
            }

            exportPdf(filePath, result);
        } catch (Exception e) {
            LOGGER.error("Une erreur s'est produite.", e);
        }
    }

    private void exportPdf(String filePath, List<Note> result) {
        try (PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf)) {

            var notesIterator = result.iterator();
            while (notesIterator.hasNext()) {
                var currentNote = notesIterator.next();
                if (currentNote instanceof TextNote) {
                    String textContent = ((TextNote) currentNote).getContent();
                    if (textContent != null) {
                        document.add(new Paragraph(textContent));
                    }
                } else if (currentNote instanceof ImageNote) {
                    byte[] imageBytes = ((ImageNote) currentNote).getContent();
                    if (imageBytes != null) {
                        Image image = new Image(ImageDataFactory.create(imageBytes));
                        document.add(image);
                    }
                }

                if (notesIterator.hasNext()) {
                    document.add(new Paragraph("\n"));
                }
            }

            System.out.println("Exportation terminée : " + filePath);

            boolean isUploaded = DriveApiManager.uploadFile(filePath);

            if (isUploaded) {
                System.out.println("Fichier téléchargé sur Google Drive");
            } else {
                System.out.println("Erreur réseau");
            }
        } catch (Exception e) {
            LOGGER.error("Une erreur s'est produite.", e);
        }
    }

    public void exportToText(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            DBManager dbManager = new SQLiteDBManager();
            List<Note> notes = dbManager.getAllNotes();
            for (Note note : notes) {
                writer.write("Tag: " + note.getTag() + "\n");
                writer.write("Content: " + note.getContent() + "\n\n");
            }
            System.out.println("Exportation des notes en texte brut terminée : " + filePath);
        } catch (IOException e) {
            System.out.println("Erreur lors de l'exportation en texte brut : " + e.getMessage());
        }
    }

}
