package com.papermate.papermate.dto;

public class PaperDto {
    private Long id;
    private String title;
    private String authors;
    private Integer year;
    private String doi;

    public PaperDto() {}

    public PaperDto(Long id, String title, String authors, Integer year, String doi) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.doi = doi;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getDoi() { return doi; }
    public void setDoi(String doi) { this.doi = doi; }
}
