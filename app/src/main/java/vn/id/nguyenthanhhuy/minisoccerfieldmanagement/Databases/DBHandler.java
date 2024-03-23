package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "name TEXT NOT NULL UNIQUE," +
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
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


}