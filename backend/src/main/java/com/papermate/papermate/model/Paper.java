package com.papermate.papermate.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "papers")
public class Paper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String authors;

    @Column(name = "published_year")
    private Integer year;

    @Column(unique = true)
    private String doi;

    @OneToMany(mappedBy = "citingPaper", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Citation> outgoingCitations = new ArrayList<>();

    @OneToMany(mappedBy = "citedPaper", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Citation> incomingCitations = new ArrayList<>();

    public Paper() {}

    public Paper(String title, String authors, Integer year, String doi) {
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

    public List<Citation> getOutgoingCitations() { return outgoingCitations; }
    public void setOutgoingCitations(List<Citation> outgoingCitations) { this.outgoingCitations = outgoingCitations; }

    public List<Citation> getIncomingCitations() { return incomingCitations; }
    public void setIncomingCitations(List<Citation> incomingCitations) { this.incomingCitations = incomingCitations; }
}
