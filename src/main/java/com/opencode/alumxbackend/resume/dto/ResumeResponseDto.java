package com.opencode.alumxbackend.resume.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeResponseDto {
    private String id;
    private String userId;
    private String fileName;
    private String fileType;
}
