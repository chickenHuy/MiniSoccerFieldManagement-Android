package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Service implements Serializable {
    private String id;
    private String name;
    private BigDecimal price;
    private byte[] image;
    private String description;
    private String unit;
    private int quantity;
    private int sold;
    private String status;
    private Boolean isDeleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private int orderQuantity;

    public Service() {
    }

    public Service(String id, String name, BigDecimal price, byte[] image, String description, String unit, int quantity, int sold, String status, Boolean isDeleted, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.unit = unit;
        this.quantity = quantity;
        this.sold = sold;
        this.status = status;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Service clone() {
        Service service = new Service(id, name, price, image, description, unit, quantity, sold, status, isDeleted, createdAt, updatedAt);
        service.setOrderQuantity(orderQuantity);
        return service;
    }
}
