package manager;

import model.Paper;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaperManager {

    public void addPaper(Paper paper) throws SQLException {
        String sql = "INSERT INTO papers(title, authors, year, doi) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, paper.getTitle());
            ps.setString(2, paper.getAuthors());
            ps.setInt(3, paper.getYear());
            ps.setString(4, paper.getDoi());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    paper.setId(keys.getInt(1));
                }
            }
        }
    }

    public List<Paper> getAllPapers() throws SQLException {
        List<Paper> papers = new ArrayList<>();
        String sql = "SELECT * FROM papers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Paper p = new Paper();
                p.setId(rs.getInt("id"));
                p.setTitle(rs.getString("title"));
                p.setAuthors(rs.getString("authors"));
                p.setYear(rs.getInt("year"));
                p.setDoi(rs.getString("doi"));
                papers.add(p);
            }
        }
        return papers;
    }

    public Paper getPaperById(int id) throws SQLException {
        String sql = "SELECT * FROM papers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Paper p = new Paper();
                    p.setId(rs.getInt("id"));
                    p.setTitle(rs.getString("title"));
                    p.setAuthors(rs.getString("authors"));
                    p.setYear(rs.getInt("year"));
                    p.setDoi(rs.getString("doi"));
                    return p;
                }
            }
        }
        return null;
    }

    public void updatePaper(Paper paper) throws SQLException {
        String sql = "UPDATE papers SET title = ?, authors = ?, year = ?, doi = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paper.getTitle());
            ps.setString(2, paper.getAuthors());
            ps.setInt(3, paper.getYear());
            ps.setString(4, paper.getDoi());
            ps.setInt(5, paper.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Paper with ID " + paper.getId() + " not found.");
            }
        }
    }

    public void deletePaper(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Delete related citations first
            String delCitations = "DELETE FROM citations WHERE paper_id = ? OR cited_paper_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(delCitations)) {
                ps.setInt(1, id);
                ps.setInt(2, id);
                ps.executeUpdate();
            }
            // Delete the paper
            String delPaper = "DELETE FROM papers WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(delPaper)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("Paper with ID " + id + " not found.");
                }
            }
        }
    }
}