package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;

import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.AppTransactionDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IAppTransactionDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.AppTransaction;

public class AppTransactionServiceImpl implements IAppTransactionService{
    IAppTransactionDAO appTransactionDAO;
    Context context;

    public AppTransactionServiceImpl(Context context){
        appTransactionDAO = new AppTransactionDAOImpl(context);
        this.context = context;
    }
    @Override
    public Boolean add(AppTransaction appTransaction) {
        return appTransactionDAO.add(appTransaction);
    }

    @Override
    public Boolean update(AppTransaction appTransaction) {
        return appTransactionDAO.update(appTransaction);
    }

    @Override
    public Boolean softDelete(String id) {
        return appTransactionDAO.softDelete(id);
    }

    @Override
    public AppTransaction findById(String id) {
        return appTransactionDAO.findById(id);
    }

    @Override
    public List<AppTransaction> findAll() {
        return appTransactionDAO.findAll();
    }

    @Override
    public List<AppTransaction> findByUser(String userId) {
        return appTransactionDAO.findByUser(userId);
    }

    @Override
    public List<AppTransaction> findByServiceUsage(String serviceUsageId) {
        return appTransactionDAO.findByServiceUsage(serviceUsageId);
    }

    @Override
    public List<AppTransaction> findByDate(Timestamp date) {
        return appTransactionDAO.findByDate(date);
    }

    @Override
    public List<AppTransaction> findByCustomer(String customerId) {
        return appTransactionDAO.findByCustomer(customerId);
    }

    @Override
    public List<AppTransaction> findByFieldId(String fieldId) {
        return appTransactionDAO.findByFieldId(fieldId);
    }
    @Override
    public String getUserNameByUserID(String userID){
        return appTransactionDAO.getUserNameByUserID(userID);
    }
}
