package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.FieldDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IFieldDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;

public class FieldServiceImpl implements IFieldService{
    IFieldDAO fieldDAO;
    Context context;;
    public FieldServiceImpl(Context context) {
        this.context = context;
        fieldDAO = new FieldDAOImpl(context);
    }
    @Override
    public Boolean add(Field field) {
        return fieldDAO.add(field);
    }

    @Override
    public Boolean update(Field field) {
        return fieldDAO.update(field);
    }

    @Override
    public Boolean softDelete(String id) {
        return softDelete(id);
    }

    @Override
    public List<Field> findAllCombinedField() {
        return fieldDAO.findAllCombinedField();
    }

    @Override
    public List<Field> findAll() {
        return fieldDAO.findAll();
    }

    @Override
    public List<Field> findAllNormalField() {
        return fieldDAO.findAllNormalField();
    }

    @Override
    public Field findById(String id) {
        return fieldDAO.findById(id);
    }

    @Override
    public List<Field> findByStatus(String status) {
        return fieldDAO.findByStatus(status);
    }
}
