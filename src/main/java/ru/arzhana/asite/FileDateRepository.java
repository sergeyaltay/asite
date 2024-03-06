package ru.arzhana.asite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileDateRepository extends JpaRepository<FileData, Long> {
//    List<FileData> findAllByIdFileData(List<Long> ids);
    List<FileData> findFirst25ByOrderByCreateDateDesc();
}
