package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;

public interface IFieldDAO {
    Boolean add (Field field);
    Boolean update (Field field);
    Boolean softDelete (String id);
    List<Field> findAllCombinedField();
    List<Field> findAll();
    List<Field> findAllNormalField();
    Field findById (String id);
    List<Field> findByStatus (String status);
    
}
