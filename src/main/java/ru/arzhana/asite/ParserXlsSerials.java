package ru.arzhana.asite;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParserXlsSerials implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(ParserXlsSerials.class);
    private Path path;
    private File file;
    private String nameStation;


    private DeskTableService service;


    public ParserXlsSerials(Path pathSavedFile) {
//        log.info("constructor() pathSavedFile: {}", pathSavedFile.toAbsolutePath());
        this.service = new DeskTableService();
        path = pathSavedFile;
        file = new File(pathSavedFile.toUri());
        Path fileNamePath = path.getFileName();
        String fileName = (fileNamePath != null) ? fileNamePath.toString() : null;
        if(fileName != null) {
            String[] arrName = fileName.trim().split("_");
//            log.info("constructor() {} arrName size: {}", fileName, arrName.length);
            nameStation = arrName[1].trim();
    //        nameStation = path.getFileName().toString().trim().split("_")[1];
            log.info("constructor() {}", nameStation);
        }
    }

    @Override
    public void run() {
        log.info("Thread RUN");
        List<DeskTable> listDeskTables = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(file);
             XSSFWorkbook book = new XSSFWorkbook(inputStream)) {

            int amountSheets = book.getNumberOfSheets();
            log.info("amountSheets {}", amountSheets);
//            List<DeskTable> listDeskTables= new ArrayList<>();
            for (int i = 0; i < amountSheets; i++) {
                XSSFSheet sheet = book.getSheetAt(i);
                Iterator<Row> rowIterator = sheet.iterator();
                sheet.forEach(row -> {
                    if(row.getRowNum() == 0){
                        DeskTable desk = headRowHandler((XSSFRow) row);
                        listDeskTables.add(desk);
                    } else if (row.getRowNum() > 1) {
                        try {
                            parserRow((XSSFRow) row);
                        } catch (NumberFormatException e) {
                            log.error("Error parsing row {}: {}", row.getRowNum() + 1, e.getMessage());
                        }
                    }
                });
            }
            if(!listDeskTables.isEmpty()) {
                if (service != null) {
                service.saveListDeskTable(listDeskTables);
                } else {
                log.error("service is null!");
                }
            } else {
                log.error("listDeskTables is Empty!");
            }

        } catch (IOException e) {
            log.error("Error parsing Excel file", e);
            throw new RuntimeException("Error parsing Excel file", e);
        } finally {
            file = null;
            path = null;
        };
    }

    private void parserRow(XSSFRow row) throws NumberFormatException{
//        log.info("row.getCell(1) {}", row.getCell(1).getRawValue().trim());
        int number = (int) Math.round(row.getCell(1).getNumericCellValue());
        String desk = row.getCell(2).getStringCellValue().trim();
        String pvmLocate = row.getCell(3).getStringCellValue().trim();
        int pvmPower = (int) Math.round(row.getCell(4).getNumericCellValue());
        String serial = row.getCell(5).getStringCellValue().trim();
        double lat = row.getCell(6).getNumericCellValue();
        double lon = row.getCell(7).getNumericCellValue();
//        log.info("parserRow() {}, {}, {}, {}, {}, {}, {}",number, desk, pvmLocate, pvmPower, serial, lat, lon);
    }

    private DeskTable headRowHandler(XSSFRow row) {
//        XSSFCell cell = row.getCell(1);
//        String nameStation = cell.getRawValue();
        String nameStation = row.getCell(1).getStringCellValue().trim();
        log.info("headRowHandler() nameStation {}", nameStation);
        DeskTable deskTable = new DeskTable();
        if(this.nameStation.equals(nameStation)){
            String nameRow = row.getCell(2).getStringCellValue().trim();
            String nameDesk = row.getCell(3).getStringCellValue().trim();
            if(nameDesk.contains(nameRow)) {
                log.info("headRowHandler() nameRow={} matches the nameDesk={}", nameRow, nameDesk);
                String deskType = row.getCell(4).getStringCellValue().trim();
                String device = row.getCell(5).getStringCellValue().trim();
                deskTable.setNameDesk(nameDesk);
                deskTable.setIdStation(853L);
                deskTable.setIdMapArea(403L);
                deskTable.setIndexAxis(getAxisIndexByName(nameDesk));
                deskTable.setIndexRow(getRowIndexByRowName(nameRow));
                log.info("Desk name:{} Station:{} Map:{} row:{} col:{}",
                        deskTable.getNameDesk(), deskTable.getIdStation(), deskTable.getIdMapArea(),
                        deskTable.getIndexRow(), deskTable.getIndexAxis());

            } else {
                log.info("headRowHandler() nameRow={} does not match the nameDesk={}", nameRow, nameDesk);
            }
        } else {
            log.info("nameStation={} not equals this nameStation={}", nameStation, this.nameStation);
        }
        return deskTable;
    }

    private int getRowIndexByRowName(String nameRow) {
        int indexRow = -1;
        try {
            indexRow = Integer.parseInt(nameRow.substring(0, nameRow.length()-1)) - 1;
        }catch (NumberFormatException e){
            log.error("getAxisIndexByName() ", e);
        }
        return indexRow;
    }

    private int getAxisIndexByName(String nameDesk) {
        int indexAxis = -1;
        try {
            indexAxis = Integer.parseInt(nameDesk.split("-")[1]) - 1;
        }catch (NumberFormatException e){
            log.error("getAxisIndexByName() ", e);
        }
        return indexAxis;
    }
}
