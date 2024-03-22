package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.sql.Timestamp;
import java.util.List;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Match;

public interface IMatchDAO {
    Boolean checkIn(int bookingId);
    void checkOut(int id);
    void softDelete(int id);
    Match findById(int id);
    List<Match> findByBooking(int bookingId);
    List<Match> findByDate(Timestamp date);
    List<Match> findByField(int fieldId);
    List<Match> findByDateAndField(Timestamp date, int fieldId);
}
