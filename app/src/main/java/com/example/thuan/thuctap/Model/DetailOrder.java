package com.example.thuan.thuctap.Model;

public class DetailOrder {
    private String id;
    private String idOrder;
    private String idMilkTea;
    private Integer amount;
    private Long priceMilkTea;

    public DetailOrder() {
    }

    public DetailOrder(String id, String idOrder, String idMilkTea, Integer amount, Long priceMilkTea) {
        this.id = id;
        this.idOrder = idOrder;
        this.idMilkTea = idMilkTea;
        this.amount = amount;
        this.priceMilkTea = priceMilkTea;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMilkTea() {
        return idMilkTea;
    }

    public void setIdMilkTea(String idMilkTea) {
        this.idMilkTea = idMilkTea;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getPriceMilkTea() {
        return priceMilkTea;
    }

    public void setPriceMilkTea(Long priceMilkTea) {
        this.priceMilkTea = priceMilkTea;
    }
}
