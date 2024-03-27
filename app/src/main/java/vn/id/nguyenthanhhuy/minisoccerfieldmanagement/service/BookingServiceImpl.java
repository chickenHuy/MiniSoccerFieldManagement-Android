package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;

import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.BookingDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IBookingDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;

public class BookingServiceImpl implements IBookingService{
    IBookingDAO bookingDAO;
    Context context;
    public BookingServiceImpl(Context context) {
        bookingDAO = new BookingDAOImpl(context);
        this.context = context;
    }
    @Override
    public Boolean add(Booking booking) {
        return bookingDAO.add(booking);
    }

    @Override
    public Boolean updateStatus(String id, String status) {
        return bookingDAO.updateStatus(id, status);
    }

    @Override
    public Boolean softDelete(String id) {
        return bookingDAO.softDelete(id);
    }

    @Override
    public Booking findById(String id) {
        return bookingDAO.findById(id);
    }

    @Override
    public List<Booking> findByCustomer(String customerId) {
        return bookingDAO.findByCustomer(customerId);
    }

    @Override
    public List<Booking> findByUser(String userId) {
        return bookingDAO.findByUser(userId);
    }

    @Override
    public List<Booking> findByField(String fieldId) {
        return bookingDAO.findByField(fieldId);
    }

    @Override
    public List<Booking> findByStatus(String status) {
        return bookingDAO.findByStatus(status);
    }


    @Override
    public List<Booking> findByDate(Timestamp date) {
        return bookingDAO.findByDate(date);
    }

    @Override
    public List<Booking> findByDateAndField(Timestamp date, String fieldId) {
        return bookingDAO.findByDateAndField(date, fieldId);
    }
}
