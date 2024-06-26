package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Time;

public class PriceList implements Serializable {
    private String id;
    private Time startTime;
    private Time endTime;
    private String typeField;
    private String dateOfWeek;
    private BigDecimal unitPricePer30Minutes;
    private Boolean isDeleted;
    private Timestamp createAt;
    private Timestamp updateAt;

    public PriceList() {
    }

    public PriceList(String id, Time startTime, Time endTime, String typeField, String dateOfWeek, BigDecimal unitPricePer30Minutes, Boolean isDeleted, Timestamp createAt, Timestamp updateAt) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.typeField = typeField;
        this.dateOfWeek = dateOfWeek;
        this.unitPricePer30Minutes = unitPricePer30Minutes;
        this.isDeleted = isDeleted;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getTypeField() {
        return typeField;
    }

    public void setTypeField(String typeField) {
        this.typeField = typeField;
    }

    public String getDateOfWeek() {
        return dateOfWeek;
    }

    public void setDateOfWeek(String dateOfWeek) {
        this.dateOfWeek = dateOfWeek;
    }

    public BigDecimal getUnitPricePer30Minutes() {
        return unitPricePer30Minutes;
    }

    public void setUnitPricePer30Minutes(BigDecimal unitPricePer30Minutes) {
        this.unitPricePer30Minutes = unitPricePer30Minutes;
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

    @Override
    public String toString() {
        return "PriceList{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", typeField='" + typeField + '\'' +
                ", dateOfWeek='" + dateOfWeek + '\'' +
                ", unitPricePer30Minutes=" + unitPricePer30Minutes +
                ", isDeleted=" + isDeleted +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
