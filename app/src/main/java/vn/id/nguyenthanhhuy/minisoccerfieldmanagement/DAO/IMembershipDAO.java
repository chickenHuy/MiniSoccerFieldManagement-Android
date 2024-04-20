package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.math.BigDecimal;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;

public interface IMembershipDAO {
    Boolean add(Membership membership);
    Boolean update(Membership membership);
    Boolean softDelete(String id);
    Membership findById(String id);
    List<Membership> findAll();
    int getDiscountRateByCustomerId(String id);

}
