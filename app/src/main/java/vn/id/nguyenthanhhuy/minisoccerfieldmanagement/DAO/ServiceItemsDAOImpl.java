package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.contract.SoccerFieldContract;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceItems;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class ServiceItemsDAOImpl implements IServiceItemsDAO {
    private DBHandler dbHandler;

    public ServiceItemsDAOImpl(Context context) {
        this.dbHandler = new DBHandler(context);
    }

    @Override
    public boolean add(ServiceItems serviceItems) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_ID, serviceItems.getId());
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_USAGE_ID, serviceItems.getServiceUsageId());
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_ID, serviceItems.getServiceId());
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_QUANTITY, serviceItems.getQuantity());
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_CREATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.insert(SoccerFieldContract.ServiceItemsEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(ServiceItems serviceItems) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_USAGE_ID, serviceItems.getServiceUsageId());
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_ID, serviceItems.getServiceId());
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_QUANTITY, serviceItems.getQuantity());
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_CREATED_AT, new Timestamp(System.currentTimeMillis()).toString());
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.update(SoccerFieldContract.ServiceItemsEntry.TABLE_NAME, values, SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_ID + " = ?", new String[]{serviceItems.getId()});
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateQty(String id, int quantity) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_QUANTITY, quantity);

            long result = db.update(SoccerFieldContract.ServiceItemsEntry.TABLE_NAME, values, SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_ID + " = ?", new String[]{id});
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
            values.put(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_IS_DELETED, 1);

            long result = db.update(SoccerFieldContract.ServiceItemsEntry.TABLE_NAME, values, SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_ID + " = ?", new String[]{id});
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ServiceItems findById(String id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        ServiceItems serviceItems = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_USAGE_ID,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_ID,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_QUANTITY,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_ID + " = ? AND " + SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_IS_DELETED + " = ?";
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
                serviceItems = new ServiceItems();
                serviceItems.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_ID)));
                serviceItems.setServiceUsageId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_USAGE_ID)));
                serviceItems.setServiceId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_ID)));
                serviceItems.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_QUANTITY)));
                serviceItems.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_CREATED_AT))));
                serviceItems.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_UPDATED_AT))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceItems;
    }

    @Override
    public List<ServiceItems> findByServiceUsage(String serviceUsageId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<ServiceItems> listServiceItems = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_USAGE_ID,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_ID,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_QUANTITY,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_USAGE_ID + " = ?";
            String[] selectionArgument = {serviceUsageId};

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
                ServiceItems serviceItems = new ServiceItems();
                serviceItems.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_ID)));
                serviceItems.setServiceUsageId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_USAGE_ID)));
                serviceItems.setServiceId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_ID)));
                serviceItems.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_QUANTITY)));
                serviceItems.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_CREATED_AT))));
                serviceItems.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_UPDATED_AT))));

                listServiceItems.add(serviceItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listServiceItems;
    }

    @Override
    public List<ServiceItems> findByService(String serviceId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<ServiceItems> listServiceItems = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_USAGE_ID,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_ID,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_QUANTITY,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_ID + " = ?";
            String[] selectionArgument = {serviceId};

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
                ServiceItems serviceItems = new ServiceItems();
                serviceItems.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_ID)));
                serviceItems.setServiceUsageId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_USAGE_ID)));
                serviceItems.setServiceId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_SERVICE_ID)));
                serviceItems.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_QUANTITY)));
                serviceItems.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_CREATED_AT))));
                serviceItems.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceItemsEntry.COLUMN_NAME_UPDATED_AT))));

                listServiceItems.add(serviceItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listServiceItems;
    }
}
