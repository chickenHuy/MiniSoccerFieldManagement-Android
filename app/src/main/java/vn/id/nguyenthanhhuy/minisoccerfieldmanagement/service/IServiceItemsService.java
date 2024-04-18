package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceItems;

public interface IServiceItemsService {
    boolean add(ServiceItems serviceItems);

    boolean update(ServiceItems serviceItems);

    boolean updateQty(String id, int quantity);

    boolean softDelete(String id);

    ServiceItems findById(String id);

    List<ServiceItems> findByServiceUsage(String serviceUsageId);

    List<ServiceItems> findByService(String serviceId);
    Service getServiceInfo(String serviceID);
}
