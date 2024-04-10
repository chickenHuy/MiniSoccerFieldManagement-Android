package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;

public interface IServiceService {
    public boolean add(Service service);

    public boolean update(Service service);

    public boolean softDelete(String id);
    public boolean revert(String id);

    public Service findById(String id);

    public List<Service> findAll();

    public List<Service> findByStatus(String status);

    public List<Service> findByInfo(String name, String description);

    public boolean updateStatus(String id, String status);

    public boolean updateSold(String id, int sold);

    public boolean updateQuantity(String id, int quantity);

    public List<Service> getServicesWithLimitAndOffset(int limit, int offset, String status, int isDeleted, String orderBy);

    public int countServices(String status, int isDeleted);
}
