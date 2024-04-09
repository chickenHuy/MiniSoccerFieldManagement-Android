package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;

public interface IFieldService {
    Boolean add (Field field);
    Boolean update (Field field);
    Boolean softDelete (String id);
    List<Field> findAllCombinedField();
    List<Field> findAll();
    List<Field> findAllNormalField();
    Field findById (String id);
    List<Field> findByStatus (String status);

    List<Field> findParent(String id);
}
