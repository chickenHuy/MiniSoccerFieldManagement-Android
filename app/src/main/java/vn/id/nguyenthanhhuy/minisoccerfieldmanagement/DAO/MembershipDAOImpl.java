package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.contract.SoccerFieldContract;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Membership;

public class MembershipDAOImpl implements IMembershipDAO{
    DBHandler dbHandler;
    public MembershipDAOImpl(Context context) {
        this.dbHandler = new DBHandler(context);
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
        return null;
    }

    @Override
    public Boolean softDelete(String id) {
        return null;
    }

    @Override
    public Membership findById(String id) {
        return null;
    }

    @Override
    public List<Membership> findAll() {
        return null;
    }

    @Override
    public Membership findBySpendAmount(BigDecimal totalSpend) {
        return null;
    }

    @Override
    public int findDiscountByCustomer(String customerId) {
        return 0;
    }
}
