package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;
import android.content.Intent;

import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IMatchRecordDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.MatchRecordDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.MatchRecord;

public class MatchRecordServiceImpl implements IMatchRecordService{
    IMatchRecordDAO matchRecordDAO;
    Context context;
    public MatchRecordServiceImpl(Context context) {
        matchRecordDAO = new MatchRecordDAOImpl(context);
        this.context = context;
    }
    @Override
    public Boolean checkIn(MatchRecord matchRecord) {
        return matchRecordDAO.checkIn(matchRecord);
    }

    @Override
    public Boolean checkOut(String id) {
        return matchRecordDAO.checkOut(id);
    }

    @Override
    public Boolean softDelete(String id) {
        return matchRecordDAO.softDelete(id);
    }

    @Override
    public MatchRecord findById(String id) {
        return matchRecordDAO.findById(id);
    }

    @Override
    public MatchRecord findByBooking(String bookingId) {
        return matchRecordDAO.findByBooking(bookingId);
    }

    @Override
    public List<MatchRecord> findByDate(Timestamp date) {
        return matchRecordDAO.findByDate(date);
    }

    @Override
    public List<MatchRecord> findByField(String fieldId) {
        return matchRecordDAO.findByField(fieldId);
    }

    @Override
    public List<MatchRecord> findByDateAndField(Timestamp date, String fieldId) {
        return matchRecordDAO.findByDateAndField(date, fieldId);
    }
}
