package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.RequiresApi;

import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.contract.SoccerFieldContract;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;

public class UserDAOImpl implements IUserDAO {
    DBHandler dbHandler;

    public UserDAOImpl(Context context) {
        this.dbHandler = new DBHandler(context);
    }

    @RequiresApi(api = 26)
    @Override
    public boolean add(User user) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_ID, user.getId());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_NAME, user.getName());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_GENDER, user.getGender());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_PHONE_NUMBER, user.getPhoneNumber());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_DATE_OF_BIRTH, user.getDateOfBirth());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_IMAGE, user.getImage());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_USER_NAME, user.getUserName());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_ROLE, user.getRole());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_TYPE, user.getType());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_CREATED_AT, new Timestamp(System.currentTimeMillis()).toString());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.insert(SoccerFieldContract.UserEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_NAME, user.getName());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_GENDER, user.getGender());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_PHONE_NUMBER, user.getPhoneNumber());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_DATE_OF_BIRTH, user.getDateOfBirth());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_IMAGE, user.getImage());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_USER_NAME, user.getUserName());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_ROLE, user.getRole());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_TYPE, user.getType());
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_UPDATED_AT, new Timestamp(System.currentTimeMillis()).toString());

            long result = db.update(SoccerFieldContract.UserEntry.TABLE_NAME, values, SoccerFieldContract.UserEntry.COLUMN_NAME_ID + " = ?", new String[]{user.getId()});
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
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_IS_DELETED, 1);

            long result = db.update(SoccerFieldContract.UserEntry.TABLE_NAME, values, SoccerFieldContract.UserEntry.COLUMN_NAME_ID + " = ?", new String[]{id});
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User findById(String id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        User user = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.UserEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_GENDER,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_DATE_OF_BIRTH,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_PHONE_NUMBER,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_USER_NAME,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_PASSWORD,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_ROLE,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_TYPE,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.UserEntry.COLUMN_NAME_ID + " = ? AND " + SoccerFieldContract.UserEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgument = {id, "0"};

            cursor = db.query(
                    SoccerFieldContract.UserEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgument,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_NAME)));
                user.setGender(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_GENDER)));
                user.setDateOfBirth(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_DATE_OF_BIRTH)));
                user.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_IMAGE)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_PHONE_NUMBER)));
                user.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_ROLE)));
                user.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_TYPE)));
                user.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_CREATED_AT))));
                user.setUpdatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_UPDATED_AT))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<User> users = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.UserEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_GENDER,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_DATE_OF_BIRTH,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_PHONE_NUMBER,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_USER_NAME,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_PASSWORD,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_ROLE,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_TYPE,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.UserEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgument = {"0"};

            cursor = db.query(
                    SoccerFieldContract.UserEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgument,
                    null,
                    null,
                    null
            );

            if (cursor.moveToNext()) {
                User user = new User();
                user.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_NAME)));
                user.setGender(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_GENDER)));
                user.setDateOfBirth(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_DATE_OF_BIRTH)));
                user.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_IMAGE)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_PHONE_NUMBER)));
                user.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_ROLE)));
                user.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_TYPE)));
                user.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_CREATED_AT))));
                user.setUpdatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_UPDATED_AT))));

                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return users;
    }

    @Override
    public User verifyLoginData(String username, String password) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        User user = null;
        Cursor cursor = null;
        try {
            String[] projection = {
                    SoccerFieldContract.UserEntry.COLUMN_NAME_ID,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_NAME,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_GENDER,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_DATE_OF_BIRTH,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_IMAGE,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_PHONE_NUMBER,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_USER_NAME,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_PASSWORD,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_ROLE,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_TYPE,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_CREATED_AT,
                    SoccerFieldContract.UserEntry.COLUMN_NAME_UPDATED_AT
            };
            String selection = SoccerFieldContract.UserEntry.COLUMN_NAME_USER_NAME + " = ? AND " + SoccerFieldContract.UserEntry.COLUMN_NAME_PASSWORD + " = ? AND " + SoccerFieldContract.UserEntry.COLUMN_NAME_IS_DELETED + " = ?";
            String[] selectionArgument = {username, password, "0"};

            cursor = db.query(
                    SoccerFieldContract.UserEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgument,
                    null,
                    null,
                    null
            );

            if (cursor.moveToNext()) {
                user = new User();
                user.setId(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_NAME)));
                user.setGender(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_GENDER)));
                user.setDateOfBirth(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_DATE_OF_BIRTH)));
                user.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_IMAGE)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_PHONE_NUMBER)));
                user.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_ROLE)));
                user.setType(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_TYPE)));
                user.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_CREATED_AT))));
                user.setUpdatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SoccerFieldContract.UserEntry.COLUMN_NAME_UPDATED_AT))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }

    @Override
    public boolean changeRole(String id, String role) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_ROLE, role);

            long result = db.update(SoccerFieldContract.UserEntry.TABLE_NAME, values, SoccerFieldContract.UserEntry.COLUMN_NAME_ID + " = ?", new String[]{id});
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean changePassword(String id, String newPassword) {
        try {
            SQLiteDatabase db = dbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SoccerFieldContract.UserEntry.COLUMN_NAME_PASSWORD, newPassword);

            long result = db.update(SoccerFieldContract.UserEntry.TABLE_NAME, values, SoccerFieldContract.UserEntry.COLUMN_NAME_ID + " = ?", new String[]{id});
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
