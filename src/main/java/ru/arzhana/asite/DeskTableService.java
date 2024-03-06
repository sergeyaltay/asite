package ru.arzhana.asite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DeskTableService implements DeskTableInterface {
    @Autowired
    private DeskTableRepository deskTableRepo;

    @Override
    public DeskTable getDeskTableById(Long id) {
        return deskTableRepo.getReferenceById(id);
    }

    @Override
    public List<DeskTable> getAllDeskTablesByIdStation(Long idStation) {
        return deskTableRepo.findAllByIdStation(idStation);
    }

    @Override
    public void saveDeskTable(DeskTable deskTable) {
        if(deskTable.getDateCreate() == null){
            deskTable.setDateCreate(new Date());
        }
        deskTableRepo.save(deskTable);
    }

    @Override
    public List<DeskTable> saveListDeskTable(List<DeskTable> listDesks) {
        return deskTableRepo.saveAllAndFlush(listDesks);
    }

    @Override
    public DeskTable updateDeskTable(DeskTable deskTable) {
        deskTable.setDateUpdate(new Date());
        return deskTableRepo.save(deskTable);
    }
}
