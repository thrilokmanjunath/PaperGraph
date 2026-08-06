package model;

public class Paper {

    private int id;
    private String title;
    private String authors;
    private int year;
    private String doi;

    public Paper() {}

    public Paper(String title, String authors, int year, String doi) {
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.doi = doi;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getDoi() { return doi; }
    public void setDoi(String doi) { this.doi = doi; }

    @Override
    public String toString() {
        return title + " by " + authors + " (" + year + ")";
    }
}