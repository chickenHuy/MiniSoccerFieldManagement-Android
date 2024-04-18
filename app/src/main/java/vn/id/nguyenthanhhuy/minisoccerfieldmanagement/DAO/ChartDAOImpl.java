package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.BookingChart;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.IncomeChart;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceChart;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class ChartDAOImpl implements IChartDAO{

    DBHandler dbHandler;
    public ChartDAOImpl(Context context){dbHandler = new DBHandler(context);}
    @Override
    public List<BookingChart> getBookingChart() {
        List<BookingChart> bookingCharts = new ArrayList<>();

        try (SQLiteDatabase db = dbHandler.getReadableDatabase()) {
            String query = "SELECT DATE(createdAt) as date, COUNT(DATE(createdAt)) as count " +
                    "FROM Booking " +
                    "GROUP BY DATE(createdAt)";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                    @SuppressLint("Range") String date = dateFormat.format(java.sql.Date.valueOf(cursor.getString(cursor.getColumnIndex("date"))));
                    @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("count"));

                    BookingChart bookingChart = new BookingChart(count, date);
                    bookingCharts.add(bookingChart);
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookingCharts;
    }

    @Override
    public List<ServiceChart> getServiceChart() {
        return null;
    }

    @Override
    public List<IncomeChart> getIncomeChart() {
        return null;
    }

    @Override
    public BigDecimal getTotalIncome() {
        BigDecimal totalIncome = BigDecimal.ZERO;

        try (SQLiteDatabase db = dbHandler.getReadableDatabase()) {
            String query = "SELECT SUM(finalAmount) as totalIncome FROM AppTransaction";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String income = cursor.getString(cursor.getColumnIndex("totalIncome"));
                totalIncome = new BigDecimal(income);
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalIncome;
    }

    @Override
    public BigDecimal getTotalIncomeToday() {
        BigDecimal totalIncome = BigDecimal.ZERO;

        try (SQLiteDatabase db = dbHandler.getReadableDatabase()) {
            String query = "SELECT SUM(finalAmount) as totalIncome FROM AppTransaction Where DATE(createdAt) = DATE('now')";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String income = cursor.getString(cursor.getColumnIndex("totalIncome"));
                totalIncome = new BigDecimal(income);
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalIncome;
    }
}
