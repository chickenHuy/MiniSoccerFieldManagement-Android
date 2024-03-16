package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Time;

public class PriceList {
    private int id;
    private Time startTime;
    private Time endTime;
    private String typeField;
    private String dateOfWeek;
    private BigDecimal unitPricePer30Minutes;
    private Boolean isDeleted;
    private Date createAt;
    private Date updateAt;

    public PriceList() {
    }

    public PriceList(int id, Time startTime, Time endTime, String typeField, String dateOfWeek, BigDecimal unitPricePer30Minutes, Boolean isDeleted, Date createAt, Date updateAt) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
