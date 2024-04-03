package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.contract.SoccerFieldContract;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class CustomerDAOImpl implements ICustomerDAO{
    DBHandler dbHandler;
    Context context;
    public CustomerDAOImpl(Context context)
    {
        this.dbHandler = new DBHandler(context);
        this.context = context;
    }
    @Override
    public Boolean add(Customer customer) {
       try {
           SQLiteDatabase db = dbHandler.getWritableDatabase();
           ContentValues values = new ContentValues();
           values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID, customer.getId());
           values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID, customer.getMemberShipId());
           values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_NAME, customer.getName());
           values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER, customer.getPhoneNumber());
           values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_TOTAL_SPEND, customer.getTotalSpend().doubleValue());
           values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IMAGE, customer.getImage());
           values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_CREATED_AT, new Timestamp(System.currentTimeMillis()).toString());

           int result = (int) db.insert(SoccerFieldContract.CustomerEntry.TABLE_NAME, null, values);
           db.close();
           return result != -1;
       }
       catch (Exception e)
       {
           e.printStackTrace();
           return false;
       }
    }

    @Override
    public Boolean update(Customer customer) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID, customer.getMemberShipId());
            values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_NAME, customer.getName());
            values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER, customer.getPhoneNumber());
            values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_TOTAL_SPEND, customer.getTotalSpend().doubleValue());
            values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IMAGE, customer.getImage());
            values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED, customer.getDeleted());
            values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            int result = (int) db.update(SoccerFieldContract.CustomerEntry.TABLE_NAME, values, SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID + " = ?", new String[]{customer.getId()});
            db.close();
            return result > 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean softDelete(String id) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED, 1);
            values.put(SoccerFieldContract.CustomerEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            int result = (int) db.update(SoccerFieldContract.CustomerEntry.TABLE_NAME, values, SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID + " = ?", new String[]{id});
            db.close();
            return result > 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Customer findById(String id) {
        Customer customer = null;
        Cursor cursor = null;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        try {
            String[] projection = {
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_TOTAL_SPEND,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID + " = ? AND " + SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED + " = 0 ;";
            String[] selectionArgs = {id};

            cursor = db.query(SoccerFieldContract.CustomerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                customer = new Customer(
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER)),
                        BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_TOTAL_SPEND))),
                        cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IMAGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED)) == 1,
                        Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_CREATED_AT))),
                        Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_UPDATED_AT))
                        ));

            }

        return customer;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    @Override
    public Customer findByPhoneNumber(String phoneNumber) {
        Customer customer = null;
        Cursor cursor = null;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        try {
            String[] projection = {
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_TOTAL_SPEND,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER + " = ? AND " + SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED + " = 0 ;";
            String[] selectionArgs = {phoneNumber};

            cursor = db.query(SoccerFieldContract.CustomerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                customer = new Customer(
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER)),
                        BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_TOTAL_SPEND))),
                        cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IMAGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED)) == 1,
                        Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_CREATED_AT))),
                        Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_UPDATED_AT))
                        ));

            }

            return customer;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<Customer>();
        Cursor cursor = null;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Customer customer = null;
        try {
            String[] projection = {
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_TOTAL_SPEND,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED + " = 0 ;";

            cursor = db.query(SoccerFieldContract.CustomerEntry.TABLE_NAME, projection, selection, null, null, null, null);
            while (cursor.moveToNext()) {
                customer = new Customer(
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER)),
                        BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_TOTAL_SPEND))),
                        cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IMAGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED)) == 1,
                        Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_CREATED_AT))),
                        Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_UPDATED_AT))
                        ));
                customers.add(customer);
            }

            return customers;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    @Override
    public List<Customer> findByMemberShip(String membershipId) {
        List<Customer> customers = new ArrayList<Customer>();
        Cursor cursor = null;
        Customer customer = null;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        try {
            String[] projection = {
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_TOTAL_SPEND,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.CustomerEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID + " = ? AND " + SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED + " = 0 ;";
            String[] selectionArgs = {membershipId};
            cursor = db.query(SoccerFieldContract.CustomerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            while (cursor.moveToNext())
            {
                customer = new Customer(
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER)),
                        BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_TOTAL_SPEND))),
                        cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IMAGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_IS_DELETED)) == 1,
                        Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_CREATED_AT))),
                        Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_UPDATED_AT))
                        ));
                customers.add(customer);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return customers;
    }
}
