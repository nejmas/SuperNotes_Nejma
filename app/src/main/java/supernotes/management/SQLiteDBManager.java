package supernotes.management;

import supernotes.notes.ImageNote;
import supernotes.notes.Note;
import supernotes.notes.TextNote;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class SQLiteDBManager implements DBManager {
    private Connection connection;

    public SQLiteDBManager()
    {
        connect();
    }

    @Override
    public void createNotesTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT NOT NULL," + // Champ pour indiquer le type de note (texte ou image)
                "content TEXT," + // Champs pour stocker le contenu texte
                "image BLOB," + // Champs pour stocker l'image en tant que BLOB
                "tag TEXT" + // Champs pour stocker le tag texte
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTextNote(String content, String tag)
    {
        String sql = "INSERT INTO notes (type, content, tag) VALUES (?, ?, ?)";
        try (var conn = this.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "text");
            pstmt.setString(2, content);            
            pstmt.setString(3, tag);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addImageNote(byte[] imageBytes, String tag)
    {
        String sql = "INSERT INTO notes (type, image, tag) VALUES (?, ?, ?)";
        try (var conn = this.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, "image");
            pstmt.setBytes(2, imageBytes);
            pstmt.setString(3, tag);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                            rs.getString("content"),
                            rs.getString("tag")));
                }
                else if (type.equals("image")){
                    result.add(new ImageNote(
                            rs.getBytes("image"),
                            rs.getString("tag")));
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
                    if (type.equals("text")){
                        result.add(new TextNote(
                                rs.getString("content"),
                                rs.getString("tag")));
                    }
                    else if (type.equals("image")){
                        result.add(new ImageNote(
                                rs.getBytes("image"),
                                rs.getString("tag")));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public ArrayList<Note> getAllNotesLike(String contentMotif)
    {
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
                                rs.getString("content"),
                                rs.getString("tag")));
                    }
                    else if (type.equals("image")){
                        result.add(new ImageNote(
                                rs.getBytes("image"),
                                rs.getString("tag")));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    private void connect()
    {
        try {
            // Charger le pilote JDBC SQLite
            Class.forName("org.sqlite.JDBC");
            // Créer une connexion à la base de données
            connection = DriverManager.getConnection("jdbc:sqlite:notes.db");
            createNotesTable();
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
