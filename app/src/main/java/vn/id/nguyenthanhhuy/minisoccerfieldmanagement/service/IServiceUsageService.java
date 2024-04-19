package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceUsage;

public interface IServiceUsageService {
    Boolean add (ServiceUsage serviceUsage);
    Boolean update (ServiceUsage serviceUsage);
    Boolean softDelete (String id);
    ServiceUsage findById (String id);
    ServiceUsage findByMatchRecord (String matchRecordId);
    List<ServiceUsage> findByCustomer (String customerId);
}
