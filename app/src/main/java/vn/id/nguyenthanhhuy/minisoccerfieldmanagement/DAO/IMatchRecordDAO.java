package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.sql.Timestamp;
import java.util.List;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.MatchRecord;

public interface IMatchRecordDAO {
    Boolean checkIn(String bookingId);
    void checkOut(String id);
    void softDelete(String id);
    MatchRecord findById(String id);
    List<MatchRecord> findByBooking(String bookingId);
    List<MatchRecord> findByDate(Timestamp date);
    List<MatchRecord> findByField(String fieldId);
    List<MatchRecord> findByDateAndField(Timestamp date, String fieldId);
}
