package ru.arzhana.asite;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FileDataService implements FileDateInterface{
    @Autowired
    private FileDateRepository fileDateRepo;


    @Override
    public FileData getFileDataById(Long idFileData) {
        return fileDateRepo.getReferenceById(idFileData);
    }

    @Override
    public FileData saveFileData(FileData fileData) {
        return fileDateRepo.save(fileData);
    }

    @Override
    public List<FileData> getListFileDataByIds(List<Long> ids) {
        return fileDateRepo.findAllById(ids);
    }

    @Override
    public List<FileData> getLast25FileData() {
        return fileDateRepo.findFirst25ByOrderByCreateDateDesc();
    }
}
