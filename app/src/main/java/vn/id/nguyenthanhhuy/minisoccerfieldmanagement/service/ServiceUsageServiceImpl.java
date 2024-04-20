package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IServiceUsageDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.ServiceUsageDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceUsage;

public class ServiceUsageServiceImpl implements IServiceUsageService{
    IServiceUsageDAO serviceUsageDAO;
    public ServiceUsageServiceImpl(Context context) {
        serviceUsageDAO = new ServiceUsageDAOImpl(context);
    }
    @Override
    public Boolean add(ServiceUsage serviceUsage) {
        return serviceUsageDAO.add(serviceUsage);
    }

    @Override
    public Boolean update(ServiceUsage serviceUsage) {
        return serviceUsageDAO.update(serviceUsage);
    }

    @Override
    public Boolean softDelete(String id) {
        return serviceUsageDAO.softDelete(id);
    }

    @Override
    public ServiceUsage findById(String id) {
        return serviceUsageDAO.findById(id);
    }

    @Override
    public ServiceUsage findByMatchRecord(String matchRecordId) {
        return serviceUsageDAO.findByMatchRecord(matchRecordId);
    }

    @Override
    public List<ServiceUsage> findByCustomer(String customerId) {
        return serviceUsageDAO.findByCustomer(customerId);
    }

    @Override
    public double getTotalServicePriceByMatchId(String matchRecordId) {
        return serviceUsageDAO.getTotalServicePriceByMatchId(matchRecordId);
    }
}
