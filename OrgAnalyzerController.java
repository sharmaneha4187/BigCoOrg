package com.bigco.org.neha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bigco.org.neha.OrgAnalysisReport;
import com.bigco.org.neha.service.OrgAnalysisService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/orgs")
@Tag(name = "Org Analysis")
public class OrgAnalyzerController {

    private final com.bigco.org.neha.service.OrgAnalysisService service;

    public OrgAnalyzerController(OrgAnalysisService service) {
        this.service = service;
    }

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Analyze organization CSV",
               description = "Upload a CSV with header id,firstname,lastname,salary,managerId and get salary violations and long chains.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = OrgAnalysisReport.class))),
                   @ApiResponse(responseCode = "400", description = "Bad Request")
               })
    public ResponseEntity<OrgAnalysisReport> analyze(@RequestPart("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String suffix = StringUtils.hasText(file.getOriginalFilename()) ? "-" + file.getOriginalFilename() : ".csv";
        Path temp = Files.createTempFile("org-", suffix);
        file.transferTo(temp);
        try {
            return ResponseEntity.ok(service.analyze(temp));
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}
