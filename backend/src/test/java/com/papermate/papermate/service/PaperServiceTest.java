package com.papermate.papermate.service;

import com.papermate.papermate.dto.PaperDto;
import com.papermate.papermate.model.Paper;
import com.papermate.papermate.repository.PaperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PaperServiceTest {

    @Mock
    private PaperRepository paperRepository;

    @InjectMocks
    private PaperService paperService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPaperById() {
        Paper paper = new Paper("Test Title", "Author", 2023, "10.1234/test");
        paper.setId(1L);
        when(paperRepository.findById(1L)).thenReturn(Optional.of(paper));

        PaperDto result = paperService.getPaperById(1L);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
    }

    @Test
    public void testCreatePaper() {
        PaperDto dto = new PaperDto(null, "New Paper", "Author", 2024, "10.1234/new");
        Paper paper = new Paper("New Paper", "Author", 2024, "10.1234/new");
        paper.setId(2L);
        
        when(paperRepository.save(any(Paper.class))).thenReturn(paper);

        PaperDto result = paperService.createPaper(dto);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("New Paper", result.getTitle());
    }
}
