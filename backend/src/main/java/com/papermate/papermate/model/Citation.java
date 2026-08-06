package com.papermate.papermate.model;

import jakarta.persistence.*;

@Entity
@Table(name = "citations")
public class Citation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citing_paper_id", nullable = false)
    private Paper citingPaper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cited_paper_id", nullable = false)
    private Paper citedPaper;

    public Citation() {}

    public Citation(Paper citingPaper, Paper citedPaper) {
        this.citingPaper = citingPaper;
        this.citedPaper = citedPaper;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paper getCitingPaper() { return citingPaper; }
    public void setCitingPaper(Paper citingPaper) { this.citingPaper = citingPaper; }

    public Paper getCitedPaper() { return citedPaper; }
    public void setCitedPaper(Paper citedPaper) { this.citedPaper = citedPaper; }
}
