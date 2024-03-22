package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Booking {
    private String id;
    private String customerId;
    private String fieldId;
    private String userId;
    private String status;
    private String note;
    private Timestamp timeStart;
    private Timestamp timeEnd;
    private BigDecimal price;
    private Boolean isDeleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Booking() {
    }

    public Booking(String id, String customerId, String fieldId, String userId, String status, String note, Timestamp timeStart, Timestamp timeEnd, BigDecimal price, Boolean isDeleted, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.fieldId = fieldId;
        this.userId = userId;
        this.status = status;
        this.note = note;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.price = price;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
