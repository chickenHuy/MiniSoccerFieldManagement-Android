package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.contract.SoccerFieldContract;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class ServiceDAOImpl implements IServiceDAO {
    DBHandler dbHandler;

    public ServiceDAOImpl(Context context) {
        this.dbHandler = new DBHandler(context);
    }

    @RequiresApi(api = 26)
    @Override
    public boolean add(Service service) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID, service.getId());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_NAME, service.getName());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_PRICE, service.getPrice().doubleValue());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_IMAGE, service.getImage());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_DESCRIPTION, service.getDescription());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_UNIT, service.getUnit());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_QUANTITY, service.getQuantity());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_SOLD, service.getSold());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_STATUS, service.getStatus());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_CREATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.insert(SoccerFieldContract.ServiceEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Service service) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_NAME, service.getName());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_PRICE, service.getPrice().doubleValue());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_IMAGE, service.getImage());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_DESCRIPTION, service.getDescription());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_UNIT, service.getUnit());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_QUANTITY, service.getQuantity());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_SOLD, service.getSold());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_STATUS, service.getStatus());
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.update(SoccerFieldContract.ServiceEntry.TABLE_NAME, values, SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID + " = ?", new String[]{service.getId()});
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean softDelete(String id) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_IS_DELETED, 1);

            long result = db.update(SoccerFieldContract.ServiceEntry.TABLE_NAME, values, SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID + " = ?", new String[]{id});
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Service findById(String id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Service service = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_PRICE,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_DESCRIPTION,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_UNIT,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_QUANTITY,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_SOLD,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID + " = ? AND " + SoccerFieldContract.ServiceEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgument = {id, "0"};

            cursor = db.query(
                    SoccerFieldContract.ServiceEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgument,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                service = new Service();
                service.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID)));
                service.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_NAME)));
                service.setPrice(new BigDecimal(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_PRICE))));
                service.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_IMAGE)));
                service.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_DESCRIPTION)));
                service.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_UNIT)));
                service.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_QUANTITY)));
                service.setSold(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_SOLD)));
                service.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_CREATED_AT))));
                service.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_UPDATED_AT))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return service;
    }

    @Override
    public List<Service> findAll() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Service> services = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_PRICE,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_DESCRIPTION,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_UNIT,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_QUANTITY,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_SOLD,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.ServiceEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgument = {"0"};

            cursor = db.query(
                    SoccerFieldContract.ServiceEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgument,
                    null,
                    null,
                    null
            );

            if (cursor.moveToNext()) {
                Service service = new Service();
                service.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID)));
                service.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_NAME)));
                service.setPrice(new BigDecimal(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_PRICE))));
                service.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_IMAGE)));
                service.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_DESCRIPTION)));
                service.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_UNIT)));
                service.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_QUANTITY)));
                service.setSold(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_SOLD)));
                service.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_CREATED_AT))));
                service.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_UPDATED_AT))));

                services.add(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return services;
    }

    @Override
    public List<Service> findByStatus(String status) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Service> services = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_PRICE,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_DESCRIPTION,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_UNIT,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_QUANTITY,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_SOLD,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.ServiceEntry.COLUMN_NAME_STATUS + "= ?" + SoccerFieldContract.ServiceEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgument = {status, "0"};

            cursor = db.query(
                    SoccerFieldContract.ServiceEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgument,
                    null,
                    null,
                    null
            );

            if (cursor.moveToNext()) {
                Service service = new Service();
                service.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID)));
                service.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_NAME)));
                service.setPrice(new BigDecimal(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_PRICE))));
                service.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_IMAGE)));
                service.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_DESCRIPTION)));
                service.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_UNIT)));
                service.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_QUANTITY)));
                service.setSold(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_SOLD)));
                service.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_CREATED_AT))));
                service.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_UPDATED_AT))));

                services.add(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return services;
    }

    @Override
    public List<Service> findByInfo(String name, String description) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Service> services = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_PRICE,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_DESCRIPTION,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_UNIT,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_QUANTITY,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_SOLD,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.ServiceEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.ServiceEntry.COLUMN_NAME_NAME + " LIKE %?%" + SoccerFieldContract.ServiceEntry.COLUMN_NAME_DESCRIPTION + " LIKE %?%" + SoccerFieldContract.ServiceEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgument = {name, description, "0"};

            cursor = db.query(
                    SoccerFieldContract.ServiceEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgument,
                    null,
                    null,
                    null
            );

            if (cursor.moveToNext()) {
                Service service = new Service();
                service.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID)));
                service.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_NAME)));
                service.setPrice(new BigDecimal(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_PRICE))));
                service.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_IMAGE)));
                service.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_DESCRIPTION)));
                service.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_UNIT)));
                service.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_QUANTITY)));
                service.setSold(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_SOLD)));
                service.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_CREATED_AT))));
                service.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceEntry.COLUMN_NAME_UPDATED_AT))));

                services.add(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return services;
    }

    @Override
    public boolean updateStatus(String id, String status) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_STATUS, status);

            long result = db.update(SoccerFieldContract.ServiceEntry.TABLE_NAME, values, SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID + " = ?", new String[]{id});
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSold(String id, int sold) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_SOLD, sold);

            long result = db.update(SoccerFieldContract.ServiceEntry.TABLE_NAME, values, SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID + " = ?", new String[]{id});
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateQuantity(String id, int quantity) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.ServiceEntry.COLUMN_NAME_QUANTITY, quantity);

            long result = db.update(SoccerFieldContract.ServiceEntry.TABLE_NAME, values, SoccerFieldContract.ServiceEntry.COLUMN_NAME_ID + " = ?", new String[]{id});
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
