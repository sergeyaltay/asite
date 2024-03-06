package ru.arzhana.asite;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity(name = "files")
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idFileData;
    private String originalName;
    private String storageName;
    private String storagePath;
    private String extFile;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    public FileData() {
    }

    public FileData(String cleanFilename, String destFileName, String path, String contentType) {
        originalName = cleanFilename;
        storageName = destFileName;
        storagePath = path;
        extFile = contentType;
        createDate = new Date();
    }

    public Long getIdFileData() {
        return idFileData;
    }

    public void setIdFileData(Long idFileData) {
        this.idFileData = idFileData;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getExtFile() {
        return extFile;
    }

    public void setExtFile(String extFile) {
        this.extFile = extFile;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
