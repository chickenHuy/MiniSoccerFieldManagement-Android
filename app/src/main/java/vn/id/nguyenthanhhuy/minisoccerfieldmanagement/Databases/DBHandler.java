package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.CurrentTimeID;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.PasswordUtils;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mini_soccer_field_mgmt.db";
    private static final int DATABASE_VERSION = 1;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEMBERSHIP_TABLE = "CREATE TABLE MemberShip(" +
                "id TEXT PRIMARY KEY," +
                "name TEXT NOT NULL UNIQUE," +
                "discountRate INTEGER NOT NULL UNIQUE," +
                "minimumSpendAmount REAL NOT NULL," +
                "isDeleted INTEGER NOT NULL DEFAULT 0," +
                "createdAt TEXT NOT NULL," +
                "updatedAt TEXT" +
                ");";
        db.execSQL(CREATE_MEMBERSHIP_TABLE);

        String CREATE_CUSTOMER_TABLE = "CREATE TABLE Customer(" +
                "id TEXT PRIMARY KEY," +
                "memberShipId TEXT NOT NULL," +
                "name TEXT NOT NULL," +
                "phoneNumber TEXT NOT NULL UNIQUE," +
                "totalSpend REAL," +
                "image BLOB," +
                "isDeleted INTEGER NOT NULL DEFAULT 0," +
                "createdAt TEXT NOT NULL," +
                "updatedAt TEXT," +
                "FOREIGN KEY(memberShipId) REFERENCES MemberShip(id)" +
                ");";
        db.execSQL(CREATE_CUSTOMER_TABLE);

        String CREATE_PRICE_LIST_TABLE = "CREATE TABLE PriceList(" +
                "id TEXT PRIMARY KEY," +
                "startTime TEXT NOT NULL," +
                "endTime TEXT NOT NULL," +
                "typeField TEXT NOT NULL," +
                "dateOfWeek TEXT NOT NULL," +
                "unitPricePer30Minutes REAL NOT NULL," +
                "isDeleted INTEGER NOT NULL DEFAULT 0," +
                "createdAt TEXT NOT NULL," +
                "updatedAt TEXT" +
                ");";
        db.execSQL(CREATE_PRICE_LIST_TABLE);

        String CREATE_FIELD_TABLE = "CREATE TABLE Field(" +
                "id TEXT PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "status TEXT," +
                "type TEXT NOT NULL," +
                "image BLOB," +
                "combineField1 TEXT," +
                "combineField2 TEXT," +
                "combineField3 TEXT," +
                "isDeleted INTEGER NOT NULL DEFAULT 0," +
                "createdAt TEXT NOT NULL," +
                "updatedAt TEXT," +
                "FOREIGN KEY(combineField1) REFERENCES Field(id)," +
                "FOREIGN KEY(combineField2) REFERENCES Field(id)," +
                "FOREIGN KEY(combineField3) REFERENCES Field(id)" +
                ");";
        db.execSQL(CREATE_FIELD_TABLE);

        String CREATE_BOOKING_TABLE = "CREATE TABLE Booking(" +
                "id TEXT PRIMARY KEY," +
                "customerId TEXT NOT NULL," +
                "fieldId TEXT NOT NULL," +
                "userId TEXT NOT NULL," +
                "status TEXT," +
                "note TEXT," +
                "timeStart TEXT NOT NULL," +
                "timeEnd TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "isDeleted INTEGER NOT NULL DEFAULT 0," +
                "createdAt TEXT NOT NULL," +
                "updatedAt TEXT," +
                "FOREIGN KEY(customerId) REFERENCES Customer(id)," +
                "FOREIGN KEY(fieldId) REFERENCES Field(id)," +
                "FOREIGN KEY(userId) REFERENCES User(id)" +
                ");";
        db.execSQL(CREATE_BOOKING_TABLE);

        String CREATE_USER_TABLE = "CREATE TABLE User(" +
                "id TEXT PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "gender TEXT NOT NULL," +
                "dateOfBirth TEXT NOT NULL," +
                "image BLOB," +
                "phoneNumber TEXT NOT NULL UNIQUE," +
                "userName TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL," +
                "type TEXT," +
                "isDeleted INTEGER NOT NULL DEFAULT 0," +
                "createdAt TEXT NOT NULL," +
                "updatedAt TEXT" +
                ");";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_MATCH_TABLE = "CREATE TABLE MatchRecord(" +
                "id TEXT PRIMARY KEY," +
                "bookingId TEXT NOT NULL UNIQUE," +
                "checkIn TEXT NOT NULL," +
                "checkOut TEXT," +
                "isDeleted INTEGER NOT NULL DEFAULT 0," +
                "createdAt TEXT NOT NULL," +
                "updatedAt TEXT," +
                "FOREIGN KEY(bookingId) REFERENCES Booking(id)" +
                ");";
        db.execSQL(CREATE_MATCH_TABLE);

        String CREATE_SERVICE_TABLE = "CREATE TABLE Service(" +
                "id TEXT PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "image BLOB," +
                "description TEXT," +
                "unit TEXT," +
                "quantity INTEGER NOT NULL," +
                "sold INTEGER NOT NULL," +
                "status TEXT," +
                "isDeleted INTEGER NOT NULL DEFAULT 0," +
                "createdAt TEXT NOT NULL," +
                "updatedAt TEXT" +
                ");";
        db.execSQL(CREATE_SERVICE_TABLE);

        String CREATE_SERVICE_USAGE_TABLE = "CREATE TABLE ServiceUsage(" +
                "id TEXT PRIMARY KEY," +
                "matchRecordId TEXT," +
                "customerId TEXT," +
                "note TEXT," +
                "isDeleted INTEGER NOT NULL DEFAULT 0," +
                "createdAt TEXT NOT NULL," +
                "updatedAt TEXT," +
                "FOREIGN KEY(matchRecordId) REFERENCES MatchRecord(id)," +
                "FOREIGN KEY(customerId) REFERENCES Customer(id)" +
                ");";
        db.execSQL(CREATE_SERVICE_USAGE_TABLE);

        String CREATE_SERVICE_ITEMS_TABLE = "CREATE TABLE ServiceItems(" +
                "id TEXT PRIMARY KEY," +
                "serviceUsageId TEXT NOT NULL," +
                "serviceId TEXT NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "isDeleted INTEGER NOT NULL DEFAULT 0," +
                "createdAt TEXT NOT NULL," +
                "updatedAt TEXT," +
                "FOREIGN KEY(serviceUsageId) REFERENCES ServiceUsage(id)," +
                "FOREIGN KEY(serviceId) REFERENCES Service(id)" +
                ");";
        db.execSQL(CREATE_SERVICE_ITEMS_TABLE);

        String CREATE_TRANSACTION_TABLE = "CREATE TABLE AppTransaction(" +
                "id TEXT PRIMARY KEY," +
                "userId TEXT NOT NULL," +
                "serviceUsageId TEXT NOT NULL," +
                "type TEXT NOT NULL," +
                "totalAmount REAL NOT NULL," +
                "additionalFee REAL NOT NULL," +
                "discountAmount REAL NOT NULL," +
                "finalAmount REAL NOT NULL," +
                "isDeleted INTEGER NOT NULL DEFAULT 0," +
                "createdAt TEXT NOT NULL," +
                "updatedAt TEXT," +
                "FOREIGN KEY(userId) REFERENCES User(id)," +
                "FOREIGN KEY(serviceUsageId) REFERENCES ServiceUsage(id)" +
                ");";
        db.execSQL(CREATE_TRANSACTION_TABLE);
        insertService(db);
        insertPriceList(db);
        insertMembership(db);
        insertCustomer(db);
        insertUser(db);
        insertField(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS MemberShip");
        db.execSQL("DROP TABLE IF EXISTS Customer");
        db.execSQL("DROP TABLE IF EXISTS PriceList");
        db.execSQL("DROP TABLE IF EXISTS Field");
        db.execSQL("DROP TABLE IF EXISTS Booking");
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS MatchRecord");
        db.execSQL("DROP TABLE IF EXISTS Service");
        db.execSQL("DROP TABLE IF EXISTS ServiceUsage");
        db.execSQL("DROP TABLE IF EXISTS ServiceItems");
        db.execSQL("DROP TABLE IF EXISTS AppTransaction");

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    private void insertService(@NonNull SQLiteDatabase db) {
        String INSERT_DATA = "INSERT INTO Service (id, name, price, image, description, unit, quantity, sold, status, createdAt, updatedAt)\n" +
                "VALUES\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Coca Cola', 10000, NULL, 'Nước ngọt Coca cola', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Coca Cola', 10000, NULL, 'Nước ngọt Coca cola', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Pepsi', 10000, NULL, 'Nước ngọt Pepsi', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Pepsi', 10000, NULL, 'Nước ngọt Pepsi', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', '7 Up', 10000, NULL, 'Nước ngọt 7 Up', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', '7 Up', 10000, NULL, 'Nước ngọt 7 Up', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì sandwich', 20000, NULL, 'Bánh mì sandwich', 'Cái', 50, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì hotdog', 25000, NULL, 'Bánh mì hotdog', 'Cái', 50, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước ngọt Fanta', 12000, NULL, 'Nước ngọt Fanta', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước suối Lavie', 8000, NULL, 'Nước suối Lavie', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước khoáng Aquafina', 10000, NULL, 'Nước khoáng Aquafina', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước tăng lực Red Bull', 25000, NULL, 'Nước tăng lực Red Bull', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước cam Sunny', 15000, NULL, 'Nước cam Sunny', 'Lon', 100, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước ngọt Mirinda', 12000, NULL, 'Nước ngọt Mirinda', 'Lon', 100, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì hamburger', 30000, NULL, 'Bánh mì hamburger', 'Cái', 50, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Coca Cola', 10000, NULL, 'Nước ngọt Coca cola', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Coca Cola', 10000, NULL, 'Nước ngọt Coca cola', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Pepsi', 10000, NULL, 'Nước ngọt Pepsi', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Pepsi', 10000, NULL, 'Nước ngọt Pepsi', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', '7 Up', 10000, NULL, 'Nước ngọt 7 Up', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', '7 Up', 10000, NULL, 'Nước ngọt 7 Up', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì sandwich', 20000, NULL, 'Bánh mì sandwich', 'Cái', 50, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì hotdog', 25000, NULL, 'Bánh mì hotdog', 'Cái', 50, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước ngọt Fanta', 12000, NULL, 'Nước ngọt Fanta', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì hotdog', 25000, NULL, 'Bánh mì hotdog', 'Cái', 50, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước ngọt Fanta', 12000, NULL, 'Nước ngọt Fanta', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước suối Lavie', 8000, NULL, 'Nước suối Lavie', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước khoáng Aquafina', 10000, NULL, 'Nước khoáng Aquafina', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước tăng lực Red Bull', 25000, NULL, 'Nước tăng lực Red Bull', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước cam Sunny', 15000, NULL, 'Nước cam Sunny', 'Lon', 100, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước ngọt Mirinda', 12000, NULL, 'Nước ngọt Mirinda', 'Lon', 100, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì hamburger', 30000, NULL, 'Bánh mì hamburger', 'Cái', 50, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Coca Cola', 10000, NULL, 'Nước ngọt Coca cola', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Coca Cola', 10000, NULL, 'Nước ngọt Coca cola', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Pepsi', 10000, NULL, 'Nước ngọt Pepsi', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Pepsi', 10000, NULL, 'Nước ngọt Pepsi', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', '7 Up', 10000, NULL, 'Nước ngọt 7 Up', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', '7 Up', 10000, NULL, 'Nước ngọt 7 Up', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì sandwich', 20000, NULL, 'Bánh mì sandwich', 'Cái', 50, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì hotdog', 25000, NULL, 'Bánh mì hotdog', 'Cái', 50, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước ngọt Fanta', 12000, NULL, 'Nước ngọt Fanta', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước suối Lavie', 8000, NULL, 'Nước suối Lavie', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước khoáng Aquafina', 10000, NULL, 'Nước khoáng Aquafina', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước tăng lực Red Bull', 25000, NULL, 'Nước tăng lực Red Bull', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước cam Sunny', 15000, NULL, 'Nước cam Sunny', 'Lon', 100, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước ngọt Mirinda', 12000, NULL, 'Nước ngọt Mirinda', 'Lon', 100, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì hamburger', 30000, NULL, 'Bánh mì hamburger', 'Cái', 50, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước suối Lavie', 8000, NULL, 'Nước suối Lavie', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước khoáng Aquafina', 10000, NULL, 'Nước khoáng Aquafina', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước tăng lực Red Bull', 25000, NULL, 'Nước tăng lực Red Bull', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước cam Sunny', 15000, NULL, 'Nước cam Sunny', 'Lon', 100, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước ngọt Mirinda', 12000, NULL, 'Nước ngọt Mirinda', 'Lon', 100, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì hamburger', 30000, NULL, 'Bánh mì hamburger', 'Cái', 50, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Coca Cola', 10000, NULL, 'Nước ngọt Coca cola', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Coca Cola', 10000, NULL, 'Nước ngọt Coca cola', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Pepsi', 10000, NULL, 'Nước ngọt Pepsi', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Pepsi', 10000, NULL, 'Nước ngọt Pepsi', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', '7 Up', 10000, NULL, 'Nước ngọt 7 Up', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', '7 Up', 10000, NULL, 'Nước ngọt 7 Up', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì sandwich', 20000, NULL, 'Bánh mì sandwich', 'Cái', 50, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì hotdog', 25000, NULL, 'Bánh mì hotdog', 'Cái', 50, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước ngọt Fanta', 12000, NULL, 'Nước ngọt Fanta', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước suối Lavie', 8000, NULL, 'Nước suối Lavie', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước khoáng Aquafina', 10000, NULL, 'Nước khoáng Aquafina', 'Chai', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước tăng lực Red Bull', 25000, NULL, 'Nước tăng lực Red Bull', 'Lon', 100, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước cam Sunny', 15000, NULL, 'Nước cam Sunny', 'Lon', 100, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Nước ngọt Mirinda', 12000, NULL, 'Nước ngọt Mirinda', 'Lon', 100, 0, 'Inactive', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì hamburger', 30000, NULL, 'Bánh mì hamburger', 'Cái', 50, 0, 'Active', datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("SV")) + "', 'Bánh mì bơ tỏi', 15000, NULL, 'Bánh mì bơ tỏi', 'Cái', 50, 0, 'Inactive', datetime('now'), NULL);";
        db.execSQL(INSERT_DATA);
    }

    private void insertPriceList(@NonNull SQLiteDatabase db) {
        int id = 0;
        String INSERT_DATA = "INSERT INTO PriceList (id, startTime, endTime, typeField, dateOfWeek, unitPricePer30Minutes, isDeleted, createdAt, updatedAt)\n" +
                "VALUES\n" +
                // Monday
                "('" + (id++) + "', '06:00:00', '08:00:00', '5 a side', 'Monday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '5 a side', 'Monday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '5 a side', 'Monday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '5 a side', 'Monday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '5 a side', 'Monday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '5 a side', 'Monday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '06:00:00', '08:00:00', '7 a side', 'Monday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '7 a side', 'Monday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '7 a side', 'Monday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '7 a side', 'Monday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '7 a side', 'Monday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '7 a side', 'Monday', 150000, 0, datetime('now'), NULL),\n" +
                // Tuesday
                "('" + (id++) + "', '06:00:00', '08:00:00', '5 a side', 'Tuesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '5 a side', 'Tuesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '5 a side', 'Tuesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '5 a side', 'Tuesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '5 a side', 'Tuesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '5 a side', 'Tuesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '06:00:00', '08:00:00', '7 a side', 'Tuesday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '7 a side', 'Tuesday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '7 a side', 'Tuesday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '7 a side', 'Tuesday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '7 a side', 'Tuesday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '7 a side', 'Tuesday', 150000, 0, datetime('now'), NULL),\n" +
                // Wednesday
                "('" + (id++) + "', '06:00:00', '08:00:00', '5 a side', 'Wednesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '5 a side', 'Wednesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '5 a side', 'Wednesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '5 a side', 'Wednesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '5 a side', 'Wednesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '5 a side', 'Wednesday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '06:00:00', '08:00:00', '7 a side', 'Wednesday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '7 a side', 'Wednesday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '7 a side', 'Wednesday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '7 a side', 'Wednesday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '7 a side', 'Wednesday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '7 a side', 'Wednesday', 150000, 0, datetime('now'), NULL),\n" +
                // Thursday
                "('" + (id++) + "', '06:00:00', '08:00:00', '5 a side', 'Thursday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '5 a side', 'Thursday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '5 a side', 'Thursday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '5 a side', 'Thursday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '5 a side', 'Thursday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '5 a side', 'Thursday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '06:00:00', '08:00:00', '7 a side', 'Thursday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '7 a side', 'Thursday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '7 a side', 'Thursday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '7 a side', 'Thursday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '7 a side', 'Thursday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '7 a side', 'Thursday', 150000, 0, datetime('now'), NULL),\n" +
                // Friday
                "('" + (id++) + "', '06:00:00', '08:00:00', '5 a side', 'Friday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '5 a side', 'Friday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '5 a side', 'Friday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '5 a side', 'Friday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '5 a side', 'Friday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '5 a side', 'Friday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '06:00:00', '08:00:00', '7 a side', 'Friday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '7 a side', 'Friday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '7 a side', 'Friday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '7 a side', 'Friday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '7 a side', 'Friday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '7 a side', 'Friday', 150000, 0, datetime('now'), NULL),\n" +
                // Saturday
                "('" + (id++) + "', '06:00:00', '08:00:00', '5 a side', 'Saturday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '5 a side', 'Saturday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '5 a side', 'Saturday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '5 a side', 'Saturday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '5 a side', 'Saturday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '5 a side', 'Saturday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '06:00:00', '08:00:00', '7 a side', 'Saturday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '7 a side', 'Saturday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '7 a side', 'Saturday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '7 a side', 'Saturday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '7 a side', 'Saturday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '7 a side', 'Saturday', 150000, 0, datetime('now'), NULL),\n" +
                // Sunday
                "('" + (id++) + "', '06:00:00', '08:00:00', '5 a side', 'Sunday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '5 a side', 'Sunday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '5 a side', 'Sunday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '5 a side', 'Sunday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '5 a side', 'Sunday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '5 a side', 'Sunday', 100000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '06:00:00', '08:00:00', '7 a side', 'Sunday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '08:00:00', '10:00:00', '7 a side', 'Sunday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '10:00:00', '14:00:00', '7 a side', 'Sunday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '14:00:00', '16:00:00', '7 a side', 'Sunday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '16:00:00', '18:00:00', '7 a side', 'Sunday', 150000, 0, datetime('now'), NULL),\n" +
                "('" + (id++) + "', '18:00:00', '23:00:00', '7 a side', 'Sunday', 150000, 0, datetime('now'), NULL);\n";
        db.execSQL(INSERT_DATA);
    }

    private void insertCustomer(@NonNull SQLiteDatabase db) {
        int id = 0;
        String INSERT_DATA = "INSERT INTO Customer (id, memberShipId, name, phoneNumber, totalSpend, image, isDeleted, createdAt, updatedAt)\n" +
                "VALUES\n" +
                "('" + (CurrentTimeID.nextId("C")) + "', '1', 'Nguyễn Văn Nam', '0328256450', 0, NULL, 0, datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("C")) + "', '1', 'Lê Văn Hà', '0628236450', 0, NULL, 0, datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("C")) + "', '1', 'Phan Văn Mạnh', '0326236357', 0, NULL, 0, datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("C")) + "', '1', 'Võ Văn Quỳnh', '0987555113', 0, NULL, 0, datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("C")) + "', '1', 'Phan Anh Thư', '0387560223', 0, NULL, 0, datetime('now'), NULL),\n" +
                "('" + (CurrentTimeID.nextId("C")) + "', '1', 'Nguyễn Văn Thành', '0323456450', 0, NULL, 0, datetime('now'), NULL);\n";
        db.execSQL(INSERT_DATA);
    }

    private void insertMembership(@NonNull SQLiteDatabase db){
        String INSERT_DATA = "INSERT INTO MemberShip (id, name, discountRate, minimumSpendAmount, isDeleted, createdAt, updatedAt)\n" +
                "VALUES\n" +
                "('1', 'Standard', 0, 0, 0, datetime('now'), NULL),\n" +
                "('2', 'Silver', 2, 1000000, 0, datetime('now'), NULL),\n" +
                "('3', 'Gold', 4, 5000000, 0, datetime('now'), NULL),\n" +
                "('4', 'Platinum', 5, 10000000, 0, datetime('now'), NULL),\n" +
                "('5', 'Diamond', 6, 20000000, 0, datetime('now'), NULL);\n";
        db.execSQL(INSERT_DATA);
    }

    private  void  insertField(@NonNull SQLiteDatabase db)
    {
        String INSERT_DATA = "INSERT INTO Field (id, name, status, type, createdAt) \n" +
        "VALUES \n"+
        "('" + (CurrentTimeID.nextId("F")) + "', '5 Tmp', 'active', '5 a side', datetime('now')),\n" +
        "('" + (CurrentTimeID.nextId("F")) + "', '5 A', 'active', '5 a side', datetime('now')),\n" +
        "('" + (CurrentTimeID.nextId("F")) + "', '5 B', 'active', '5 a side', datetime('now')),\n" +
        "('" + (CurrentTimeID.nextId("F")) + "', '5 C', 'active', '5 a side', datetime('now')),\n" +
        "('" + (CurrentTimeID.nextId("F")) + "', '5 D', 'active', '5 a side', datetime('now')),\n" +
        "('" + (CurrentTimeID.nextId("F")) + "', '5 E', 'active', '5 a side', datetime('now')),\n" +
        "('" + (CurrentTimeID.nextId("F")) + "', '5 F', 'active', '5 a side', datetime('now')),\n" +
        "('" + (CurrentTimeID.nextId("F")) + "', '5 G', 'active', '5 a side', datetime('now')),\n" +
        "('" + (CurrentTimeID.nextId("F")) + "', '5 H', 'active', '5 a side', datetime('now')),\n" +
        "('" + (CurrentTimeID.nextId("F")) + "', '5 I', 'active', '5 a side', datetime('now')),\n" +
        "('" + (CurrentTimeID.nextId("F")) + "', '5 J', 'active', '5 a side', datetime('now')),\n" +
        "('" + (CurrentTimeID.nextId("F")) + "', '5 K', 'active', '5 a side', datetime('now'));\n";
        db.execSQL(INSERT_DATA);

    }

    private void insertUser(@NonNull SQLiteDatabase db) {
        String INSERT_DATA = "INSERT INTO User (id, userName, password, name, phoneNumber, gender, dateOfBirth, role, isDeleted, createdAt)\n" +
                "VALUES\n" +
                "('user1', 'admin', '" + PasswordUtils.hashPassword("admin") + "', 'Admin', '0123456789', 'Male', '1999-01-01', 'Manager', 0, datetime('now'));\n";
        db.execSQL(INSERT_DATA);
    }
}