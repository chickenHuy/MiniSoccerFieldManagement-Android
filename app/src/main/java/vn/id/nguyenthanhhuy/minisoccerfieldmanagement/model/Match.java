package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

import java.sql.Timestamp;

public class Match {
    private int id;
    private int bookingId;
    private Timestamp checkIn;
    private Timestamp checkOut;
    private Boolean isDeleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Match() {
    }

    public Match(int id, int bookingId, Timestamp checkIn, Timestamp checkOut, Boolean isDeleted, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Timestamp getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Timestamp checkIn) {
        this.checkIn = checkIn;
    }

    public Timestamp getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Timestamp checkOut) {
        this.checkOut = checkOut;
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
