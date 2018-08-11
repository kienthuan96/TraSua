package com.example.thuan.thuctap.Model;

public class User {
    private String idUser;
    private String name;
    private Integer point;
    private String status;
    private String numberPhone;

    public User() {
    }

    public User(String idUser, String name, Integer point, String status, String numberPhone) {
        this.idUser = idUser;
        this.name = name;
        this.point = point;
        this.status = status;
        this.numberPhone = numberPhone;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
