package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import java.math.BigDecimal;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;

public interface ICustomerService {
    Boolean add(Customer customer);
    Boolean update(Customer customer);
    Boolean softDelete(String id);
    Customer findById(String id);
    Customer findByPhoneNumber(String phoneNumber);
    List<Customer> findAll();
    List<Customer> findByMemberShip(String membershipId);
    int findDiscountByCustomer(String customerId);
    List<Customer> findCustomer(String searchparam);
    String getMembershipNameByID(String membershipId);
    Boolean updateTotalSpend(String id, BigDecimal increment);
}
