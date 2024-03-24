package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.PriceList;

public interface IPriceListService {
    Boolean add(PriceList model);

    Boolean update(PriceList model);

    Boolean softDelete(String id);

    List<PriceList> findByDateOfWeek(String date);

    List<PriceList> findAll();

    PriceList findById(String id);

    BigDecimal findPriceByTime(Time timeIn, Time timeOut, String date);
}
