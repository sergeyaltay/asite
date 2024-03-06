package ru.arzhana.asite;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequestMapping("/api/files")
@RestController
public class UploadFileRestController {

    private static final Logger log = LoggerFactory.getLogger(UploadFileRestController.class);
    private final FileDataService fileDataService;
    private final ParserXlsSerials parserXlsSerials;
    @Value("${upload.path}")
    private String uploadPath;
    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize MAX_SIZE;
    @Autowired
    public UploadFileRestController(FileDataService fileDataService, ParserXlsSerials parserXlsSerials) {
        this.fileDataService = fileDataService;
        this.parserXlsSerials = parserXlsSerials;
    }


    @GetMapping
    public ResponseEntity<List<FileData>> getPage(){
       log.info("getPageForm");
       return ResponseEntity.ok().body(fileDataService.getLast25FileData());
    }

    @PostMapping
    public ResponseEntity<List<FileData>> handleFileUpload(
            @RequestParam("file") MultipartFile multipartFile) {
        log.info("Загрузка файла: {}", multipartFile.getName());

        if (multipartFile.isEmpty()) {
            log.info("multipartFile isEmpty()");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (multipartFile.getSize() > MAX_SIZE.toBytes()) {
            log.info("multipartFile over Size");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        String cleanFilename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileExtension = StringUtils.getFilenameExtension(cleanFilename);
        Path dest = Paths.get(uploadPath, String.valueOf(year), String.valueOf(month), String.valueOf(day));
        Path relativePath = Paths.get(uploadPath).relativize(dest);
        String relativePathString = relativePath.toString();
        String destFileName;

        try {
            Files.createDirectories(dest);

            byte[] bytes = multipartFile.getBytes();
            String uuid = UUID.randomUUID().toString();
            destFileName = uuid + "-" + cleanFilename;
            Path filePath = Paths.get(dest.toString(), destFileName);

            try (var outputStream = Files.newOutputStream(filePath)) {
                outputStream.write(bytes);
                log.info("Файл успешно загружен: {}", filePath);
                FileData fileData = new FileData(cleanFilename, destFileName, relativePathString, fileExtension);
                FileData fileDataSaved = fileDataService.saveFileData(fileData);
                if(fileIsXlsSerials(multipartFile)){
                    log.info("fileIsXlsSerials() TRUE");
                    Path pathSavedFile = Paths.get(uploadPath, fileDataSaved.getStoragePath(), fileDataSaved.getStorageName());
//                    ParserXlsSerials parserXlsSerials = new ParserXlsSerials(pathSavedFile);
                    parserXlsSerials.parsingProcess(pathSavedFile);

                }
                return ResponseEntity.ok().body(fileDataService.getLast25FileData());
            }

        } catch (NoSuchFileException e) {
            log.error("Не найдены родительские папки для загрузки файла", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        } catch (IOException e) {
            log.error("Ошибка при загрузке файла", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    private boolean fileIsXlsSerials(MultipartFile file) {
        if (file != null && file.getSize() > 100) {
            String filename = file.getOriginalFilename();
            if(filename != null && (filename.endsWith(".xls") || filename.endsWith(".xlsx"))){
                String[] arrName = Arrays.stream(filename.split("_"))
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);
                if(arrName.length == 5){
                    if(arrName[0].trim().length()<6
                            && arrName[3].trim().matches("^\\d{2}-\\d{2}-\\d{2}$")
                            && arrName[4].trim().matches("^([01]\\d|2[0-3])-[0-5]\\d-[0-5]\\d.*")
                    ){
                        log.info("fileIsXlsSerials() " + file.getOriginalFilename() + " " + arrName.length);
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
