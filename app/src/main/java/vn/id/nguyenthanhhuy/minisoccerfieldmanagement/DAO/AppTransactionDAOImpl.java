package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.contract.SoccerFieldContract;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.AppTransaction;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Customer;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class AppTransactionDAOImpl implements IAppTransactionDAO{
    DBHandler dbHandler;
    public AppTransactionDAOImpl(Context context){dbHandler = new DBHandler(context);}

    @Override
    public Boolean add(AppTransaction appTransaction) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID, appTransaction.getId());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID, appTransaction.getUserID());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID, appTransaction.getServiceUsageId());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE, appTransaction.getType());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT, appTransaction.getTotalAmount().doubleValue());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE, appTransaction.getAdditionalFee().doubleValue());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT, appTransaction.getDiscountAmount().doubleValue());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT, appTransaction.getFinalAmount().doubleValue());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED, 0);
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.insert(SoccerFieldContract.AppTransactionEntry.TABLE_NAME, null, values);
            db.close();

            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean update(AppTransaction appTransaction) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID, appTransaction.getUserID());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID, appTransaction.getServiceUsageId());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE, appTransaction.getType());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT, appTransaction.getTotalAmount().doubleValue());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE, appTransaction.getAdditionalFee().doubleValue());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT, appTransaction.getDiscountAmount().doubleValue());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT, appTransaction.getFinalAmount().doubleValue());
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            int result = db.update(SoccerFieldContract.AppTransactionEntry.TABLE_NAME, values, SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID + " = ? ", new String[] {appTransaction.getId()});
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
            values.put(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED, 1);

            int result = db.update(SoccerFieldContract.AppTransactionEntry.TABLE_NAME, values, SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID + " = ? ", new String[] {id});
            db.close();

            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public AppTransaction findById(String id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        AppTransaction appTransaction = null;
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID + " = ? AND " + SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { id, "0" };

            cursor = db.query(
                    SoccerFieldContract.AppTransactionEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                appTransaction = new AppTransaction();
                appTransaction.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID)));
                appTransaction.setUserID(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID)));
                appTransaction.setServiceUsageId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID)));
                appTransaction.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE)));
                appTransaction.setTotalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT))));
                appTransaction.setAdditionalFee(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE))));
                appTransaction.setDiscountAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT))));
                appTransaction.setFinalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT))));
                appTransaction.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED)) == 1);
                appTransaction.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT))));
                appTransaction.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return appTransaction;
    }

    @Override
    public List<AppTransaction> findAll() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<AppTransaction> listAppTransactions = new ArrayList<>();
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgument = {"0"};

            cursor = db.query(
                    SoccerFieldContract.AppTransactionEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgument,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                AppTransaction appTransaction = new AppTransaction();
                appTransaction.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID)));
                appTransaction.setUserID(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID)));
                appTransaction.setServiceUsageId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID)));
                appTransaction.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE)));
                appTransaction.setTotalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT))));
                appTransaction.setAdditionalFee(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE))));
                appTransaction.setDiscountAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT))));
                appTransaction.setFinalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT))));
                appTransaction.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED)) == 1);
                appTransaction.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT))));
                appTransaction.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT))));
                listAppTransactions.add(appTransaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listAppTransactions;
    }

    @Override
    public List<AppTransaction> findByUser(String userId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<AppTransaction> listAppTransactions = new ArrayList<>();
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID + " = ?";
            String[] selectionArgument = {userId};

            cursor = db.query(
                    SoccerFieldContract.AppTransactionEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgument,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                AppTransaction appTransaction = new AppTransaction();
                appTransaction.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID)));
                appTransaction.setUserID(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID)));
                appTransaction.setServiceUsageId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID)));
                appTransaction.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE)));
                appTransaction.setTotalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT))));
                appTransaction.setAdditionalFee(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE))));
                appTransaction.setDiscountAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT))));
                appTransaction.setFinalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT))));
                appTransaction.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED)) == 1);
                appTransaction.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT))));
                appTransaction.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT))));
                listAppTransactions.add(appTransaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listAppTransactions;
    }

    @Override
    public List<AppTransaction> findByServiceUsage(String serviceUsageId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<AppTransaction> listAppTransactions = new ArrayList<>();
        Cursor cursor = null;

        try{
            String[] projection = {
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID + " = ?";
            String[] selectionArgument = {serviceUsageId};

            cursor = db.query(
                    SoccerFieldContract.AppTransactionEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgument,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                AppTransaction appTransaction = new AppTransaction();
                appTransaction.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID)));
                appTransaction.setUserID(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID)));
                appTransaction.setServiceUsageId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID)));
                appTransaction.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE)));
                appTransaction.setTotalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT))));
                appTransaction.setAdditionalFee(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE))));
                appTransaction.setDiscountAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT))));
                appTransaction.setFinalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT))));
                appTransaction.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED)) == 1);
                appTransaction.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT))));
                appTransaction.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT))));
                listAppTransactions.add(appTransaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listAppTransactions;
    }

    @Override
    public List<AppTransaction> findByDate(Timestamp date) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<AppTransaction> listAppTransactions = new ArrayList<>();
        Cursor cursor = null;

        try {
            String[] projection = {
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = "Date(" + SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT + ") = ? AND " + SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { date.toString(), "0"};

            cursor = db.query(
                    SoccerFieldContract.AppTransactionEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                AppTransaction appTransaction = new AppTransaction();
                appTransaction.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID)));
                appTransaction.setUserID(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID)));
                appTransaction.setServiceUsageId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID)));
                appTransaction.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE)));
                appTransaction.setTotalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT))));
                appTransaction.setAdditionalFee(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE))));
                appTransaction.setDiscountAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT))));
                appTransaction.setFinalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT))));
                appTransaction.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED)) == 1);
                appTransaction.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT))));
                appTransaction.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT))));
                listAppTransactions.add(appTransaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listAppTransactions;
    }

    @Override
    public List<AppTransaction> findByCustomer(String customerId) {
        return null;
    }

    @Override
    public List<AppTransaction> findByFieldId(String fieldId) {
        return null;
    }

    @Override
    public String getUserNameByUserID(String userID){
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String userName = "";
        Cursor cursor = null;

        try {
            String[] projection = {SoccerFieldContract.UserEntry.COLUMN_NAME_NAME};
            String selection = SoccerFieldContract.UserEntry.COLUMN_NAME_ID + " = ?";
            String[] selectionArgs = {userID};

            cursor = db.query(
                    SoccerFieldContract.UserEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                userName = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userName;
    }

    @Override
    public Customer getCustomerByServiceUsageId(String serviceUsageId) {
        Customer customer = null;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + SoccerFieldContract.ServiceUsageEntry.TABLE_NAME +
                    " INNER JOIN " + SoccerFieldContract.CustomerEntry.TABLE_NAME +
                    " ON " + SoccerFieldContract.ServiceUsageEntry.TABLE_NAME + "." + SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_CUSTOMER_ID +
                    " = " + SoccerFieldContract.CustomerEntry.TABLE_NAME + "." + SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID +
                    " WHERE " + SoccerFieldContract.ServiceUsageEntry.TABLE_NAME + "." + SoccerFieldContract.ServiceUsageEntry.COLUMN_NAME_ID +
                    " = ?";

            cursor = db.rawQuery(query, new String[]{serviceUsageId});

            if (cursor != null && cursor.moveToFirst()) {
                String customerId = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_PHONE_NUMBER));
                String membershipId = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.CustomerEntry.COLUMN_NAME_MEMBERSHIP_ID));
                customer = new Customer(customerId, membershipId, name, phoneNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return customer;
    }
    @Override
    public String getNameOfField(String appTransactionId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = null;
        String fieldName = "";

        try {
            String query = "SELECT Field.name FROM AppTransaction " +
                    "INNER JOIN ServiceUsage ON AppTransaction.serviceUsageId = ServiceUsage.id " +
                    "INNER JOIN MatchRecord ON ServiceUsage.matchRecordId = MatchRecord.id " +
                    "INNER JOIN Booking ON MatchRecord.bookingId = Booking.id " +
                    "INNER JOIN Field ON Booking.fieldId = Field.id " +
                    "WHERE AppTransaction.id = ?";

            cursor = db.rawQuery(query, new String[]{appTransactionId});

            if (cursor != null && cursor.moveToFirst()) {
                fieldName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return fieldName;
    }
    @Override
    public Booking getBookingDetails(String appTransactionId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = null;
        Booking booking = null;

        try {
            String query = "SELECT Booking.id, Booking.timeStart, Booking.timeEnd, Booking.price " +
                    "FROM Booking " +
                    "INNER JOIN MatchRecord ON Booking.id = MatchRecord.bookingId " +
                    "INNER JOIN ServiceUsage ON MatchRecord.id = ServiceUsage.matchRecordId " +
                    "INNER JOIN AppTransaction ON ServiceUsage.id = AppTransaction.serviceUsageId " +
                    "WHERE AppTransaction.id = ?";
            cursor = db.rawQuery(query, new String[]{appTransactionId});

            if (cursor != null && cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                Timestamp timeStart = Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("timeStart")));
                Timestamp timeEnd = Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("timeEnd")));
                BigDecimal price = new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow("price")));
                booking = new Booking(id, null, null, null, null, null, timeStart, timeEnd, price, null, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return booking;
    }

    public List<AppTransaction> searchList (String searchParam){
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<AppTransaction> listAppTransactions = new ArrayList<>();
        Cursor cursor = null;

        try{
            String sql = "SELECT AppTransaction.* " +
                    "FROM AppTransaction " +
                    "JOIN ServiceUsage ON AppTransaction.serviceUsageId = ServiceUsage.id " +
                    "JOIN Customer ON ServiceUsage.customerId = Customer.id " +
                    "JOIN User ON AppTransaction.userId = User.id " +
                    "WHERE LOWER(User.userName) LIKE ?";

            cursor = db.rawQuery(sql, new String[]{
                    "%" + searchParam + "%"
            });

            while (cursor.moveToNext()) {
                AppTransaction appTransaction = new AppTransaction();
                appTransaction.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ID)));
                appTransaction.setUserID(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_USER_ID)));
                appTransaction.setServiceUsageId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_SERVICE_USAGE_ID)));
                appTransaction.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TYPE)));
                appTransaction.setTotalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_TOTAL_AMOUNT))));
                appTransaction.setAdditionalFee(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_ADDITIONAL_FEE))));
                appTransaction.setDiscountAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_DISCOUNT_AMOUNT))));
                appTransaction.setFinalAmount(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_FINAL_AMOUNT))));
                appTransaction.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_IS_DELETED)) == 1);
                appTransaction.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_CREATED_AT))));
                appTransaction.setUpdatedAt(Utils.toTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.AppTransactionEntry.COLUMN_NAME_UPDATED_AT))));
                listAppTransactions.add(appTransaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listAppTransactions;
    }
}
