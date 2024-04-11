package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;

import androidx.annotation.RequiresApi;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.ServiceDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;

public class ServiceServiceImpl implements IServiceService {
    ServiceDAOImpl serviceDAO;

    public ServiceServiceImpl(Context context) {
        serviceDAO = new ServiceDAOImpl(context);
    }

    @RequiresApi(api = 26)
    @Override
    public boolean add(Service service) {
        return serviceDAO.add(service);
    }

    @Override
    public boolean update(Service service) {
        return serviceDAO.update(service);
    }

    @Override
    public boolean softDelete(String id) {
        return serviceDAO.softDelete(id);
    }

    @Override
    public boolean revert(String id) {
        return serviceDAO.revert(id);
    }

    @Override
    public Service findById(String id) {
        return serviceDAO.findById(id);
    }

    @Override
    public List<Service> findAll() {
        return serviceDAO.findAll();
    }

    @Override
    public List<Service> findByStatus(String status) {
        return serviceDAO.findByStatus(status);
    }

    @Override
    public List<Service> findByInfo(String name, String description) {
        return serviceDAO.findByInfo(name, description);
    }

    @Override
    public boolean updateStatus(String id, String status) {
        return serviceDAO.updateStatus(id, status);
    }

    @Override
    public boolean updateSold(String id, int sold) {
        return serviceDAO.updateSold(id, sold);
    }

    @Override
    public boolean updateQuantity(String id, int quantity) {
        return serviceDAO.updateQuantity(id, quantity);
    }

    @Override
    public List<Service> getServicesWithLimitAndOffset(int limit, int offset, String status, int isDeleted, String orderBy) {
        return serviceDAO.getServicesWithLimitAndOffset(limit, offset, status, isDeleted, orderBy);
    }

    @Override
    public int countServices(String status, int isDeleted) {
        return serviceDAO.countServices(status, isDeleted);
    }

    @Override
    public List<String> findServiceName(String keyword, String status, int isDeleted) {
        return serviceDAO.findServiceName(keyword, status, isDeleted);
    }

    @Override
    public List<Service> findServiceByKeyword(String keyword, int limit, int offset, String status, int isDeleted) {
        return serviceDAO.findServiceByKeyword(keyword, limit, offset, status, isDeleted);
    }

    @Override
    public int countServicesSearch(String keyword, String status, int isDeleted) {
        return serviceDAO.countServicesSearch(keyword, status, isDeleted);
    }
}
