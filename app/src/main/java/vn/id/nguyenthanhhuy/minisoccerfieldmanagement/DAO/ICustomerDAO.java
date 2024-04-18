package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.math.BigDecimal;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;

public interface ICustomerDAO {
    Boolean add(Customer customer);
    Boolean update(Customer customer);
    Boolean softDelete(String id);
    Customer findById(String id);
    Customer findByPhoneNumber(String phoneNumber);
    List<Customer> findAll();
    List<Customer> findByMemberShip(String membershipId);
    List<Customer> findCustomer(String searchparam);
    String getMembershipNameByID(String membershipId);
}
