package com.assignment.etm.service;

import com.assignment.etm.bean.FileDownloadResponse;
import com.assignment.etm.bean.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    FileUploadResponse uploadFile(MultipartFile file, boolean isHeaderPresent);

    String checkUploadStatus(String fileId);

    List<String> listFiles();

    FileDownloadResponse reviewFileRecords(String fileId);

    String deleteFile(String fileId);
}

