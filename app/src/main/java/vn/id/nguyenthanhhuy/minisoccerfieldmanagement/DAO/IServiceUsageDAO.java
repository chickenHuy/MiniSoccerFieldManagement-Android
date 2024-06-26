package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceUsage;

public interface IServiceUsageDAO {
    Boolean add (ServiceUsage serviceUsage);
    Boolean update (ServiceUsage serviceUsage);
    Boolean softDelete (String id);
    ServiceUsage findById (String id);
    ServiceUsage findByMatchRecord (String matchRecordId);
    List <ServiceUsage> findByCustomer (String customerId);
    double getTotalServicePriceByMatchId (String matchRecordId);
}
