package com.assignment.etm.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Blob;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class FileEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "file_name", nullable = false, columnDefinition = "VARCHAR(200)")
    private String fileName;

    @Column(name = "file")
    private Blob file;

    @CreationTimestamp
    @Column(name = "uploaded_on", nullable = false)
    private LocalDateTime uploadedOn;

    @Column(name = "is_header_present")
    private Boolean isHeaderPresent;

    @Column(name = "delimiter", columnDefinition = "VARCHAR(60)")
    private String delimiter;

    @Column(name = "upload_file_path", columnDefinition = "VARCHAR(1000)")
    private String uploadFilePath;
}

