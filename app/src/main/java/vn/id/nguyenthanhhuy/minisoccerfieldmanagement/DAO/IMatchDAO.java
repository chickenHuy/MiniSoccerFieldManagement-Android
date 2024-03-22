package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.sql.Timestamp;
import java.util.List;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Match;

public interface IMatchDAO {
    Boolean checkIn(String bookingId);
    void checkOut(String id);
    void softDelete(String id);
    Match findById(String id);
    List<Match> findByBooking(String bookingId);
    List<Match> findByDate(Timestamp date);
    List<Match> findByField(String fieldId);
    List<Match> findByDateAndField(Timestamp date, String fieldId);
}
