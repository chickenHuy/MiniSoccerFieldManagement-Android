package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

public class ServiceChart {
    int rate;
    String name;

    public ServiceChart(int rate, String name) {
        this.rate = rate;
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
