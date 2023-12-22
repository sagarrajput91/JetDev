package com.assignment.etm.service.impl;

import com.assignment.etm.bean.FileDownloadResponse;
import com.assignment.etm.bean.FileUploadResponse;
import com.assignment.etm.entity.FileEntity;
import com.assignment.etm.enums.Status;
import com.assignment.etm.repository.FileRepository;
import com.assignment.etm.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, boolean isHeaderPresent) {
        log.info("Uploading file");
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        FileEntity fileEntity = new FileEntity();
        if (file != null) {
            try {
                fileEntity.setFile(new javax.sql.rowset.serial.SerialBlob(file.getBytes()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setIsHeaderPresent(isHeaderPresent);
        FileEntity entity = fileRepository.save(fileEntity);
        BeanUtils.copyProperties(entity, fileUploadResponse);
        log.info("File Uploaded successfully");
        return fileUploadResponse;
    }

    @Override
    public String checkUploadStatus(String fileId) {
        String response;
        Optional<FileEntity> dataSourceFilesEntityOptional = fileRepository.findById(fileId);
        if(dataSourceFilesEntityOptional.isPresent()){
            response = "File upload successfully. Status : "+ Status.COMPLETED.name();
        }else{
           response = "File upload failed. Status: " + Status.FAILED;
        }
        log.info("Status : {}", response);
        return response;
    }

    @Override
    public List<String> listFiles() {
        log.info("Listing files");
        List<FileEntity> files = fileRepository.findAll();
        return files.stream().map(FileEntity::getFileName).collect(Collectors.toList());
    }

    @Override
    public FileDownloadResponse reviewFileRecords(String fileId) {
        log.info("Reviewing file records");
        try {
            FileDownloadResponse response = new FileDownloadResponse();
            FileEntity filesEntity = this.getFile(fileId);
            if (filesEntity.getFile() != null) {
                Blob fileBlob = filesEntity.getFile();
                int blobLength = (int) fileBlob.length();
                byte[] fileBytes = fileBlob.getBytes(1, blobLength);
                fileBlob.free();
                response.setFileContent(fileBytes);
            }
            response.setFileName(filesEntity.getFileName());
            response.setIsHeaderPresent(filesEntity.getIsHeaderPresent());
            return response;
        } catch (SQLException exception) {
            log.error("Exception occurred getFileContent",exception);
        }
        return null;
    }

    @Override
    public String deleteFile(String fileId) {
        log.info("Deleting file");
        FileEntity fileEntity = getFile(fileId);
        fileRepository.delete(fileEntity);
        return "File deleted successfully";
    }

    public FileEntity getFile(String fileId) {
        Optional<FileEntity> optionalDataSourceFilesEntity = fileRepository.findById(fileId);
        if (optionalDataSourceFilesEntity.isPresent()) {
            FileEntity fileEntity = optionalDataSourceFilesEntity.get();
            log.info("File : {}", fileEntity);
            return fileEntity;
        }
        throw new RuntimeException("Incorrect file ID");
    }
}

