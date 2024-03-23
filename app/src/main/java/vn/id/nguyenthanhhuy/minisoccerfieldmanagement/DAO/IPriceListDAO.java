package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.PriceList;

public interface IPriceListDAO {
    Boolean add(PriceList model);

    Boolean update(PriceList model);

    Boolean softDelete(String id);

    List<PriceList> findByDateOfWeek(String date);

    List<PriceList> findAll();

    PriceList findById(String id);

    BigDecimal findPriceByTime(Timestamp dateTimeIn, Timestamp dateTimeOut, String date);
}
