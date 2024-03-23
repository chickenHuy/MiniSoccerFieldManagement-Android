package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.math.BigDecimal;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;

public interface ICustomerDAO {
    Boolean add(Customer customer);
    Boolean update(Customer customer);
    Boolean softDelete(String id);
    Boolean updateTotalSpend(String id, BigDecimal increment);
    Customer findById(String id);
    Customer findByPhoneNumber(String phoneNumber);
    List<Customer> findByMemberShip(String membershipId);
}
