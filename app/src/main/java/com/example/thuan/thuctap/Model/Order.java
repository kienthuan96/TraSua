package com.example.thuan.thuctap.Model;

import java.util.ArrayList;

public class Order {
    private String id;
    private String idUser;
    private String idShipper;
    private ArrayList<DetailOrder> arrayList;
    private String addressOrder;
    private Integer pointOrder;
    private String dateOrder;
    private Long priceOrder;
    private String status;

    public Order() {
    }

    public Order(String id, String idUser, String idShipper, ArrayList<DetailOrder> arrayList, String addressOrder, Integer pointOrder, String dateOrder, Long priceOrder, String status) {
        this.id = id;
        this.idUser = idUser;
        this.idShipper = idShipper;
        this.arrayList = arrayList;
        this.addressOrder = addressOrder;
        this.pointOrder = pointOrder;
        this.dateOrder = dateOrder;
        this.priceOrder = priceOrder;
        this.status = status;
    }

    public String getIdShipper() {
        return idShipper;
    }

    public void setIdShipper(String idShipper) {
        this.idShipper = idShipper;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(Long priceOrder) {
        this.priceOrder = priceOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public ArrayList<DetailOrder> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<DetailOrder> arrayList) {
        this.arrayList = arrayList;
    }

    public String getAddressOrder() {
        return addressOrder;
    }

    public void setAddressOrder(String addressOrder) {
        this.addressOrder = addressOrder;
    }

    public Integer getPointOrder() {
        return pointOrder;
    }

    public void setPointOrder(Integer pointOrder) {
        this.pointOrder = pointOrder;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }
}
