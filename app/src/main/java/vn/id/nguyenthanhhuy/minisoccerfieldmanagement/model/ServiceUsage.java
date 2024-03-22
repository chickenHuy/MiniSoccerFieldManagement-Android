package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

import java.sql.Timestamp;

public class ServiceUsage {
    private String id;
    private String matchId;
    private String customerId;
    private String note;
    private Boolean isDeleted;
    private Timestamp createAt;
    private Timestamp updateAt;

    public ServiceUsage() {
    }

    public ServiceUsage(String id, String matchId, String customerId, String note, Boolean isDeleted, Timestamp createAt, Timestamp updateAt) {
        this.id = id;
        this.matchId = matchId;
        this.customerId = customerId;
        this.note = note;
        this.isDeleted = isDeleted;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
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

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }
}
