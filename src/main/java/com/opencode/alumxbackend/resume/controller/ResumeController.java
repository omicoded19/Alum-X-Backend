package com.opencode.alumxbackend.resume.controller;

import com.opencode.alumxbackend.resume.model.Resume;
import com.opencode.alumxbackend.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<?> uploadResume(
            @RequestParam String userId,
            @RequestParam MultipartFile file
    ) throws Exception {
        resumeService.uploadResume(userId, file);
        return ResponseEntity.ok("Resume uploaded successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> fetchResume(@PathVariable String userId) throws Exception {

        Resume resume = resumeService.getResumeByUserId(userId);
        Path path = Path.of(resume.getFilePath());

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resume.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(resume.getFileType()))
                .body(resource);
    }
}
