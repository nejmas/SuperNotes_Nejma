package supernotes.fileHandling;

import java.io.*;

import supernotes.fileHandling.FileHandler;
import supernotes.management.DBManager;
import supernotes.management.SQLiteDBManager;
import supernotes.notes.ImageNote;
import supernotes.notes.Note;
import supernotes.notes.TextNote;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.util.List;

public class FileManager implements FileHandler {
    public void exportPdfFile(String filePath, String tag)
    {

        try {
            List<Note> result = null;

            DBManager dbManager = new SQLiteDBManager();
            if(tag == null || tag.isEmpty())
            {
                result = dbManager.getAllNotes();
            }
            else
            {
                result = dbManager.getAllNotesByTag(tag);
            }

            exportPdf(filePath, result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportPdfFileUsingFilter(String filePath, String filter)
    {
        try {
            List<Note> result = null;

            DBManager dbManager = new SQLiteDBManager();
            if(filter == null || filter.isEmpty())
            {
                result = dbManager.getAllNotes();
            }
            else
            {
                result = dbManager.getAllNotesLike(filter);
            }

            exportPdf(filePath, result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportPdf(String filePath, List<Note> result)
    {
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            var notesIterator = result.iterator();
            while (notesIterator.hasNext()) {
                var currentNote = notesIterator.next();
                if (currentNote instanceof TextNote) {
                    document.add(new Paragraph(((TextNote) currentNote).getContent()));
                } else if (currentNote instanceof ImageNote) {
                    var imageBytes = ((ImageNote) currentNote).getContent();
                    Image image = new Image(ImageDataFactory.create(imageBytes));

                    document.add(image);
                }
                document.add(new Paragraph("\n"));
            }

            document.close();
            pdf.close();

            System.out.println("Exportation terminée : " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
