package graph;

import manager.CitationManager;
import manager.PaperManager;
import model.Citation;
import model.Paper;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;
import org.graphstream.ui.view.ViewerListener;

import javax.swing.JOptionPane;
import java.util.*;

public class GraphVisualizer {

    // Move stylesheet to a constant for readability
    private static final String STYLE_SHEET = 
        "node { text-size: 14px; text-color: black; stroke-mode: plain; stroke-color: #333; }" +
        "node.mostCited { fill-color: gold; stroke-color: orange; stroke-width: 2px; }" +
        "node.recent { fill-color: red; }" +
        "node.mid { fill-color: orange; }" +
        "node.old { fill-color: blue; }" +
        "edge { fill-color: gray; arrow-size: 10px; }";

    public void showGraph() {
        try {
            System.setProperty("org.graphstream.ui", "swing");
            Graph graph = new SingleGraph("Citation Network");
            graph.setAttribute("ui.quality");
            graph.setAttribute("ui.antialias");
            graph.setAttribute("ui.stylesheet", STYLE_SHEET);

            PaperManager manager = new PaperManager();
            CitationManager cm = new CitationManager();

            List<Paper> papers = manager.getAllPapers();
            List<Citation> citations = cm.getAllCitations();
            if (papers.isEmpty()) return;

            Map<String, Integer> citationCount = new HashMap<>();
            for (Paper p : papers) {
                String id = String.valueOf(p.getId());
                citationCount.put(id, 0);

                Node node = graph.addNode(id);
                node.setAttribute("ui.label", p.getTitle());
                // Store the whole object or specific attributes
                node.setAttribute("data", p); 
            }

            // Create edges and track counts
            int edgeIndex = 0;
            for (Citation c : citations) {
                String fromId = String.valueOf(c.getPaperId());
                String toId = String.valueOf(c.getCitedPaperId());

                // Only add edge if both nodes exist
                if (graph.getNode(fromId) != null && graph.getNode(toId) != null) {
                    String edgeId = "e" + (edgeIndex++);
                    graph.addEdge(edgeId, fromId, toId, true); // true = directed
                    citationCount.put(toId, citationCount.get(toId) + 1);
                }
            }

            // Find max citations for scaling
            String mostCitedId = Collections.max(citationCount.entrySet(), Map.Entry.comparingByValue()).getKey();

            // Apply Classes instead of raw strings for optimization
            for (Node node : graph) {
                Paper p = (Paper) node.getAttribute("data");
                int count = citationCount.get(node.getId());
                int year = p.getYear();

                // Set Size based on citations
                node.setAttribute("ui.style", "size: " + (20 + count * 10) + "px;");

                // Set Class based on logic
                if (node.getId().equals(mostCitedId)) {
                    node.setAttribute("ui.class", "mostCited");
                } else if (year >= 2020) {
                    node.setAttribute("ui.class", "recent");
                } else if (year >= 2018) {
                    node.setAttribute("ui.class", "mid");
                } else {
                    node.setAttribute("ui.class", "old");
                }
            }

            Viewer viewer = graph.display();
            viewer.enableAutoLayout();
            ViewerPipe pipe = viewer.newViewerPipe();
            pipe.addAttributeSink(graph);

            // Use an adapter or safely implement the listener
            pipe.addViewerListener(new ViewerListener() {
                @Override public void viewClosed(String viewName) {}
                @Override public void buttonReleased(String id) {}
                @Override public void mouseLeft(String id) {}
                @Override public void mouseOver(String id) {}

                @Override
                public void buttonPushed(String id) {
                    Node n = graph.getNode(id);
                    Paper p = (Paper) n.getAttribute("data");
                    
                    JOptionPane.showMessageDialog(null, 
                        "Title: " + p.getTitle() + "\nYear: " + p.getYear() + "\nDOI: " + p.getDoi(),
                        "Paper Details", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            // OPTIMIZATION: Run the pump in a background thread to prevent UI freezing
            new Thread(() -> {
                while (true) {
                    pipe.pump();
                    try {
                        Thread.sleep(50); // Significant CPU saving
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}