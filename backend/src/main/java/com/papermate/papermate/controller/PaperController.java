package com.papermate.papermate.controller;

import com.papermate.papermate.dto.PaperDto;
import com.papermate.papermate.service.PaperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/papers")
@CrossOrigin(origins = "*") // Allow frontend access
public class PaperController {

    private final PaperService paperService;

    public PaperController(PaperService paperService) {
        this.paperService = paperService;
    }

    @GetMapping
    public ResponseEntity<List<PaperDto>> getAllPapers() {
        return ResponseEntity.ok(paperService.getAllPapers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaperDto> getPaperById(@PathVariable Long id) {
        return ResponseEntity.ok(paperService.getPaperById(id));
    }

    @PostMapping
    public ResponseEntity<PaperDto> createPaper(@RequestBody PaperDto paperDto) {
        return ResponseEntity.ok(paperService.createPaper(paperDto));
    }
}
