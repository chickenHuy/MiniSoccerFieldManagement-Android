package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.AppTransaction;

public interface IAppTransactionService {
    Boolean add (AppTransaction appTransaction);
    Boolean update (AppTransaction appTransaction);
    Boolean softDelete (String id);
    AppTransaction findById (String id);
    List<AppTransaction> findAll();
    List<AppTransaction> findByUser (String userId);
    List <AppTransaction> findByServiceUsage (String serviceUsageId);
    List<AppTransaction> findByDate (Timestamp date);
    List<AppTransaction> findByCustomer (String customerId);
    List<AppTransaction> findByFieldId (String fieldId);
    String getUserNameByUserID(String userID);
}
