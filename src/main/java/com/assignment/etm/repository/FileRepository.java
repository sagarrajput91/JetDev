package com.assignment.etm.repository;

import com.assignment.etm.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {
    Optional<FileEntity>  findByFileName(String fileName);
}

