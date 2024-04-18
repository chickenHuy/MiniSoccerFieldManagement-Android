package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.math.BigDecimal;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.BookingChart;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.IncomeChart;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.ServiceChart;

public interface IChartDAO {
    List<BookingChart> getBookingChart();
    List<ServiceChart> getServiceChart();
    List<IncomeChart> getIncomeChart();
    BigDecimal getTotalIncome();
    BigDecimal getTotalIncomeToday();
}
