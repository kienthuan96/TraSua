package com.example.thuan.thuctap.Model;

import com.example.thuan.thuctap.Model.MilkTea;

import java.util.ArrayList;

public class Store {
    private String id;
    private String nameStore;
    private String idUser;
    private String address;
    private String numberPhone;
    private String imageStore;
    private ArrayList<MilkTea> listMilkTea;


    public Store() {
    }

    public Store(String id, String nameStore, String idUser, String address, String numberPhone, String imageStore, ArrayList<MilkTea> listMilkTea) {
        this.id = id;
        this.nameStore = nameStore;
        this.idUser = idUser;
        this.address = address;
        this.numberPhone = numberPhone;
        this.imageStore = imageStore;
        this.listMilkTea = listMilkTea;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getImageStore() {
        return imageStore;
    }

    public void setImageStore(String imageStore) {
        this.imageStore = imageStore;
    }

    public ArrayList<MilkTea> getListMilkTea() {
        return listMilkTea;
    }

    public void setListMilkTea(ArrayList<MilkTea> listMilkTea) {
        this.listMilkTea = listMilkTea;
    }
}
