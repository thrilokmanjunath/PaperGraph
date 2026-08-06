package com.papermate.papermate.repository;

import com.papermate.papermate.model.Citation;
import com.papermate.papermate.model.Paper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitationRepository extends JpaRepository<Citation, Long> {
    List<Citation> findByCitingPaper(Paper citingPaper);
    List<Citation> findByCitedPaper(Paper citedPaper);
}
