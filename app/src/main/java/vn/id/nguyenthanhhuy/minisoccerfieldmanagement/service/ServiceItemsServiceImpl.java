package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.AppTransactionDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IServiceItemsDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.ServiceItemsDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceItems;

public class ServiceItemsServiceImpl implements IServiceItemsService{
    IServiceItemsDAO serviceItemsDAO;
    Context context;

    public ServiceItemsServiceImpl(Context context) {
        serviceItemsDAO = new ServiceItemsDAOImpl(context);
        this.context = context;
    }

    @Override
    public boolean add(ServiceItems serviceItems) {
        return serviceItemsDAO.add(serviceItems);
    }

    @Override
    public boolean update(ServiceItems serviceItems) {
        return serviceItemsDAO.update(serviceItems);
    }

    @Override
    public boolean updateQty(String id, int quantity) {
        return serviceItemsDAO.updateQty(id, quantity);
    }

    @Override
    public boolean softDelete(String id) {
        return serviceItemsDAO.softDelete(id);
    }

    @Override
    public ServiceItems findById(String id) {
        return serviceItemsDAO.findById(id);
    }

    @Override
    public List<ServiceItems> findByServiceUsage(String serviceUsageId) {
        return serviceItemsDAO.findByServiceUsage(serviceUsageId);
    }

    @Override
    public List<ServiceItems> findByService(String serviceId) {
        return serviceItemsDAO.findByService(serviceId);
    }

    @Override
    public Service getServiceInfo(String serviceID) {
        return serviceItemsDAO.getServiceInfo(serviceID);
    }
}
