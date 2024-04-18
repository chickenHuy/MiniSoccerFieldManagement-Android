package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

public class BookingChart {
    int count;
    String createdAt;

    public BookingChart(int count, String createdAt) {
        this.count = count;
        this.createdAt = createdAt;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
