package ru.arzhana.asite;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity(name = "desktables")
public class DeskTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idDesk;

    private Long idStation;

    private Long idMapArea;

    private Long idDeskType;

    @Column(length = 100)
    private String nameDesk;

    private Long statusId;

    private int indexRow;

    private int indexAxis;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateUpdate;


    public DeskTable() {
    }

    public Long getIdDesk() {
        return idDesk;
    }

    public void setIdDesk(Long idDesk) {
        this.idDesk = idDesk;
    }

    public Long getIdStation() {
        return idStation;
    }

    public void setIdStation(Long idStation) {
        this.idStation = idStation;
    }

    public Long getIdMapArea() {
        return idMapArea;
    }

    public void setIdMapArea(Long idMapArea) {
        this.idMapArea = idMapArea;
    }

    public Long getIdDeskType() {
        return idDeskType;
    }

    public void setIdDeskType(Long idDeskType) {
        this.idDeskType = idDeskType;
    }

    public String getNameDesk() {
        return nameDesk;
    }

    public void setNameDesk(String nameDesk) {
        this.nameDesk = nameDesk;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public int getIndexRow() {
        return indexRow;
    }

    public void setIndexRow(int indexRow) {
        this.indexRow = indexRow;
    }

    public int getIndexAxis() {
        return indexAxis;
    }

    public void setIndexAxis(int indexAxis) {
        this.indexAxis = indexAxis;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
}
