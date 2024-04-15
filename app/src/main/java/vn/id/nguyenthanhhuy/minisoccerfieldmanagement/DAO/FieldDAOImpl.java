package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.contract.SoccerFieldContract;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Field;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.StaticString;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class FieldDAOImpl implements IFieldDAO{
    DBHandler dbHandler;
    public FieldDAOImpl(Context context){dbHandler = new DBHandler(context);}

    @Override
    public Boolean add(Field field) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_ID, field.getId());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME, field.getName());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS, field.getStatus());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD, field.getType());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE, field.getImage());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1, field.getCombineField1());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2, field.getCombineField2());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3, field.getCombineField3());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED, 0);
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.insert(SoccerFieldContract.FieldEntry.TABLE_NAME, null, values);
            db.close();

            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean update(Field field) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_ID, field.getId());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME, field.getName());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS, field.getStatus());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD, field.getType());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE, field.getImage());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1, field.getCombineField1());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2, field.getCombineField2());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3, field.getCombineField3());
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            int result = db.update(SoccerFieldContract.FieldEntry.TABLE_NAME, values, SoccerFieldContract.FieldEntry.COLUMN_NAME_ID + " = ? ", new String[] {field.getId()});
            db.close();

            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean softDelete(String id) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED, 1);

            int result = db.update(SoccerFieldContract.FieldEntry.TABLE_NAME, values, SoccerFieldContract.FieldEntry.COLUMN_NAME_ID + " = ? ", new String[] {id});
            db.close();

            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Field> findAllCombinedField() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Field> listField = new ArrayList<>();
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3   ,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD + " = ? AND " + SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = {StaticString.TYPE_7_A_SIDE, "0"};

            cursor = db.query(
                    SoccerFieldContract.FieldEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Field field = new Field();
                field.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_ID)));
                field.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME)));
                field.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS)));
                field.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD)));
                field.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE)));
                field.setCombineField1(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1)));
                field.setCombineField2(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2)));
                field.setCombineField3(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3)));
                field.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED)) == 1);
                field.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT))));
                field.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT))));

                listField.add(field);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listField;
    }

    @Override
    public List<Field> findAll() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Field> listField = new ArrayList<>();
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3   ,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = {"0"};

            cursor = db.query(
                    SoccerFieldContract.FieldEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Field field = new Field();
                field.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_ID)));
                field.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME)));
                field.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS)));
                field.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD)));
                field.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE)));
                field.setCombineField1(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1)));
                field.setCombineField2(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2)));
                field.setCombineField3(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3)));
                field.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED)) == 1);
                field.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT))));
                field.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT))));

                listField.add(field);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listField;
    }

    @Override
    public List<Field> findAllNormalField() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Field> listField = new ArrayList<>();
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3   ,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD + " = ? AND " + SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = {StaticString.TYPE_5_A_SIDE, "0"};

            cursor = db.query(
                    SoccerFieldContract.FieldEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Field field = new Field();
                field.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_ID)));
                field.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME)));
                field.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS)));
                field.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD)));
                field.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE)));
                field.setCombineField1(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1)));
                field.setCombineField2(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2)));
                field.setCombineField3(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3)));
                field.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED)) == 1);
                field.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT))));
                field.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT))));

                listField.add(field);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listField;
    }

    @Override
    public Field findById(String id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Field field = null;
        Cursor cursor = null;

        try {
            String[] projection = {
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.FieldEntry.COLUMN_NAME_ID + " = ? AND " + SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = {id, "0"};

            cursor = db.query(
                    SoccerFieldContract.FieldEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.moveToNext()) {
                field = new Field();
                field.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_ID)));
                field.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME)));
                field.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS)));
                field.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD)));
                field.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE)));
                field.setCombineField1(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1)));
                field.setCombineField2(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2)));
                field.setCombineField3(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3)));
                field.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED)) == 1);
                field.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT))));
                field.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT))));
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return field;
    }

    @Override
    public List<Field> findByStatus(String status) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Field> listField = new ArrayList<>();
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS + "= ? AND " + SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgument = {status, "0"};

            cursor = db.query(
                    SoccerFieldContract.FieldEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgument,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Field field = new Field();
                field.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_ID)));
                field.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME)));
                field.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS)));
                field.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD)));
                field.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE)));
                field.setCombineField1(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1)));
                field.setCombineField2(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2)));
                field.setCombineField3(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3)));
                field.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED)) == 1);
                field.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT))));
                field.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT))));

                listField.add(field);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listField;
    }

    @Override
    public List<Field> findAllActiveField() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Field> listField = new ArrayList<>();
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3   ,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED + " = ? AND status = 'active'";
            String[] selectionArgs = {"0"};

            cursor = db.query(
                    SoccerFieldContract.FieldEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Field field = new Field();
                field.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_ID)));
                field.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_FIELD_NAME)));
                field.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_STATUS)));
                field.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_TYPE_FIELD)));
                field.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IMAGE)));
                field.setCombineField1(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD1)));
                field.setCombineField2(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD2)));
                field.setCombineField3(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_COMBINE_FIELD3)));
                field.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_IS_DELETED)) == 1);
                field.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_CREATED_AT))));
                field.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.FieldEntry.COLUMN_NAME_UPDATED_AT))));

                listField.add(field);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listField;
    }
}
