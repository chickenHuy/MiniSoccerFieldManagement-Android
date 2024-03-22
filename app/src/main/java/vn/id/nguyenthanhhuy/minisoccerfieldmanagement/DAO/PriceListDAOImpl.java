package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

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
    }

    @Override
    public Boolean update(PriceList model) {
        return null;
    }

    @Override
    public Boolean softDelete(String id) {
        return null;
    }

    @Override
    public List<PriceList> findByDateOfWeek(Date date) {
        return null;
    }

    @Override
    public List<PriceList> findAll() {
        return null;
    }

    @Override
    public PriceList findById(String id) {
        return null;
    }

    @Override
    public BigDecimal findPriceByTime(Timestamp dateTimeIn, Timestamp dateTimeOut) {
        return null;
    }
}
