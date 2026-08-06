PaperMate: Research Citation & Management System
PaperMate is a Java-based research management tool designed to help researchers organize academic papers, track citation relationships, and visualize the resulting network using dynamic graph representations.

Detailed below is the project report and documentation.

1. Project Overview
In the modern era of academic research, tracking the relationships between various publications is a complex task. PaperMate simplifies this by providing a unified interface to:

Store and manage paper metadata (Title, Authors, Year, DOI).
Establish citation links between research papers.
Visualize the research landscape using an interactive Graph UI.
2. System Architecture
The project follows a modular 3-tier architecture to ensure separation of concerns:

Model Layer: Defines the core entities such as model.Paper and model.Citation.
Manager (Logic) Layer: Handles business logic and database interactions via manager.PaperManager and manager.CitationManager.
Presentation Layer: A Swing-based GUI (ui.MainUI) and a visualization engine (graph.GraphVisualizer).
3. Database Schema
The system uses a MySQL backend for persistent storage.

papers: Stores id, title, authors, year, and doi.
citations: Stores the relationships between paper_id and cited_paper_id.
Connection configuration can be found in database.DatabaseConnection.

4. Key Features
📥 Paper Management
The manager.PaperManager class provides methods to add new papers to the database and retrieve the complete list of managed research items.

🕸️ Interactive Visualization
The graph.GraphVisualizer uses the GraphStream library to generate a dynamic map of your research.

Node Scaling: Nodes grow in size based on how many times they are cited.
Color Coding:
🟡 Gold: Most cited paper.
🔴 Red: Recent publications (2020+).
🟠 Orange: Mid-range publications (2018+).
🔵 Blue: Older legacy papers.
Interactivity: Clicking any node in the graph triggers a popup with the full paper details.
💻 User Interface
The ui.MainUI provides a clean, entry-point for the application, allowing users to input paper data and launch the visualization tool with a single click.

5. Technical Stack
Language: Java 11+
GUI: Java Swing
Database: MySQL (via mysql-connector-j-9.6.0.jar)
Graphing Engine: GraphStream 2.0 (Core, Algo, UI-Swing)
6. Setup and Installation
Prerequisites
MySQL Server running on localhost:3306.
A database named papermate.
Configuration
Update the credentials in DatabaseConnection.java if your local MySQL settings differ:
private static final String USER="root";
private static final String PASSWORD="root";

Running the Application
The main entry point is located in ui.MainUI. You can run it via your IDE or using the terminal:

javac -d bin -cp "lib/*" src/**/*.java
java -cp "bin:lib/*" ui.MainUI