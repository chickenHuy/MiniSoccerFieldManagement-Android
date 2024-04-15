package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.BookingDetail;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;

public interface IBookingService {
    Boolean isBookedOfParentField(String idUpdated ,Field child, Timestamp date, Time start, Time end);
    Boolean isBookedOfChildField(String idUpdated, Field Parent, Timestamp date, Time start, Time end);
    Boolean add (Booking booking);
    Boolean updateStatus(String id, String status);
    Boolean update(Booking booking);
    Boolean softDelete(String id);
    Booking findById(String id);
    List<Booking> findByCustomer(String customerId);
    List<Booking> findByUser(String userId);
    List<Booking> findByField(String fieldId);
    List<Booking> findByStatus(String status);
    List<Booking> findUpcomingBookings(String status);
    List<Booking> findLiveBookings();
    List<Booking> findByDate(Timestamp date);
    List<Booking> findByDateAndField(Timestamp date, String fieldId);
    BookingDetail getBookingDetail(String status, String bookingId);
}
