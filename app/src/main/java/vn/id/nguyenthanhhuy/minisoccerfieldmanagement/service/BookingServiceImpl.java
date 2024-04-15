package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;
import android.widget.Toast;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.BookingDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IBookingDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.BookingDetail;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;

public class BookingServiceImpl implements IBookingService{
    IBookingDAO bookingDAO;
    Context context;
    public BookingServiceImpl(Context context) {
        bookingDAO = new BookingDAOImpl(context);
        this.context = context;
    }


    @Override
    public Boolean isBookedOfParentField(String idUpdated, Field child, Timestamp date, Time start, Time end) {
        IFieldService fieldService = new FieldServiceImpl(context);

        List<Field> parentFields = fieldService.findParent(child.getId());
        //Check itself
        parentFields.add(child);
        List<Booking> bookings = new ArrayList<>();
        for (Field field : parentFields) {
            bookings.addAll(bookingDAO.findByDateAndField(date, field.getId()));
        }
        String hourString = start.toString().substring(0, 2);
        String minuteString = start.toString().substring(3, 5);

        int hour = Integer.parseInt(hourString);
        int minute = Integer.parseInt(minuteString);
        LocalTime startLocalTime = LocalTime.of(hour, minute);

        hourString = end.toString().substring(0, 2);
        minuteString = end.toString().substring(3, 5);

        hour = Integer.parseInt(hourString);
        minute = Integer.parseInt(minuteString);
        LocalTime endLocalTime = LocalTime.of(hour, minute);
        Calendar cal = Calendar.getInstance();
        for (Booking booking : bookings) {
            if (booking.getId().equals(idUpdated)) {
                continue;
            }
            cal.setTimeInMillis(booking.getTimeStart().getTime());

            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);

            LocalTime bookedStart = LocalTime.of(hour, minute);

            cal.setTimeInMillis(booking.getTimeEnd().getTime());
            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);

            LocalTime bookedEnd = LocalTime.of(hour, minute);

            if (startLocalTime.isAfter(bookedStart) && startLocalTime.isBefore(bookedEnd)) {
                return true;
            }
            else if (endLocalTime.isAfter(bookedStart) && endLocalTime.isBefore(bookedEnd)) {
                return true;
            }
            else if (startLocalTime.isBefore(bookedStart) && endLocalTime.isAfter(bookedEnd)) {
                return true;
            }
            else if (startLocalTime.equals(bookedStart))
            {
                return true;
            }
            else if (endLocalTime.equals(bookedEnd))
            {
                return true;
            }


        }

        return false;
    }

    @Override
    public Boolean isBookedOfChildField(String idUpdated, Field Parent, Timestamp date, Time start, Time end) {
        IFieldService fieldService = new FieldServiceImpl(context);
        List<Field> childFields = new ArrayList<>();
        Field child1 = fieldService.findById(Parent.getCombineField1());
        Field child2 = fieldService.findById(Parent.getCombineField2());
        Field child3 = fieldService.findById(Parent.getCombineField3());
        if (child1 != null) {
            childFields.add(child1);
        }
        if (child2 != null) {
            childFields.add(child2);
        }
        if (child3 != null) {
            childFields.add(child3);
        }
        // tính toán luôn cả chính nó
        childFields.add(Parent);
        List<Booking> bookings = new ArrayList<>();
        for (Field field : childFields) {
            bookings.addAll(bookingDAO.findByDateAndField(date, field.getId()));
        }

        String hourString = start.toString().substring(0, 2);
        String minuteString = start.toString().substring(3, 5);

        int hour = Integer.parseInt(hourString);
        int minute = Integer.parseInt(minuteString);
        LocalTime startLocalTime = LocalTime.of(hour, minute);

        hourString = end.toString().substring(0, 2);
        minuteString = end.toString().substring(3, 5);

        hour = Integer.parseInt(hourString);
        minute = Integer.parseInt(minuteString);
        LocalTime endLocalTime = LocalTime.of(hour, minute);
        Calendar cal = Calendar.getInstance();
        for (Booking booking : bookings) {
            if (booking.getId().equals(idUpdated)) {
                continue;
            }
            cal.setTimeInMillis(booking.getTimeStart().getTime());

            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);

            LocalTime bookedStart = LocalTime.of(hour, minute);

            cal.setTimeInMillis(booking.getTimeEnd().getTime());
            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);

            LocalTime bookedEnd = LocalTime.of(hour, minute);

            if (startLocalTime.isAfter(bookedStart) && startLocalTime.isBefore(bookedEnd)) {
                return true;
            }
            else if (endLocalTime.isAfter(bookedStart) && endLocalTime.isBefore(bookedEnd)) {
                return true;
            }
            else if (startLocalTime.isBefore(bookedStart) && endLocalTime.isAfter(bookedEnd)) {
                return true;
            }
            else if (startLocalTime.equals(bookedStart))
            {
                return true;
            }
            else if (endLocalTime.equals(bookedEnd))
            {
                return true;
            }


        }
        return  false;


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
    public Boolean update(Booking booking) {
        return bookingDAO.update(booking);
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
    public List<Booking> findUpcomingBookings(String status) {
        return bookingDAO.findUpcomingBookings(status);
    }

    @Override
    public List<Booking> findLiveBookings() {
        return bookingDAO.findLiveBookings();
    }


    @Override
    public List<Booking> findByDate(Timestamp date) {
        return bookingDAO.findByDate(date);
    }

    @Override
    public List<Booking> findByDateAndField(Timestamp date, String fieldId) {
        return bookingDAO.findByDateAndField(date, fieldId);
    }

    @Override
    public BookingDetail getBookingDetail(String status, String bookingId) {
        return bookingDAO.getBookingDetail(status, bookingId);
    }
}
