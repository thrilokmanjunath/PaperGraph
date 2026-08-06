package com.papermate.papermate.service;

import com.papermate.papermate.dto.PaperDto;
import com.papermate.papermate.model.Paper;
import com.papermate.papermate.repository.PaperRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaperService {

    private final PaperRepository paperRepository;

    public PaperService(PaperRepository paperRepository) {
        this.paperRepository = paperRepository;
    }

    public List<PaperDto> getAllPapers() {
        return paperRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PaperDto getPaperById(Long id) {
        return paperRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Paper not found with id: " + id));
    }

    public PaperDto createPaper(PaperDto paperDto) {
        Paper paper = new Paper(paperDto.getTitle(), paperDto.getAuthors(), paperDto.getYear(), paperDto.getDoi());
        Paper savedPaper = paperRepository.save(paper);
        return convertToDto(savedPaper);
    }

    private PaperDto convertToDto(Paper paper) {
        return new PaperDto(paper.getId(), paper.getTitle(), paper.getAuthors(), paper.getYear(), paper.getDoi());
    }
}
