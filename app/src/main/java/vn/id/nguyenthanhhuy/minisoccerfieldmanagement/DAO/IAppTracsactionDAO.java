package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.util.Date;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.AppTransaction;

public interface IAppTracsactionDAO {
    Boolean add (AppTransaction appTransaction);
    Boolean update (AppTransaction appTransaction);
    Boolean softDelete (String id);
    AppTransaction findById (String id);
    List <AppTransaction> findByUser (String userId);
    AppTransaction findByServiceUsage (String serviceUsageId);
    void findByDate (Date date);
    void findByCustomer (String customerId);
    void findByFieldId (String fieldId);
}
