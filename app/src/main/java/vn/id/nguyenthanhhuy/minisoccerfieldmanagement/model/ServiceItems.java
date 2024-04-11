package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class ServiceItems implements Serializable {
    private String id;
    private String serviceUsageId;
    private String serviceId;
    private int quantity;
    private Boolean isDeleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ServiceItems() {
    }

    public ServiceItems(String id, String serviceUsageId, String serviceId, int quantity, Boolean isDeleted, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.serviceUsageId = serviceUsageId;
        this.serviceId = serviceId;
        this.quantity = quantity;
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

    public String getServiceUsageId() {
        return serviceUsageId;
    }

    public void setServiceUsageId(String serviceUsageId) {
        this.serviceUsageId = serviceUsageId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
}
