package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.contract.SoccerFieldContract;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.MatchRecord;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.PriceList;

public class MatchRecordDAOImpl implements IMatchRecordDAO{
    DBHandler dbHandler;
    public MatchRecordDAOImpl(Context context) {
        dbHandler = new DBHandler(context);
    }
    @Override
    public Boolean checkIn(MatchRecord matchRecord) {

        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_ID, matchRecord.getId());
            values.put(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_BOOKING_ID, matchRecord.getBookingId());
            values.put(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_IN, matchRecord.getCheckIn().toString());
            values.put(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_OUT, matchRecord.getCheckOut().toString());
            values.put(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_IS_DELETED, 0);
            values.put(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CREATED_AT, matchRecord.getCreatedAt().toString());

            long result = db.insert(SoccerFieldContract.MatchRecordEntry.TABLE_NAME, null, values);
            db.close();

            // if insert is successful, result will be the row ID of the new row, otherwise it will be -1
            return result != -1;
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
            return false;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public Boolean checkOut(String id) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_OUT, new Timestamp(System.currentTimeMillis()).toString());
            values.put(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            int result = db.update(SoccerFieldContract.MatchRecordEntry.TABLE_NAME, values, SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_ID + " = ? ", new String[] {id});
            db.close();

            // if insert is successful, result will be the row ID of the new row, otherwise it will be -1
            return result != -1;
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean softDelete(String id) {

        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_IS_DELETED, 1);

            int result = db.update(SoccerFieldContract.MatchRecordEntry.TABLE_NAME, values, SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_ID + " = ? ", new String[] {id});
            db.close();

            // if update is successful, result will be the number of rows affected, otherwise it will be 0
            return result > 0;
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public MatchRecord findById(String id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        MatchRecord matchRecord = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_BOOKING_ID,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_IN,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_OUT,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_ID + " = ? AND " + SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { id, "0" };

            cursor = db.query(
                    SoccerFieldContract.MatchRecordEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                matchRecord = new MatchRecord();
                matchRecord.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_ID)));
                matchRecord.setBookingId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_BOOKING_ID)));
                matchRecord.setCheckIn(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_IN))));
                matchRecord.setCheckOut(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_OUT))));
                matchRecord.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_IS_DELETED)) == 1);
                matchRecord.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CREATED_AT))));
                matchRecord.setUpdatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_UPDATED_AT))));
            }
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return matchRecord;
    }

    @Override
    public List<MatchRecord> findByBooking(String bookingId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<MatchRecord> matchRecords = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_BOOKING_ID,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_IN,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_OUT,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_BOOKING_ID + " = ? AND " + SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { bookingId, "0" };

            cursor = db.query(
                    SoccerFieldContract.MatchRecordEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                MatchRecord matchRecord = new MatchRecord();
                matchRecord.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_ID)));
                matchRecord.setBookingId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_BOOKING_ID)));
                matchRecord.setCheckIn(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_IN))));
                matchRecord.setCheckOut(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_OUT))));
                matchRecord.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_IS_DELETED)) == 1);
                matchRecord.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CREATED_AT))));
                matchRecord.setUpdatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_UPDATED_AT))));
                matchRecords.add(matchRecord);
            }
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return matchRecords;
    }

    @Override
    public List<MatchRecord> findByDate(Timestamp date) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<MatchRecord> matchRecords = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_BOOKING_ID,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_IN,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_OUT,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = "DATE(" + SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_IN + ") = ? AND " + SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { date.toString(), "0" };

            cursor = db.query(
                    SoccerFieldContract.MatchRecordEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                MatchRecord matchRecord = new MatchRecord();
                matchRecord.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_ID)));
                matchRecord.setBookingId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_BOOKING_ID)));
                matchRecord.setCheckIn(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_IN))));
                matchRecord.setCheckOut(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CHECK_OUT))));
                matchRecord.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_IS_DELETED)) == 1);
                matchRecord.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_CREATED_AT))));
                matchRecord.setUpdatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MatchRecordEntry.COLUMN_NAME_UPDATED_AT))));
                matchRecords.add(matchRecord);
            }
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return matchRecords;
    }

    @Override
    public List<MatchRecord> findByField(String fieldId) {
        throw new UnsupportedOperationException("Hàm này chưa được khởi tạo.");
    }

    @Override
    public List<MatchRecord> findByDateAndField(Timestamp date, String fieldId) {
        throw new UnsupportedOperationException("Hàm này chưa được khởi tạo.");
    }
}
