package com.example.thuan.thuctap.Model;

import java.util.Date;

public class MilkTea {
    private String id;
    private String idStore;
    private String idUser;
    private String nameMilkTea;
    private String price;
    private String dateUp;
    private String imageMilkTea;
    private String status;
    private String description;

    public MilkTea() {
    }

    public MilkTea(String id, String idStore, String idUser, String nameMilkTea, String price, String dateUp, String imageMilkTea, String status, String description) {
        this.id = id;
        this.idStore = idStore;
        this.idUser = idUser;
        this.nameMilkTea = nameMilkTea;
        this.price = price;
        this.dateUp = dateUp;
        this.imageMilkTea = imageMilkTea;
        this.status = status;
        this.description = description;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameMilkTea() {
        return nameMilkTea;
    }

    public void setNameMilkTea(String nameMilkTea) {
        this.nameMilkTea = nameMilkTea;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDateUp() {
        return dateUp;
    }

    public void setDateUp(String dateUp) {
        this.dateUp = dateUp;
    }

    public String getImageMilkTea() {
        return imageMilkTea;
    }

    public void setImageMilkTea(String imageMilkTea) {
        this.imageMilkTea = imageMilkTea;
    }

    public String getIdStore() {
        return idStore;
    }

    public void setIdStore(String idStore) {
        this.idStore = idStore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
