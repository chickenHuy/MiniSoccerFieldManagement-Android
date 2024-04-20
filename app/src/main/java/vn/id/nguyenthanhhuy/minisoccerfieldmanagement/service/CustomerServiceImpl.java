package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;

import java.math.BigDecimal;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.CustomerDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.ICustomerDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IMembershipDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.MembershipDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;

public class CustomerServiceImpl implements ICustomerService{
    ICustomerDAO customerDAO;
    Context context;
    public CustomerServiceImpl(Context context) {
        customerDAO = new CustomerDAOImpl(context);
        this.context = context;
    }
    @Override
    public Boolean add(Customer customer) {
        return customerDAO.add(customer);
    }

    @Override
    public Boolean update(Customer customer) {
        return customerDAO.update(customer);
    }

    @Override
    public Boolean softDelete(String id) {
        return customerDAO.softDelete(id);
    }

    @Override
    public Customer findById(String id) {
        return customerDAO.findById(id);
    }

    @Override
    public Customer findByPhoneNumber(String phoneNumber) {
        return customerDAO.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<Customer> findAll() {
        return customerDAO.findAll();
    }

    @Override
    public List<Customer> findByMemberShip(String membershipId) {
        return customerDAO.findByMemberShip(membershipId);
    }

    @Override
    public int findDiscountByCustomer(String customerId) {
        Customer customer = customerDAO.findById(customerId);
        if (customer == null) {
            return -1;
        }
        else {
            IMembershipDAO membershipDAO = new MembershipDAOImpl(context);
            return membershipDAO.findById(customer.getMemberShipId()).getDiscountRate();
        }
    }
    @Override
    public List<Customer> findCustomer(String searchparam){
        return customerDAO.findCustomer(searchparam);
    }

    @Override
    public String getMembershipNameByID(String membershipId) {
        return customerDAO.getMembershipNameByID(membershipId);
    }

    @Override
    public Boolean updateTotalSpend(String id, BigDecimal increment) {
        Customer customer = customerDAO.findById(id);
        IMembershipService memberShipService = new MembershipServiceImpl(context);
        if (customer == null) {
            return false;
        }
        customer.setTotalSpend(customer.getTotalSpend().add(increment));
        Membership newMemberShip = memberShipService.findBySpendAmount(customer.getTotalSpend());
        customer.setMemberShipId(newMemberShip.getId());
        return update(customer);
    }
}
