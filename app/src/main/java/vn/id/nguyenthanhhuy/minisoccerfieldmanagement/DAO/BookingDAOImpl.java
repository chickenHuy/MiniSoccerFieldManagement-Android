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
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class BookingDAOImpl implements IBookingDAO{
    DBHandler dbHandler;
    public BookingDAOImpl(Context context) {
        dbHandler = new DBHandler(context);
    }
    @Override
    public Boolean add(Booking booking) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_ID, booking.getId());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID, booking.getCustomerId());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID, booking.getUserId());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID, booking.getFieldId());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS, booking.getStatus());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE, booking.getNote());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START, booking.getTimeStart().toString());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END, booking.getTimeEnd().toString());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE, booking.getPrice().doubleValue());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED, 0);
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT, String.valueOf(new Timestamp(System.currentTimeMillis())));


            long newRowId = db.insert(SoccerFieldContract.BookingEntry.TABLE_NAME, null, values);

            return newRowId != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateStatus(String id, String status) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS, status);
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            String selection = SoccerFieldContract.BookingEntry.COLUMN_NAME_ID + " = ?";
            String[] selectionArgs = { id };

            int count = db.update(
                    SoccerFieldContract.BookingEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean update(Booking booking) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID, booking.getCustomerId());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID, booking.getUserId());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID, booking.getFieldId());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS, booking.getStatus());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE, booking.getNote());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START, booking.getTimeStart().toString());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END, booking.getTimeEnd().toString());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE, booking.getPrice().doubleValue());
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT, String.valueOf(new Timestamp(System.currentTimeMillis())));

            int result = db.update(SoccerFieldContract.BookingEntry.TABLE_NAME, values, SoccerFieldContract.FieldEntry.COLUMN_NAME_ID + " = ? ", new String[] {booking.getId()});
            db.close();

            return result > 0 ;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean softDelete(String id) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED, 1);
            values.put(SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            String selection = SoccerFieldContract.BookingEntry.COLUMN_NAME_ID + " = ?";
            String[] selectionArgs = { id };

            int count = db.update(
                    SoccerFieldContract.BookingEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Booking findById(String id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Booking booking = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.BookingEntry.COLUMN_NAME_ID + " = ? AND " + SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { id, "0" };

            cursor = db.query(
                    SoccerFieldContract.BookingEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                booking = new Booking();
                booking.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_ID)));
                booking.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID)));
                booking.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID)));
                booking.setFieldId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID)));
                booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS)));
                booking.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE)));
                booking.setTimeStart(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START))));
                booking.setTimeEnd(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END))));
                booking.setPrice(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE))));
                booking.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED)) == 1);
                booking.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT))));
                String updateAt = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT));
                booking.setUpdatedAt(Utils.toTimestamp(updateAt));
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

    @Override
    public List<Booking> findByCustomer(String customerId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Booking> bookings = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID + " = ? AND " + SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { customerId, "0" };

            cursor = db.query(
                    SoccerFieldContract.BookingEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Booking booking = new Booking();
                booking.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_ID)));
                booking.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID)));
                booking.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID)));
                booking.setFieldId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID)));
                booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS)));
                booking.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE)));
                booking.setTimeStart(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START))));
                booking.setTimeEnd(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END))));
                booking.setPrice(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE))));
                booking.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED)) == 1);
                booking.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT))));
                String updateAt = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT));
                booking.setUpdatedAt(Utils.toTimestamp(updateAt));
                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return bookings;
    }

    @Override
    public List<Booking> findByUser(String userId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Booking> bookings = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID + " = ? AND " + SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { userId, "0" };

            cursor = db.query(
                    SoccerFieldContract.BookingEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Booking booking = new Booking();
                booking.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_ID)));
                booking.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID)));
                booking.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID)));
                booking.setFieldId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID)));
                booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS)));
                booking.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE)));
                booking.setTimeStart(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START))));
                booking.setTimeEnd(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END))));
                booking.setPrice(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE))));
                booking.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED)) == 1);
                booking.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT))));
                String updateAt = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT));
                booking.setUpdatedAt(Utils.toTimestamp(updateAt));
                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return bookings;
    }

    @Override
    public List<Booking> findByField(String fieldId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Booking> bookings = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID + " = ? AND " + SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { fieldId, "0" };

            cursor = db.query(
                    SoccerFieldContract.BookingEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Booking booking = new Booking();
                booking.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_ID)));
                booking.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID)));
                booking.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID)));
                booking.setFieldId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID)));
                booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS)));
                booking.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE)));
                booking.setTimeStart(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START))));
                booking.setTimeEnd(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END))));
                booking.setPrice(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE))));
                booking.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED)) == 1);
                booking.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT))));
                String updateAt = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT));
                booking.setUpdatedAt(Utils.toTimestamp(updateAt));
                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return bookings;
    }

    @Override
    public List<Booking> findByStatus(String status) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Booking> bookings = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS + " = ? AND " + SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { status, "0" };

            cursor = db.query(
                    SoccerFieldContract.BookingEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Booking booking = new Booking();
                booking.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_ID)));
                booking.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID)));
                booking.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID)));
                booking.setFieldId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID)));
                booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS)));
                booking.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE)));
                booking.setTimeStart(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START))));
                booking.setTimeEnd(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END))));
                booking.setPrice(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE))));
                booking.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED)) == 1);
                booking.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT))));
                String updateAt = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT));
                booking.setUpdatedAt(Utils.toTimestamp(updateAt));
                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return bookings;
    }

    @Override
    public List<Booking> findByDate(Timestamp date) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Booking> bookings = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = "DATE(" + SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START + ") = DATE(?) AND " + SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { date.toString(), "0" };

            cursor = db.query(
                    SoccerFieldContract.BookingEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Booking booking = new Booking();
                booking.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_ID)));
                booking.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID)));
                booking.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID)));
                booking.setFieldId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID)));
                booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS)));
                booking.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE)));
                booking.setTimeStart(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START))));
                booking.setTimeEnd(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END))));
                booking.setPrice(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE))));
                booking.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED)) == 1);
                booking.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT))));
                String updateAt = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT));
                booking.setUpdatedAt(Utils.toTimestamp(updateAt));
                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return bookings;
    }

    @Override
    public List<Booking> findByDateAndField(Timestamp date, String fieldId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Booking> bookings = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = "DATE(" + SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START + ") = DATE(?) AND " + SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID + " = ? AND " + SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { date.toString(), fieldId, "0" };

            cursor = db.query(
                    SoccerFieldContract.BookingEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                Booking booking = new Booking();
                booking.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_ID)));
                booking.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CUSTOMER_ID)));
                booking.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_USER_ID)));
                booking.setFieldId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_FIELD_ID)));
                booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_STATUS)));
                booking.setNote(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_NOTE)));
                booking.setTimeStart(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_START))));
                booking.setTimeEnd(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_TIME_END))));
                booking.setPrice(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_PRICE))));
                booking.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_IS_DELETED)) == 1);
                booking.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_CREATED_AT))));
                String updateAt = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.BookingEntry.COLUMN_NAME_UPDATED_AT));
                booking.setUpdatedAt(Utils.toTimestamp(updateAt));
                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return bookings;
    }
}
