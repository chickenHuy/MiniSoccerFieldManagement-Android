package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

import java.math.BigDecimal;

public class IncomeChart {
    BigDecimal income;
    String date;
    String type;

    public IncomeChart(BigDecimal income, String date, String type) {
        this.income = income;
        this.date = date;
        this.type = type;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
