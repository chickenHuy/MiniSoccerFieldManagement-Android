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
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceUsage;

public class ServiceUsageDAOImpl implements IServiceUsageDAO{
    DBHandler dbHandler;
    public ServiceUsageDAOImpl(Context context) {
        dbHandler = new DBHandler(context);
    }

    @Override
    public Boolean add(ServiceUsage serviceUsage) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID, serviceUsage.getId());
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_MATCH_RECORD_ID, serviceUsage.getMatchRecordId());
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CUSTOMER_ID, serviceUsage.getCustomerId());
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_NOTE, serviceUsage.getNote());
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED, 0);
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CREATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.insert(SoccerFieldContract.ServiceUsageEntry.TABLE_NAME, null, values);
            db.close();

            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean update(ServiceUsage serviceUsage) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID, serviceUsage.getId());
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_MATCH_RECORD_ID, serviceUsage.getMatchRecordId());
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CUSTOMER_ID, serviceUsage.getCustomerId());
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_NOTE, serviceUsage.getNote());
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED, 0);
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            int result = db.update(SoccerFieldContract.ServiceUsageEntry.TABLE_NAME, values, SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID + " = ? ", new String[] {serviceUsage.getId()});
            db.close();

            return result != -1;
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
            values.put(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED, 1);

            int result = db.update(SoccerFieldContract.ServiceUsageEntry.TABLE_NAME, values, SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID + " = ? ", new String[] {id});
            db.close();

            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ServiceUsage findById(String id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        ServiceUsage serviceUsage = null;
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_MATCH_RECORD_ID,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CUSTOMER_ID,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_NOTE,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID + " = ? AND " + SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { id, "0" };

            cursor = db.query(
                    SoccerFieldContract.ServiceUsageEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                serviceUsage = new ServiceUsage();
                serviceUsage.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID)));
                serviceUsage.setMatchRecordId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_MATCH_RECORD_ID)));
                serviceUsage.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CUSTOMER_ID)));
                serviceUsage.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_NOTE)));
                serviceUsage.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED)) == 1);
                serviceUsage.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CREATED_AT))));
                serviceUsage.setUpdatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_UPDATED_AT))));
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceUsage;
    }

    @Override
    public ServiceUsage findByMatchRecord(String matchRecordId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        ServiceUsage serviceUsage = null;
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_MATCH_RECORD_ID,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CUSTOMER_ID,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_NOTE,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_MATCH_RECORD_ID + " = ? AND " + SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { matchRecordId, "0" };

            cursor = db.query(
                    SoccerFieldContract.ServiceUsageEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                serviceUsage = new ServiceUsage();
                serviceUsage.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID)));
                serviceUsage.setMatchRecordId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_MATCH_RECORD_ID)));
                serviceUsage.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CUSTOMER_ID)));
                serviceUsage.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_NOTE)));
                serviceUsage.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED)) == 1);
                serviceUsage.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CREATED_AT))));
                serviceUsage.setUpdatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_UPDATED_AT))));
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceUsage;
    }

    @Override
    public List<ServiceUsage> findByCustomer(String customerId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<ServiceUsage> listServiceUsages = new ArrayList<>();
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_MATCH_RECORD_ID,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CUSTOMER_ID,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_NOTE,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CUSTOMER_ID + " = ? AND " + SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { customerId, "0" };

            cursor = db.query(
                    SoccerFieldContract.ServiceUsageEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToFirst()) {
                ServiceUsage serviceUsage = new ServiceUsage();
                serviceUsage.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID)));
                serviceUsage.setMatchRecordId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_MATCH_RECORD_ID)));
                serviceUsage.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CUSTOMER_ID)));
                serviceUsage.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_NOTE)));
                serviceUsage.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_IS_DELETED)) == 1);
                serviceUsage.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CREATED_AT))));
                serviceUsage.setUpdatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_UPDATED_AT))));

                listServiceUsages.add(serviceUsage);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listServiceUsages;
    }
}
