package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class ServiceUsage implements Serializable {
    private String id;
    private String matchRecordId;
    private String customerId;
    private String note;
    private Boolean isDeleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ServiceUsage() {
    }

    public ServiceUsage(String id, String matchRecordId, String customerId, String note, Boolean isDeleted, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.matchRecordId = matchRecordId;
        this.customerId = customerId;
        this.note = note;
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

    public String getMatchRecordId() {
        return matchRecordId;
    }

    public void setMatchRecordId(String matchId) {
        this.matchRecordId = matchId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
