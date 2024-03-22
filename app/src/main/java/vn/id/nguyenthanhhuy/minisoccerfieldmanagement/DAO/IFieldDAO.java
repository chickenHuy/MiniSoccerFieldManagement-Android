package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;

public interface IFieldDAO {
    Boolean add (Field field);
    Boolean update (Field field);
    Boolean softDelete (String id);
    void findAllCombinedField(); //check status
    void findAll(); //check status
    void findAllNormalField(); //check status
    void findById (String id);
    List<Field> findByStatus (String status);

}
