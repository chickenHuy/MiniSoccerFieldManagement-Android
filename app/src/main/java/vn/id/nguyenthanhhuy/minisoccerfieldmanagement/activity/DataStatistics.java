package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;

public class DataStatistics extends AppCompatActivity {
    private LineChart lineChart;
    private PieChart serviceChart;
    private BarChart bookingChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_statistics);

        setWidgets();
        setLineChart();
        setPieChart();
        setBarChart();

    }

    private void setBarChart() {
        // Booking chart
        List<BarEntry> bookingData = new ArrayList<>();
        List<String> days = new ArrayList<>();
        // Replace with your actual data
        float[] bookings = new float[]{5, 7, 6, 8, 9};

        for (int i = 0; i < bookings.length; i++) {
            bookingData.add(new BarEntry(i, bookings[i]));
        }

        BarDataSet bookingDataSet = new BarDataSet(bookingData, "Number of bookings");
        bookingDataSet.setColor(ContextCompat.getColor(this, R.color.primaryColor));
        BarData bookingBarData = new BarData(bookingDataSet);

        bookingChart.setData(bookingBarData);

        XAxis bookingXAxis = bookingChart.getXAxis();
        bookingXAxis.setDrawGridLines(false);
        bookingXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bookingXAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        bookingChart.getDescription().setEnabled(false);
        bookingChart.invalidate();
    }


    private void setPieChart() {
        List<PieEntry> serviceData = new ArrayList<>();
        // Replace with your actual data
        float[] serviceRevenues = new float[]{20, 30, 25, 35, 40};
        String[] services = new String[]{"Dịch vụ 1", "Dịch vụ 2", "Dịch vụ 3", "Dịch vụ 4", "Other"};

        for (int i = 0; i < serviceRevenues.length; i++) {
            serviceData.add(new PieEntry(serviceRevenues[i], services[i]));
        }

        PieDataSet serviceDataSet = new PieDataSet(serviceData, "Doanh thu dịch vụ");

        // Set colors for the PieDataSet
        serviceDataSet.setColors(new int[] {
                ContextCompat.getColor(this, R.color.primaryColor),
                ContextCompat.getColor(this, R.color.black_overlay),
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.light_blue_900),
                ContextCompat.getColor(this, R.color.light_blue_A400)
        });

        PieData servicePieData = new PieData(serviceDataSet);

        serviceChart.setData(servicePieData);
        serviceChart.getDescription().setEnabled(false);

        serviceChart.invalidate();
    }

    private void setWidgets() {
        lineChart = findViewById(R.id.line_chart);
        serviceChart = findViewById(R.id.service_chart);
        bookingChart = findViewById(R.id.booking_chart);
    }

    private void setLineChart() {
        lineChart.getDescription().setEnabled(false);

        float[] totalIncome = new float[]{100000, 200000, 150000, 200000};
        float[] fieldIncome = new float[]{50000, 100000, 75000,0};
        float[] serviceIncome = new float[]{20000, 40000, 30000,25000};
        String[] dates = new String[]{"Ngày 1", "Ngày 2", "Ngày 3", "Ngày 4"};

        List<Entry> totalIncomeData = new ArrayList<>();
        List<Entry> additionalFeeData = new ArrayList<>();
        List<Entry> discountData = new ArrayList<>();

        for (int i = 0; i < totalIncome.length; i++) {
            totalIncomeData.add(new Entry(i, totalIncome[i]));
            additionalFeeData.add(new Entry(i, fieldIncome[i]));
            discountData.add(new Entry(i, serviceIncome[i]));
        }

        LineDataSet totalIncomeDataSet = new LineDataSet(totalIncomeData, "Total Income");
        totalIncomeDataSet.setColor(ContextCompat.getColor(this, R.color.primaryColor));
        LineDataSet fieldDataSet = new LineDataSet(additionalFeeData, "Field Income");
        fieldDataSet.setColor(ContextCompat.getColor(this, R.color.light_blue_900));
        LineDataSet serviceDataSet = new LineDataSet(discountData, "Service Income");
        serviceDataSet.setColor(ContextCompat.getColor(this, R.color.red));

        LineData lineData = new LineData();
        lineData.addDataSet(totalIncomeDataSet);
        lineData.addDataSet(fieldDataSet);
        lineData.addDataSet(serviceDataSet);

        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setLabelCount(dates.length, true);
        lineChart.invalidate();
    }
}