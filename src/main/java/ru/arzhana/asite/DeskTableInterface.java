package ru.arzhana.asite;

import java.util.List;

public interface DeskTableInterface {
    DeskTable getDeskTableById(Long id);
    List<DeskTable> getAllDeskTablesByIdStation(Long id);
    void saveDeskTable(DeskTable deskTable);
    List<DeskTable> saveListDeskTable(List<DeskTable> listDesks);
    DeskTable updateDeskTable(DeskTable deskTable);
}
