package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.contract.SoccerFieldContract;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.PriceList;

public class PriceListDAOImpl implements IPriceListDAO{
    DBHandler dbHandler;
    public PriceListDAOImpl(Context context) {
        dbHandler = new DBHandler(context);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Boolean add(PriceList model) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_ID, model.getId());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_START_TIME, model.getStartTime().toString());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_END_TIME, model.getEndTime().toString());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_TYPE_FIELD, model.getTypeField());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_DATE_OF_WEEK, model.getDateOfWeek());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_UNIT_PRICE_PER_30_MINUTES, model.getUnitPricePer30Minutes().doubleValue());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_IS_DELETED, 0);
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_CREATED_AT, new Timestamp(System.currentTimeMillis()).toString());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.insert(SoccerFieldContract.PriceListEntry.TABLE_NAME, null, values);
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
    public Boolean update(PriceList model) {

        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_START_TIME, model.getStartTime().toString());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_END_TIME, model.getEndTime().toString());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_TYPE_FIELD, model.getTypeField());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_DATE_OF_WEEK, model.getDateOfWeek());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_UNIT_PRICE_PER_30_MINUTES, model.getUnitPricePer30Minutes().doubleValue());
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            int result = db.update(SoccerFieldContract.PriceListEntry.TABLE_NAME, values, SoccerFieldContract.PriceListEntry.COLUMN_NAME_ID + " = ? ", new String[] {model.getId()});
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
            values.put(SoccerFieldContract.PriceListEntry.COLUMN_NAME_IS_DELETED, 1);

            int result = db.update(SoccerFieldContract.PriceListEntry.TABLE_NAME, values, SoccerFieldContract.PriceListEntry.COLUMN_NAME_ID + " = ? ", new String[] {id});
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
    public List<PriceList> findByDateOfWeek(String date) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<PriceList> priceList = new ArrayList<PriceList>();
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_START_TIME,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_END_TIME,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_TYPE_FIELD,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_DATE_OF_WEEK,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_UNIT_PRICE_PER_30_MINUTES,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.PriceListEntry.COLUMN_NAME_DATE_OF_WEEK + " = ? AND " + SoccerFieldContract.PriceListEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { date, "0" };

            cursor = db.query(
                    SoccerFieldContract.PriceListEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                PriceList price = new PriceList();
                price.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_ID)));
                price.setStartTime(Time.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_START_TIME))));
                price.setEndTime(Time.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_END_TIME))));
                price.setTypeField(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_TYPE_FIELD)));
                price.setDateOfWeek(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_DATE_OF_WEEK)));
                price.setUnitPricePer30Minutes(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_UNIT_PRICE_PER_30_MINUTES))));
                price.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_IS_DELETED)) == 1);
                price.setCreateAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_CREATED_AT))));
                price.setUpdateAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_UPDATED_AT))));
                priceList.add(price);
            }
        }
        catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return  priceList;
    }

    @Override
    public List<PriceList> findAll() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<PriceList> priceList = new ArrayList<PriceList>();
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_START_TIME,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_END_TIME,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_TYPE_FIELD,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_DATE_OF_WEEK,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_UNIT_PRICE_PER_30_MINUTES,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.PriceListEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = {"0"};

            cursor = db.query(
                    SoccerFieldContract.PriceListEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            while (cursor.moveToNext()) {
                PriceList price = new PriceList();
                price.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_ID)));
                price.setStartTime(Time.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_START_TIME))));
                price.setEndTime(Time.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_END_TIME))));
                price.setTypeField(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_TYPE_FIELD)));
                price.setDateOfWeek(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_DATE_OF_WEEK)));
                price.setUnitPricePer30Minutes(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_UNIT_PRICE_PER_30_MINUTES))));
                price.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_IS_DELETED)) == 1);
                price.setCreateAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_CREATED_AT))));
                price.setUpdateAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_UPDATED_AT))));
                priceList.add(price);
            }
        }
        catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return  priceList;
    }

    @Override
    public PriceList findById(String id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        PriceList priceList = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_START_TIME,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_END_TIME,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_TYPE_FIELD,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_DATE_OF_WEEK,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_UNIT_PRICE_PER_30_MINUTES,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.PriceListEntry.COLUMN_NAME_UPDATED_AT
            };

            String selection = SoccerFieldContract.PriceListEntry.COLUMN_NAME_ID + " = ? AND " + SoccerFieldContract.PriceListEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgs = { id, "0" };

            cursor = db.query(
                    SoccerFieldContract.PriceListEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                priceList = new PriceList();
                priceList.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_ID)));
                priceList.setStartTime(Time.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_START_TIME))));
                priceList.setEndTime(Time.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_END_TIME))));
                priceList.setTypeField(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_TYPE_FIELD)));
                priceList.setDateOfWeek(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_DATE_OF_WEEK)));
                priceList.setUnitPricePer30Minutes(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_UNIT_PRICE_PER_30_MINUTES))));
                priceList.setDeleted(cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_IS_DELETED)) == 1);
                priceList.setCreateAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_CREATED_AT))));
                priceList.setUpdateAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.PriceListEntry.COLUMN_NAME_UPDATED_AT))));
            }
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return priceList;
    }

    @Override
    public BigDecimal findPriceByTime(Timestamp dateTimeIn, Timestamp dateTimeOut, String date) {
        List<PriceList> priceLists = findByDateOfWeek(date);
        BigDecimal totalCost = BigDecimal.ZERO;

        Calendar calendarIn = Calendar.getInstance();
        calendarIn.setTimeInMillis(dateTimeIn.getTime());
        int hourIn = calendarIn.get(Calendar.HOUR_OF_DAY);
        int minuteIn = calendarIn.get(Calendar.MINUTE);

        Calendar calendarOut = Calendar.getInstance();
        calendarOut.setTimeInMillis(dateTimeOut.getTime());
        int hourOut = calendarOut.get(Calendar.HOUR_OF_DAY);
        int minuteOut = calendarOut.get(Calendar.MINUTE);

        long totalMinutes = TimeUnit.HOURS.toMinutes(hourOut - hourIn) + minuteOut - minuteIn;

        for (PriceList priceList : priceLists) {
            Time startTime = priceList.getStartTime();
            Time endTime = priceList.getEndTime();

            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTimeInMillis(startTime.getTime());
            int hourStart = calendarStart.get(Calendar.HOUR_OF_DAY);
            int minuteStart = calendarStart.get(Calendar.MINUTE);

            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTimeInMillis(endTime.getTime());
            int hourEnd = calendarEnd.get(Calendar.HOUR_OF_DAY);
            int minuteEnd = calendarEnd.get(Calendar.MINUTE);

            if ((hourIn > hourStart || (hourIn == hourStart && minuteIn >= minuteStart)) && (hourIn < hourEnd || (hourIn == hourEnd && minuteIn < minuteEnd))) {
                if ((hourOut < hourEnd || (hourOut == hourEnd && minuteOut <= minuteEnd)) && (hourOut > hourStart || (hourOut == hourStart && minuteOut > minuteStart))) {
                    totalCost = totalCost.add(priceList.getUnitPricePer30Minutes().multiply(new BigDecimal(totalMinutes / 30)));
                    break;
                } else {
                    long minutesInFirstPeriod = TimeUnit.HOURS.toMinutes(hourEnd - hourIn) + minuteEnd - minuteIn;
                    totalCost = totalCost.add(priceList.getUnitPricePer30Minutes().multiply(new BigDecimal(minutesInFirstPeriod / 30)));
                    totalMinutes -= minutesInFirstPeriod;
                    hourIn = hourEnd;
                    minuteIn = minuteEnd;
                }
            }
        }

        return totalCost;
    }
}
