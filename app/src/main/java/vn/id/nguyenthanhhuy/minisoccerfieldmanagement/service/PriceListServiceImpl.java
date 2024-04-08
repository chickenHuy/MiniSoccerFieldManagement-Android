package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IPriceListDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.PriceListDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.PriceList;

public class PriceListServiceImpl implements IPriceListService{
    IPriceListDAO priceListDAO;
    public PriceListServiceImpl(Context context)
    {
        priceListDAO = new PriceListDAOImpl(context);
    }

    @Override
    public Boolean add(PriceList model) {
        return priceListDAO.add(model);
    }

    @Override
    public Boolean update(PriceList model) {
        return priceListDAO.update(model);
    }

    @Override
    public Boolean softDelete(String id) {
        return priceListDAO.softDelete(id);
    }

    @Override
    public List<PriceList> findByDateOfWeek(String date) {
        return priceListDAO.findByDateOfWeek(date);
    }

    @Override
    public List<PriceList> findAll() {
        return priceListDAO.findAll();
    }

    @Override
    public PriceList findById(String id) {
        return priceListDAO.findById(id);
    }

    @Override
    public BigDecimal findPriceByTime(Time timeIn, Time timeOut, String date) {
        List<PriceList> priceLists = priceListDAO.findByDateOfWeek(date);

        String[] timeInParts = timeIn.toString().split(":");
        int hourIn = Integer.parseInt(timeInParts[0]);
        int minuteIn = Integer.parseInt(timeInParts[1]);

        String[] timeOutParts = timeOut.toString().split(":");
        int hourOut = Integer.parseInt(timeOutParts[0]);
        int minuteOut = Integer.parseInt(timeOutParts[1]);

        // Tính tổng số phút giữa timeIn và timeOut
        long durationInMinutes = (hourOut * 60 + minuteOut) - (hourIn * 60 + minuteIn);

        // Tính số đoạn thời gian mỗi 30 phút
        int numberOfSegments = (int) Math.ceil((double) durationInMinutes / 30);

        // Tính tổng giá tiền
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Duyệt qua từng đoạn thời gian mỗi 30 phút
        for (int i = 0; i < numberOfSegments; i++) {
            // Tính thời gian bắt đầu và kết thúc của đoạn thời gian
            int segmentStartHour = hourIn + (minuteIn + i * 30) / 60;
            int segmentStartMinute = (minuteIn + i * 30) % 60;
            int segmentEndHour = hourIn + (minuteIn + (i + 1) * 30) / 60;
            int segmentEndMinute = (minuteIn + (i + 1) * 30) % 60;

            // Duyệt qua danh sách các mục giá
            for (PriceList priceList : priceLists) {
                // Convert java.sql.Time to String and split it to get hours and minutes
                String[] startTimeParts = priceList.getStartTime().toString().split(":");
                int startHour = Integer.parseInt(startTimeParts[0]);
                int startMinute = Integer.parseInt(startTimeParts[1]);

                String[] endTimeParts = priceList.getEndTime().toString().split(":");
                int endHour = Integer.parseInt(endTimeParts[0]);
                int endMinute = Integer.parseInt(endTimeParts[1]);

                // Check if the 30-minute segment is within the time range of the price list item
                if ((segmentStartHour > startHour || (segmentStartHour == startHour && segmentStartMinute >= startMinute)) &&
                        (segmentEndHour < endHour || (segmentEndHour == endHour && segmentEndMinute <= endMinute))) {
                    // If it is, add the price of the price list item to the total price
                    totalPrice = totalPrice.add(priceList.getUnitPricePer30Minutes());
                    break; // Move on to the next segment
                }
            }
        }
        return totalPrice;
    }

    @Override
    public BigDecimal findPriceByTimeAndType(Time timeIn, Time timeOut, String date, String type) {
        List<PriceList> priceListsTmp = priceListDAO.findByDateOfWeek(date);
        List<PriceList> priceLists = new ArrayList<>();
        for (PriceList priceList : priceListsTmp) {
            if (priceList.getTypeField().equals(type)) {
                priceLists.add(priceList);
            }
        }

        String[] timeInParts = timeIn.toString().split(":");
        int hourIn = Integer.parseInt(timeInParts[0]);
        int minuteIn = Integer.parseInt(timeInParts[1]);

        String[] timeOutParts = timeOut.toString().split(":");
        int hourOut = Integer.parseInt(timeOutParts[0]);
        int minuteOut = Integer.parseInt(timeOutParts[1]);

        // Tính tổng số phút giữa timeIn và timeOut
        long durationInMinutes = (hourOut * 60 + minuteOut) - (hourIn * 60 + minuteIn);

        // Tính số đoạn thời gian mỗi 30 phút
        int numberOfSegments = (int) Math.ceil((double) durationInMinutes / 30);

        // Tính tổng giá tiền
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Duyệt qua từng đoạn thời gian mỗi 30 phút
        for (int i = 0; i < numberOfSegments; i++) {
            // Tính thời gian bắt đầu và kết thúc của đoạn thời gian
            int segmentStartHour = hourIn + (minuteIn + i * 30) / 60;
            int segmentStartMinute = (minuteIn + i * 30) % 60;
            int segmentEndHour = hourIn + (minuteIn + (i + 1) * 30) / 60;
            int segmentEndMinute = (minuteIn + (i + 1) * 30) % 60;

            // Duyệt qua danh sách các mục giá
            for (PriceList priceList : priceLists) {
                // Convert java.sql.Time to String and split it to get hours and minutes
                String[] startTimeParts = priceList.getStartTime().toString().split(":");
                int startHour = Integer.parseInt(startTimeParts[0]);
                int startMinute = Integer.parseInt(startTimeParts[1]);

                String[] endTimeParts = priceList.getEndTime().toString().split(":");
                int endHour = Integer.parseInt(endTimeParts[0]);
                int endMinute = Integer.parseInt(endTimeParts[1]);

                // Check if the 30-minute segment is within the time range of the price list item
                if ((segmentStartHour > startHour || (segmentStartHour == startHour && segmentStartMinute >= startMinute)) &&
                        (segmentEndHour < endHour || (segmentEndHour == endHour && segmentEndMinute <= endMinute))) {
                    // If it is, add the price of the price list item to the total price
                    totalPrice = totalPrice.add(priceList.getUnitPricePer30Minutes());
                    break; // Move on to the next segment
                }
            }
        }
        return totalPrice;
    }

    @Override
    public List<PriceList> findByDateAndType(String date, String type) {
        List<PriceList> priceLists = priceListDAO.findByDateOfWeek(date);
        List<PriceList> result = new ArrayList<>();
        for (PriceList priceList : priceLists) {
            if (priceList.getTypeField().equals(type)) {
                result.add(priceList);
            }
        }
        return  result;
    }
}
