package manager;

import model.Citation;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitationManager {

    public void addCitation(int paperId, int citedPaperId) throws SQLException {
        String sql = "INSERT INTO citations(paper_id, cited_paper_id) VALUES(?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paperId);
            ps.setInt(2, citedPaperId);
            ps.executeUpdate();
        }
    }

    public List<Citation> getAllCitations() throws SQLException {
        List<Citation> citations = new ArrayList<>();
        String sql = "SELECT * FROM citations";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Citation c = new Citation(rs.getInt("paper_id"), rs.getInt("cited_paper_id"));
                citations.add(c);
            }
        }
        return citations;
    }
}