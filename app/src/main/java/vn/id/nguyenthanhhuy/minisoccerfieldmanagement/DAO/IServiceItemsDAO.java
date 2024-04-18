package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceItems;

public interface IServiceItemsDAO {
    public boolean add(ServiceItems serviceItems);

    public boolean update(ServiceItems serviceItems);

    public boolean updateQty(String id, int quantity);

    public boolean softDelete(String id);

    public ServiceItems findById(String id);

    public List<ServiceItems> findByServiceUsage(String serviceUsageId);

    public List<ServiceItems> findByService(String serviceId);
    Service getServiceInfo(String serviceID);
}
