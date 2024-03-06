package ru.arzhana.asite;

import java.util.List;

public interface FileDateInterface {
    FileData getFileDataById(Long idFileData);
    FileData saveFileData(FileData fileData);
    List<FileData> getListFileDataByIds(List<Long> ids);
    List<FileData> getLast25FileData();
}
