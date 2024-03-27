package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.MatchRecord;

public interface IMatchRecordService {
    Boolean checkIn(MatchRecord matchRecord);
    Boolean checkOut(String id);
    Boolean softDelete(String id);
    MatchRecord findById(String id);
    List<MatchRecord> findByBooking(String bookingId);
    List<MatchRecord> findByDate(Timestamp date);
    List<MatchRecord> findByField(String fieldId);
    List<MatchRecord> findByDateAndField(Timestamp date, String fieldId);
}
