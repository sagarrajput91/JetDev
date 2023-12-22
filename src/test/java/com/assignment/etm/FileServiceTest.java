package com.assignment.etm;

import com.assignment.etm.bean.FileDownloadResponse;
import com.assignment.etm.bean.FileUploadResponse;
import com.assignment.etm.entity.FileEntity;
import com.assignment.etm.repository.FileRepository;
import com.assignment.etm.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileServiceImpl fileService ;

    @Test
    public void testUploadFile() {
        MultipartFile file = createMockMultipartFile();

        FileEntity mockedSavedEntity = new FileEntity();
        mockedSavedEntity.setId("1");
        mockedSavedEntity.setFileName("testFile");

        when(fileRepository.save(any(FileEntity.class))).thenReturn(mockedSavedEntity);

        FileUploadResponse result = fileService.uploadFile(file, true);

        assertEquals(file.getName(), result.getFileName());

        verify(fileRepository, times(1)).save(any(FileEntity.class));
    }


    @Test
    public void testCheckUploadStatus() {
        String fileId = "1";
        String result = fileService.checkUploadStatus(fileId);
        assertEquals("File upload failed. Status: FAILED", result);
    }

    @Test
    public void testListFiles() {
        List<FileEntity> files = Arrays.asList(createFileEntity("File1"), createFileEntity("File2"));
        when(fileRepository.findAll()).thenReturn(files);

        List<String> result = fileService.listFiles();

        assertEquals(2, result.size());
        assertEquals("File1", result.get(0));
        assertEquals("File2", result.get(1));
    }

    @Test
    public void testReviewFileRecords() {
        String fileId = "1";
        FileEntity mockedFileEntity = new FileEntity();
        mockedFileEntity.setFileName("testFile.xlsx");
        when(fileRepository.findById(fileId)).thenReturn(Optional.of(mockedFileEntity));
        FileDownloadResponse result = fileService.reviewFileRecords(fileId);
        assertEquals("testFile.xlsx", result.getFileName());
    }


    @Test
    public void testDeleteFile() {
        String fileId = "1";

        FileEntity mockedFileEntity = new FileEntity();
        when(fileRepository.findById(fileId)).thenReturn(Optional.of(mockedFileEntity));
        String result = fileService.deleteFile(fileId);
        assertEquals("File deleted successfully", result);
        verify(fileRepository, times(1)).findById(fileId);
        verify(fileRepository, times(1)).delete(mockedFileEntity);
    }



    private MultipartFile createMockMultipartFile() {
        return new MockMultipartFile("testFile", "testFile.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "test data".getBytes());
    }

    private FileEntity createFileEntity(String fileName) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileName);
        return fileEntity;
    }
}

