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
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class MembershipDAOImpl implements IMembershipDAO{
    DBHandler dbHandler;
    Context context;
    public MembershipDAOImpl(Context context) {
        this.dbHandler = new DBHandler(context);
        this.context = context;
    }
    @Override
    public Boolean add(Membership membership) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_ID, membership.getId());
            values.put(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_NAME, membership.getName());
            values.put(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_DISCOUNT_RATE, membership.getDiscountRate());
            values.put(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_MINIMUM_SPEND_AMOUNT, membership.getMinimumSpendAmount().doubleValue());
            values.put(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_CREATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.insert(SoccerFieldContract.MemberShipEntry.TABLE_NAME, null, values);
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
    public Boolean update(Membership membership) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_NAME, membership.getName());
            values.put(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_DISCOUNT_RATE, membership.getDiscountRate());
            values.put(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_MINIMUM_SPEND_AMOUNT, membership.getMinimumSpendAmount().doubleValue());
            values.put(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.update(SoccerFieldContract.MemberShipEntry.TABLE_NAME, values, SoccerFieldContract.MemberShipEntry.COLUMN_NAME_ID + " = ?", new String[]{membership.getId()});
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
            values.put(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_IS_DELETED, 1);
            values.put(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.update(SoccerFieldContract.MemberShipEntry.TABLE_NAME, values, SoccerFieldContract.MemberShipEntry.COLUMN_NAME_ID + " = ? AND ", new String[]{id});
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
    public Membership findById(String id) {
        Membership membership = null;
        Cursor cursor = null;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        try {
            String[] projection = {
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_DISCOUNT_RATE,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_MINIMUM_SPEND_AMOUNT,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.MemberShipEntry.COLUMN_NAME_ID + " = ? AND " + SoccerFieldContract.MemberShipEntry.COLUMN_NAME_IS_DELETED + " = 0 ;";
            String[] selectionArgs = {id};
            cursor = db.query(
                    SoccerFieldContract.MemberShipEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst())
            {
                String updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_UPDATED_AT));
                membership = new Membership(
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_DISCOUNT_RATE)),
                        BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_MINIMUM_SPEND_AMOUNT))),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_IS_DELETED)) == 1,
                        Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_CREATED_AT))),
                        Utils.toTimestamp(updatedAt)
                );

            }
            return membership;
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    @Override
    public List<Membership> findAll() {
        Membership membership = null;
        List<Membership> result = new ArrayList<Membership>();
        Cursor cursor = null;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        try {
            String[] projection = {
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_DISCOUNT_RATE,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_MINIMUM_SPEND_AMOUNT,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_IS_DELETED,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.MemberShipEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.MemberShipEntry.COLUMN_NAME_IS_DELETED + " = 0 ;";;
            cursor = db.query(
                    SoccerFieldContract.MemberShipEntry.TABLE_NAME,
                    projection,
                    selection,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext())
            {
                String updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_UPDATED_AT));
                membership = new Membership(
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_DISCOUNT_RATE)),
                        BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_MINIMUM_SPEND_AMOUNT))),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_IS_DELETED)) == 1,
                        Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.MemberShipEntry.COLUMN_NAME_CREATED_AT))),
                        Utils.toTimestamp(updatedAt)
                );
                result.add(membership);

            }
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }


}
