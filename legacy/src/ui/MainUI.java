package ui;

import javax.swing.*;
import java.awt.*;
import manager.PaperManager;
import manager.CitationManager;
import graph.GraphVisualizer;
import model.Paper;

import java.util.List;

public class MainUI extends JFrame {

    private JTextField titleField = new JTextField(20);
    private JTextField authorsField = new JTextField(20);
    private JTextField yearField = new JTextField(5);
    private JTextField doiField = new JTextField(20);

    private JTextField paperIdField = new JTextField(5);
    private JTextField citedPaperIdField = new JTextField(5);

    private JTextField searchIdField = new JTextField(5);
    private int loadedPaperId = -1; // tracks the paper loaded for update/delete

    public MainUI() {
        setTitle("PaperMate Research Manager");
        setLayout(new BorderLayout());

        // --- Paper input panel ---
        JPanel paperPanel = new JPanel(new FlowLayout());
        paperPanel.setBorder(BorderFactory.createTitledBorder("Add Paper"));

        paperPanel.add(new JLabel("Title:"));
        paperPanel.add(titleField);
        paperPanel.add(new JLabel("Authors:"));
        paperPanel.add(authorsField);
        paperPanel.add(new JLabel("Year:"));
        paperPanel.add(yearField);
        paperPanel.add(new JLabel("DOI:"));
        paperPanel.add(doiField);

        JButton addButton = new JButton("Add Paper");
        addButton.addActionListener(e -> addPaper());
        paperPanel.add(addButton);

        // --- Update/Delete panel ---
        JPanel udPanel = new JPanel(new FlowLayout());
        udPanel.setBorder(BorderFactory.createTitledBorder("Update / Delete Paper"));

        udPanel.add(new JLabel("Paper ID:"));
        udPanel.add(searchIdField);

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> loadPaper());
        udPanel.add(loadButton);

        JButton updateButton = new JButton("Update Paper");
        updateButton.addActionListener(e -> updatePaper());
        udPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Paper");
        deleteButton.addActionListener(e -> deletePaper());
        udPanel.add(deleteButton);

        // --- Citation input panel ---
        JPanel citationPanel = new JPanel(new FlowLayout());
        citationPanel.setBorder(BorderFactory.createTitledBorder("Add Citation"));

        citationPanel.add(new JLabel("Paper ID:"));
        citationPanel.add(paperIdField);
        citationPanel.add(new JLabel("Cited Paper ID:"));
        citationPanel.add(citedPaperIdField);

        JButton citeButton = new JButton("Add Citation");
        citeButton.addActionListener(e -> addCitation());
        citationPanel.add(citeButton);

        // --- Button panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton graphButton = new JButton("Show Citation Graph");
        graphButton.addActionListener(e -> showGraph());
        JButton listButton = new JButton("List All Papers");
        listButton.addActionListener(e -> listPapers());
        buttonPanel.add(graphButton);
        buttonPanel.add(listButton);

        // --- Layout ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(paperPanel);
        topPanel.add(udPanel);
        topPanel.add(citationPanel);

        add(topPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addPaper() {
        try {
            String t = titleField.getText().trim();
            String a = authorsField.getText().trim();
            String y = yearField.getText().trim();
            String d = doiField.getText().trim();

            if (t.isEmpty() || a.isEmpty() || y.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title, Authors and Year are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int yearVal = Integer.parseInt(y);
            Paper paper = new Paper(t, a, yearVal, d);
            new PaperManager().addPaper(paper);

            JOptionPane.showMessageDialog(this, "Paper added with ID: " + paper.getId());

            titleField.setText("");
            authorsField.setText("");
            yearField.setText("");
            doiField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void addCitation() {
        try {
            int pid = Integer.parseInt(paperIdField.getText().trim());
            int cid = Integer.parseInt(citedPaperIdField.getText().trim());

            if (pid == cid) {
                JOptionPane.showMessageDialog(this, "A paper cannot cite itself.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            new CitationManager().addCitation(pid, cid);
            JOptionPane.showMessageDialog(this, "Citation added: " + pid + " -> " + cid);

            paperIdField.setText("");
            citedPaperIdField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Paper IDs must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadPaper() {
        try {
            int id = Integer.parseInt(searchIdField.getText().trim());
            Paper p = new PaperManager().getPaperById(id);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "No paper found with ID: " + id, "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            loadedPaperId = p.getId();
            titleField.setText(p.getTitle());
            authorsField.setText(p.getAuthors());
            yearField.setText(String.valueOf(p.getYear()));
            doiField.setText(p.getDoi());
            JOptionPane.showMessageDialog(this, "Paper loaded. Edit fields above then click Update or Delete.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updatePaper() {
        try {
            if (loadedPaperId == -1) {
                JOptionPane.showMessageDialog(this, "Load a paper first using its ID.", "No Paper Loaded", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String t = titleField.getText().trim();
            String a = authorsField.getText().trim();
            String y = yearField.getText().trim();
            String d = doiField.getText().trim();

            if (t.isEmpty() || a.isEmpty() || y.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title, Authors and Year are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Paper paper = new Paper(t, a, Integer.parseInt(y), d);
            paper.setId(loadedPaperId);
            new PaperManager().updatePaper(paper);

            JOptionPane.showMessageDialog(this, "Paper ID " + loadedPaperId + " updated successfully.");
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deletePaper() {
        try {
            if (loadedPaperId == -1) {
                JOptionPane.showMessageDialog(this, "Load a paper first using its ID.", "No Paper Loaded", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete Paper ID " + loadedPaperId + "?\nThis will also remove its citations.",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            new PaperManager().deletePaper(loadedPaperId);
            JOptionPane.showMessageDialog(this, "Paper ID " + loadedPaperId + " deleted successfully.");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        titleField.setText("");
        authorsField.setText("");
        yearField.setText("");
        doiField.setText("");
        searchIdField.setText("");
        loadedPaperId = -1;
    }

    private void listPapers() {
        try {
            List<Paper> papers = new PaperManager().getAllPapers();
            StringBuilder sb = new StringBuilder();
            for (Paper p : papers) {
                sb.append("ID: ").append(p.getId()).append(" | ").append(p).append("\n");
            }
            if (sb.length() == 0) sb.append("No papers found.");
            JOptionPane.showMessageDialog(this, sb.toString(), "All Papers", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void showGraph() {
        try {
            new GraphVisualizer().showGraph();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Graph Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::new);
    }
}