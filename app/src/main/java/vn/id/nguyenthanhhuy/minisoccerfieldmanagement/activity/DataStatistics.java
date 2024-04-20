package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.ChartDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IChartDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.BookingChart;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.IncomeChart;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceChart;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.StaticString;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.Utils;

public class DataStatistics extends AppCompatActivity {
    private LineChart lineChart;
    private PieChart serviceChart;
    private BarChart bookingChart;
    private TextView tvTotalIncome, tvTotalIncomeToday;
    IChartDAO chartDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_statistics);
        chartDAO = new ChartDAOImpl(this);
        setWidgets();
        setLineChart();
        setPieChart();
        setBarChart();
        setIncome();
        goBack();
    }

    private void setIncome() {
        tvTotalIncome.setText(Utils.formatVND(chartDAO.getTotalIncome()));
        tvTotalIncomeToday.setText(Utils.formatVND(chartDAO.getTotalIncomeToday()));
    }

    private void setBarChart() {
        List<BookingChart> bookingCharts = chartDAO.getBookingChart();

        List<BarEntry> bookingData = new ArrayList<>();
        List<String> days = new ArrayList<>();

        for (int i = 0; i < bookingCharts.size(); i++) {
            BookingChart bookingChart = bookingCharts.get(i);
            bookingData.add(new BarEntry(i, bookingChart.getCount()));
            days.add(bookingChart.getCreatedAt());
        }

        BarDataSet bookingDataSet = new BarDataSet(bookingData, "");
        bookingDataSet.setColors(ContextCompat.getColor(this, R.color.primaryColor),
                ContextCompat.getColor(this, R.color.aquamarine),
                ContextCompat.getColor(this, R.color.lightGreen),
                ContextCompat.getColor(this, R.color.persianGreen));
        bookingDataSet.setHighLightAlpha(255);
        bookingDataSet.setDrawValues(true);

        BarData bookingBarData = new BarData(bookingDataSet);
        bookingChart.setData(bookingBarData);

        XAxis bookingXAxis = bookingChart.getXAxis();
        bookingXAxis.setDrawGridLines(false);
        bookingXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bookingXAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        bookingXAxis.setTextColor(ContextCompat.getColor(this, R.color.primaryColor));

        YAxis leftAxis = bookingChart.getAxisLeft();
        leftAxis.setDrawLabels(false);
        leftAxis.setAxisLineColor(ContextCompat.getColor(this, R.color.primaryColor));

        YAxis rightAxis = bookingChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setAxisLineColor(ContextCompat.getColor(this, R.color.primaryColor));

        bookingChart.getDescription().setEnabled(false);
        bookingChart.invalidate();
        bookingChart.animateXY(1000, 1000);
    }


    private void setPieChart() {

        List<ServiceChart> services = chartDAO.getServiceChart();
        List<PieEntry> serviceData = new ArrayList<>();
        // Replace with your actual data
        for (int i = 0; i < services.size(); i++) {
            serviceData.add(new PieEntry(services.get(i).getRate(), services.get(i).getName()));
        }

        PieDataSet serviceDataSet = new PieDataSet(serviceData, "");

        // Set colors for the PieDataSet
        serviceDataSet.setColors(ContextCompat.getColor(this, R.color.primaryColor),
                ContextCompat.getColor(this, R.color.black_overlay),
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.light_blue_900),
                ContextCompat.getColor(this, R.color.light_blue_A400));

        PieData servicePieData = new PieData(serviceDataSet);

        serviceChart.setData(servicePieData);
        serviceChart.getDescription().setEnabled(false);

        serviceChart.invalidate();
    }

    private void setWidgets() {
        lineChart = findViewById(R.id.line_chart);
        serviceChart = findViewById(R.id.service_chart);
        bookingChart = findViewById(R.id.booking_chart);

        tvTotalIncome = findViewById(R.id.total_revenue);
        tvTotalIncomeToday = findViewById(R.id.revenue_today);

    }

    private void setLineChart() {
        lineChart.getDescription().setEnabled(false);

        List<IncomeChart> finalAmount = chartDAO.getIncomeChart(StaticString.FINAL_AMOUNT);
        List<IncomeChart> fieldIncome = chartDAO.getIncomeChart(StaticString.FIELD_AMOUNT);
        List<IncomeChart> serviceIncome = chartDAO.getIncomeChart(StaticString.SERVICE_AMOUNT);
        // Make 3 char only size : if not exist date insert date and set data = 0
        String[] dates = new String[finalAmount.size()];
        BigDecimal[] totalIncomeBig = new BigDecimal[finalAmount.size()];
        BigDecimal[] fieldIncomeBig = new BigDecimal[finalAmount.size()];
        BigDecimal[] serviceIncomeBig = new BigDecimal[finalAmount.size()];
        for (int i = 0; i < finalAmount.size(); i++) {
            dates[i] = finalAmount.get(i).getDate();
            totalIncomeBig[i] = finalAmount.get(i).getIncome();
            fieldIncomeBig[i] = BigDecimal.ZERO;
            serviceIncomeBig[i] = BigDecimal.ZERO;
        }
        for (int i = 0; i < fieldIncomeBig.length; i++) {
            for (IncomeChart incomeChart : fieldIncome) {
                if (dates[i].equals(incomeChart.getDate())) {
                    fieldIncomeBig[i] = incomeChart.getIncome();
                    break;
                }
            }
        }
        for (int i = 0; i < serviceIncomeBig.length; i++) {
            for (IncomeChart incomeChart : serviceIncome) {
                if (dates[i].equals(incomeChart.getDate())) {
                    serviceIncomeBig[i] = incomeChart.getIncome();
                    break;
                }
            }
        }


        List<Entry> totalIncomeData = new ArrayList<>();
        List<Entry> additionalFeeData = new ArrayList<>();
        List<Entry> discountData = new ArrayList<>();

        for (int i = 0; i < dates.length; i++) {
            totalIncomeData.add(new Entry(i, totalIncomeBig[i].floatValue()));
            additionalFeeData.add(new Entry(i, fieldIncomeBig[i].floatValue()));
            discountData.add(new Entry(i, serviceIncomeBig[i].floatValue()));
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

    public void goBack() {
        findViewById(R.id.button_back).setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        finish();
    }
}