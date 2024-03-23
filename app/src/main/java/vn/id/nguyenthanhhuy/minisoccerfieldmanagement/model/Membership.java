package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Membership {
    private String id;
    private String name;
    private int discountRate;
    private BigDecimal minimumSpendAmount;
    private Boolean isDeleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Membership() {
    }

    public Membership(String id, String name, int discountRate, BigDecimal minimumSpendAmount, Boolean isDeleted, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.minimumSpendAmount = minimumSpendAmount;
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

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getMinimumSpendAmount() {
        return minimumSpendAmount;
    }

    public void setMinimumSpendAmount(BigDecimal minimumSpendAmount) {
        this.minimumSpendAmount = minimumSpendAmount;
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
