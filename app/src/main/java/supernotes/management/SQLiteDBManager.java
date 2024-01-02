package supernotes.management;

import supernotes.notes.ImageNote;
import supernotes.notes.Note;
import supernotes.notes.TextNote;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDBManager implements DBManager {
    private Connection connection;

    public SQLiteDBManager()
    {
        connect();
    }

    @Override
    public void createNotesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "type TEXT NOT NULL," + // Champ pour indiquer le type de note (texte ou image)
                "content TEXT," + // Champs pour stocker le contenu texte
                "image BLOB," + // Champs pour stocker l'image en tant que BLOB
                "tag TEXT," + // Champs pour stocker le tag texte (ajout de la virgule ici)
                "parent_page_id TEXT," +
                "page_id TEXT," + // Champs pour stocker l'ID de la page
                "time TEXT" +
                ")";
    
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public void createRemindersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Reminders ("
            + "id INT AUTO_INCREMENT PRIMARY KEY,"
            + "note_id INT,"
            + "reminder_date_time DATETIME,"
            + "FOREIGN KEY (note_id) REFERENCES Notes(id)"
            + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addReminder(int noteId, LocalDateTime reminderDateTime) {
    String sql = "INSERT INTO Reminders (note_id, reminder_date_time) VALUES (?, ?)";
        try (var conn = this.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, noteId);
            pstmt.setTimestamp(2, Timestamp.valueOf(reminderDateTime));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LocalDateTime> getReminders(int noteId) {
        List<LocalDateTime> reminders = new ArrayList<>();
    
        String sql = "SELECT reminder_date_time FROM Reminders WHERE note_id = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, noteId);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                Timestamp reminderTimestamp = rs.getTimestamp("reminder_date_time");
                LocalDateTime reminderDateTime = reminderTimestamp.toLocalDateTime();
                reminders.add(reminderDateTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return reminders;
    }

    @Override
    public boolean deleteRemindersByNoteId(int noteId) {
        String sql = "DELETE FROM reminders WHERE note_id = ?";
        boolean success = false;
    
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, noteId);
    
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tous les rappels pour la note avec l'ID " + noteId + " ont été supprimés avec succès !");
                success = true;
            } else {
                System.out.println("Aucun rappel trouvé pour la note avec l'ID " + noteId + ".");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des rappels pour la note avec l'ID " + noteId + " : " + e.getMessage());
        }
    
        return success;
    }
    
    
    @Override
    public int addTextNote(String title, String content, String tag, String parent_page_id, String page_id, String time)
    {
        String sql = "INSERT INTO notes (title, type, content, tag, parent_page_id, page_id, time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int noteId = -1;
        try (var conn = this.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, "text");
            pstmt.setString(3, content);            
            pstmt.setString(4, tag);
            pstmt.setString(5, parent_page_id);
            pstmt.setString(6, page_id);
            pstmt.setString(7,time);
            pstmt.executeUpdate();


            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    noteId = generatedKeys.getInt(1); // Récupérer l'ID généré
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteId;
    }

    public int addImageNote(String title, byte[] imageBytes, String tag, String parent_page_id, String page_id, String time)
    {
        String sql = "INSERT INTO notes (title, type, content, tag, parent_page_id, page_id, time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int noteId = -1;
        try (var conn = this.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, title);
            pstmt.setString(2, "image");
            pstmt.setBytes(3, imageBytes);
            pstmt.setString(4, tag);
            pstmt.setString(5, parent_page_id);
            pstmt.setString(6, page_id);
            pstmt.setString(7,time);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    noteId = generatedKeys.getInt(1); // Récupérer l'ID généré
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteId;
    }

    @Override
    public void deleteNoteByTag(String tag)
    {
        String sql = "DELETE FROM notes WHERE tag = ?";
        try (var conn = this.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tag);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Note> getAllNotes()
    {
        String sql = "SELECT * FROM notes";

        var result = new ArrayList<Note>();

        try (var conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                var type = rs.getString("type");
                if (type.equals("text")){
                    result.add(new TextNote(
                            rs.getString("title"),
                            rs.getString("type"),
                            rs.getString("content"),
                            rs.getString("tag"),
                            rs.getString("parent_page_id"),
                            rs.getString("page_id"),
                            rs.getString("time")));
                }
                else if (type.equals("image")){
                    result.add(new ImageNote(
                            rs.getString("title"),
                            rs.getString("type"),
                            rs.getString("content"),
                            rs.getString("tag"),
                            rs.getString("parent_page_id"),
                            rs.getString("page_id"),
                            rs.getString("time")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public ArrayList<Note> getAllNotesByTag(String tag)
    {
        String sql = "SELECT * FROM notes WHERE tag = ?";

        var result = new ArrayList<Note>();

        try (var conn = this.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, tag);

            try(ResultSet rs = stmt.executeQuery())
            {
                // loop through the result set
                while (rs.next()) {
                    var type = rs.getString("type");
                    int noteId = rs.getInt("id");

                                        if (type.equals("text")) {
                        TextNote textNote = new TextNote(
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("tag"),
                            rs.getString("parent_page_id"),
                            rs.getString("page_id"),
                            rs.getString("time")
                        );
                        textNote.setId(noteId);
                        result.add(textNote);
                    } else if (type.equals("image")) {
                        ImageNote imageNote = new ImageNote(
                            rs.getString("title"),
                            rs.getBytes("image"),
                            rs.getString("tag"),
                            rs.getString("parent_page_id"),
                            rs.getString("page_id"),
                            rs.getString("time")
                        );
                        imageNote.setId(noteId);
                        result.add(imageNote);
                    }
            }
        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public ArrayList<Note> getAllNotesLike(String contentMotif) {
        String sql = "SELECT * FROM notes WHERE content LIKE ?";

        var result = new ArrayList<Note>();

        try (var conn = this.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, "%"+contentMotif+"%");

            try(ResultSet rs = stmt.executeQuery())
            {
                // loop through the result set
                while (rs.next()) {
                    var type = rs.getString("type");
                    if (type.equals("text")){
                        result.add(new TextNote(
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("tag"),
                            rs.getString("parent_page_id"),
                            rs.getString("page_id"),
                                rs.getString("time")));
                    }
                    else if (type.equals("image")){
                        result.add(new ImageNote(
                                rs.getString("title"),
                                rs.getBytes("image"),
                                rs.getString("tag"),
                                rs.getString("parent_page_id"),
                                rs.getString("page_id"),
                                rs.getString("time")));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public String getParentPageId() {
        String parentPageId = null;
        String sql = "SELECT parent_page_id FROM notes WHERE parent_page_id IS NOT NULL LIMIT 1";
    
        try (var conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                parentPageId = rs.getString("parent_page_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return parentPageId;
    }

    @Override
    public String getPageId(String content) {
        String sql = "SELECT page_id FROM notes WHERE content = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, content);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("page_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null; 
    }

    public void updateNoteContentInDB(String pageId, String newContent) {
        String sql = "UPDATE notes SET content = ? WHERE page_id = ?";
        try(Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, newContent);
            pstmt.setString(2, pageId);
            pstmt.executeUpdate();
            System.out.println("Contenu de la note mis à jour !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean doesNoteExist(String pageId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM notes WHERE page_id = ?")) {
            
            preparedStatement.setString(1, pageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Si le compte est supérieur à zéro, la note existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    


    private void connect()
    {
        try {
            // Charger le pilote JDBC SQLite
            Class.forName("org.sqlite.JDBC");
            // Créer une connexion à la base de données
            connection = DriverManager.getConnection("jdbc:sqlite:notes.db");
            createNotesTable();
            createRemindersTable();
            System.out.println("Connexion à SQLite établie.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection()
    {
        try
        {
            if(connection == null || connection.isClosed())
            {
                connection  = DriverManager.getConnection("jdbc:sqlite:notes.db");
            }
        }
        catch (SQLException e) {
            System.out.println("Get Connexion à SQLite failed.");
        }

        return connection;
    }
}
