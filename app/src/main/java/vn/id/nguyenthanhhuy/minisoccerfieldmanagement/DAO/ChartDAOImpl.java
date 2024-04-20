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
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.StaticString;
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
        List<ServiceChart> services = new ArrayList<>();
        Cursor cursor = null;
        try (SQLiteDatabase db = dbHandler.getReadableDatabase())
        {
            String query =
                    "SELECT service.name, service.id, sum(serviceItems.quantity) as qty \n" +
                    "FROM service \n" +
                    "JOIN serviceItems ON service.id = serviceItems.serviceId \n" +
                    "GROUP BY service.name, service.id;";

            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") int qty = cursor.getInt(cursor.getColumnIndex("qty"));
                    ServiceChart service = new ServiceChart(qty, name);
                    services.add(service);
                } while (cursor.moveToNext());

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        //lamda tính tổng count = tổng rate của các service
        int total = services.stream().mapToInt(ServiceChart::getRate).sum();
        //lamda tính phần trăm của từng service
        services.forEach(service -> {
            service.setRate(service.getRate() * 100 / total);
        });
        return  services;
    }

    @Override
    public List<IncomeChart> getIncomeChart(String type) {
        List<IncomeChart> incomeCharts = new ArrayList<>();
        String query = "";
        if (type.equals(StaticString.FINAL_AMOUNT)) {
            query = "SELECT DATE(createdAt) as date, SUM(" + type + ") as income " +
                    "FROM AppTransaction " +
                    "GROUP BY DATE(createdAt)";
        }
        else if (type.equals(StaticString.FIELD_AMOUNT))
        {
            query = "SELECT DATE(AppTransaction.createdAt) as date, SUM(booking.price) as income from AppTransaction JOIN ServiceUsage on AppTransaction.serviceUsageId = ServiceUsage.id join MatchRecord on ServiceUsage.matchRecordId = MatchRecord.Id  join  Booking on MatchRecord.bookingId = Booking.id Group by (DATE(`AppTransaction`.createdAt))" ;
        }
        else if (type.equals(StaticString.SERVICE_AMOUNT))
        {
            query = "SELECT DATE(AppTransaction.createdAt) as date, SUM(service.price * serviceItems.quantity) as income from AppTransaction JOIN ServiceUsage on AppTransaction.serviceUsageId = ServiceUsage.id join ServiceItems on ServiceUsage.id = ServiceItems.serviceUsageId  join  Service on Service.id = ServiceItems.serviceId Group by (DATE(AppTransaction.createdAt))";
        }
        if (!query.isEmpty()) {
            try (SQLiteDatabase db = dbHandler.getReadableDatabase()) {


                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                        @SuppressLint("Range") BigDecimal income = new BigDecimal(cursor.getString(cursor.getColumnIndex("income")));

                        IncomeChart incomeChart = new IncomeChart(income, date, type);
                        incomeCharts.add(incomeChart);
                    } while (cursor.moveToNext());

                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return incomeCharts;
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
