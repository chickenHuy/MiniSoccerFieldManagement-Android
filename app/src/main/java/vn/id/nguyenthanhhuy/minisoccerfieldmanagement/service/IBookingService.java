package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;

public interface IBookingService {
    Boolean add (Booking booking);
    Boolean updateStatus(String id, String status);
    Boolean softDelete(String id);
    Booking findById(String id);
    List<Booking> findByCustomer(String customerId);
    List<Booking> findByUser(String userId);
    List<Booking> findByField(String fieldId);
    List<Booking> findByStatus(String status);
    List<Booking> findByDate(Timestamp date);
    List<Booking> findByDateAndField(Timestamp date, String fieldId);
}
