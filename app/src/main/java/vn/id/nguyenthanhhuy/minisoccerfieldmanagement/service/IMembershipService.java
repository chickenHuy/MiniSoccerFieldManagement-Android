package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import java.math.BigDecimal;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;

public interface IMembershipService {
    Boolean add(Membership membership);
    Boolean update(Membership membership);
    Boolean softDelete(String id);
    Membership findById(String id);
    List<Membership> findAll();
    public Membership findBySpendAmount(BigDecimal totalSpend);
    int getDiscountRateByCustomerId(String id);
}
