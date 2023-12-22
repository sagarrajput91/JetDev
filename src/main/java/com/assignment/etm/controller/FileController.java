package com.assignment.etm.controller;

import com.assignment.etm.bean.FileDownloadResponse;
import com.assignment.etm.bean.FileUploadResponse;
import com.assignment.etm.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "File Management API")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "Upload a file", description = "Upload an Excel file to the system")
    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file,
                                                         @RequestParam(required = false, defaultValue = "true") boolean isHeaderPresent) {
        FileUploadResponse fileUploadResponse = fileService.uploadFile(file, isHeaderPresent);
        return ResponseEntity.ok(fileUploadResponse);
    }

    @Operation(summary = "Check upload status", description = "Check the progress/status of file upload")
    @GetMapping("/upload/status/{fileId}")
    public ResponseEntity<String> checkUploadStatus(@PathVariable("fileId") String fileId) {
        String status = fileService.checkUploadStatus(fileId);
        return ResponseEntity.ok(status);
    }

    @Operation(summary = "List uploaded files", description = "List all files uploaded to the system")
    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        List<String> files = fileService.listFiles();
        return ResponseEntity.ok(files);
    }

    @Operation(summary = "Review file records", description = "Retrieve records of a specific file")
    @GetMapping("/files/{fileId}/records")
    public ResponseEntity<FileDownloadResponse> reviewFileRecords(@PathVariable("fileId") String fileId) {
        FileDownloadResponse fileDownloadResponse = fileService.reviewFileRecords(fileId);
        return ResponseEntity.ok(fileDownloadResponse);
    }

    @Operation(summary = "Delete a file", description = "Delete a specific file and its associated records")
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileId") String fileId) {
        String status = fileService.deleteFile(fileId);
        return ResponseEntity.ok(status);
    }
}
