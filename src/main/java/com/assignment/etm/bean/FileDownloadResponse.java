package com.assignment.etm.bean;

import lombok.Data;

@Data
public class FileDownloadResponse {
    String fileName;
    byte[] fileContent;
    Boolean isHeaderPresent;
}
