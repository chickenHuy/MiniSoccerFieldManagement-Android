package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.Databases.DBHandler;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.activity.MainActivity;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewMatchAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.RecyclerViewMatchAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.RecyclerViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ViewPagerAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentHomeBinding;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Booking;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.Service;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.BookingServiceImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service.ServiceServiceImpl;

public class HomeFragment extends Fragment {
    private boolean isSwipeable;
    private FragmentHomeBinding binding;
    private ViewPager viewPagerImage;
    private String[] imagePathArray;
    private ViewPagerAdapter viewPagerImageAdapter;
    private int duration = 7000;
    private boolean isLoading = false;
    private ExecutorService executorService;

    private Button buttonUpcoming;
    private Button buttonLive;
    private Button buttonToday;
    private Button buttonTomorrow;
    private ArrayList<Button> listButton;
    private ListView listViewMatch;
    private List<Service> listService;
    private List<Booking> bookingList;

    private BookingServiceImpl bookingService;

    private RecyclerView recyclerViewListService;
    private RecyclerView recyclerViewListBooking;

    private TextView text_view_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        bookingService = new BookingServiceImpl(getContext());
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setWidgets();
        setViewPagerImages();
        setListButton();
        text_view_name.setText(MainApplication.curentUser.getName());
        setRecyclerViewListService();

        loadService(10, 0, "Active", 0, "ORDER BY id DESC");
    }

    public void setViewPagerImages() {
        viewPagerImage = binding.viewPager;

        try {
            imagePathArray = requireActivity().getAssets().list("viewPagerImages");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imagePathArray != null) {
            for (int i = 0; i < imagePathArray.length; i++) {
                imagePathArray[i] = "viewPagerImages/" + imagePathArray[i];
            }

            String[] imageFieldArray = new String[7];
            for (int i = 0; i < imageFieldArray.length; i++) {
                imageFieldArray[i] = imagePathArray[i % imagePathArray.length];
            }

            viewPagerImageAdapter = new ViewPagerAdapter(getContext(), imageFieldArray);
            viewPagerImage.setAdapter(viewPagerImageAdapter);

            viewPagerImage.setClipToPadding(false);
            viewPagerImage.setPadding(37, 0, 37, 0);

            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int currentItem = viewPagerImage.getCurrentItem();

                    if (currentItem == imageFieldArray.length - 1) {
                        viewPagerImage.setCurrentItem(0);
                    } else {
                        viewPagerImage.setCurrentItem(currentItem + 1);
                    }
                    handler.postDelayed(this, duration);
                }
            };
            handler.postDelayed(runnable, duration);
        }
    }

    public void setListButton() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (!isSwipeable) {
//                    recyclerViewListBooking.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                    return;
                }
                int position = viewHolder.getAdapterPosition();

                if (swipeDir == ItemTouchHelper.LEFT) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Hủy trận đấu")
                            .setMessage("Bạn có chắc chắn muốn hủy trận đấu này không?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Remove item from your data set here.
                                    bookingList.remove(position);

                                    // Notify the adapter that an item is removed.
                                    RecyclerViewMatchAdapter adapter = (RecyclerViewMatchAdapter) recyclerViewListBooking.getAdapter();
                                    adapter.notifyItemRemoved(position);

                                    // Notify the adapter that the data set has changed.
                                    adapter.notifyItemRangeChanged(position, bookingList.size());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User cancelled the dialog
                                    // Here you can run whatever you want to if the user dismisses the dialog
                                    recyclerViewListBooking.getAdapter().notifyItemChanged(position);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else if (swipeDir == ItemTouchHelper.RIGHT) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Checkin trận đấu")
                            .setMessage("Bạn có chắc chắn muốn checkin trận đấu này không?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with checkin operation
                                    bookingService.updateStatus(bookingList.get(position).getId(), "completed");
                                    bookingList.remove(position);
                                    // Notify the adapter that an item is removed.
                                    RecyclerViewMatchAdapter adapter = (RecyclerViewMatchAdapter) recyclerViewListBooking.getAdapter();
                                    adapter.notifyItemRemoved(position);

                                    // Notify the adapter that the data set has changed.
                                    adapter.notifyItemRangeChanged(position, bookingList.size());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User cancelled the dialog
                                    // Here you can run whatever you want to if the user dismisses the dialog
                                    recyclerViewListBooking.getAdapter().notifyItemChanged(position);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (!isSwipeable) {
                    dX = 0; // Ignore swipe when isSwipeable is false
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewListBooking);

        buttonUpcoming = binding.buttonUpcoming;
        buttonLive = binding.buttonLive;
        buttonToday = binding.buttonToday;
        buttonTomorrow = binding.buttonTomorrow;
        text_view_name = binding.textViewName;

        listButton = new ArrayList<>();
        listButton.add(buttonUpcoming);
        listButton.add(buttonLive);
        listButton.add(buttonToday);
        listButton.add(buttonTomorrow);

        buttonUpcoming.setBackground(getResources().getDrawable(R.drawable.background_white_radius_10dp));
        buttonUpcoming.setBackgroundTintList(getResources().getColorStateList(R.color.primaryColor));
        buttonUpcoming.setTextColor(getResources().getColor(R.color.white));

        bookingList = new ArrayList<>();
        boolean showWarning = true;
        isSwipeable = true;
        int selectedButtonId = R.id.button_upcoming;
        List<Booking> upcomingBookingList = bookingService.findUpcomingBookings("active");
        bookingList.addAll(upcomingBookingList);

//        ListViewMatchAdapter listViewMatchAdapter = new ListViewMatchAdapter(getContext(), listMatch);
//        listViewMatch.setAdapter(listViewMatchAdapter);
        RecyclerViewMatchAdapter recyclerViewMatchAdapter = new RecyclerViewMatchAdapter(getContext(), bookingList, showWarning, isSwipeable, selectedButtonId);
        recyclerViewListBooking.setAdapter(recyclerViewMatchAdapter);
        recyclerViewListBooking.setLayoutManager(new LinearLayoutManager(getContext()));

        for (Button button : listButton) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Booking> selectedBookingList = new ArrayList<>();
                    boolean showWarning = false;
                    if (v.getId() == R.id.button_upcoming) {
                        selectedBookingList = bookingService.findUpcomingBookings("active");
                        showWarning = true;
                        isSwipeable = true;
//                        listMatch = new ArrayList<>();
//                        bookingList.add("MatchRecord 1");
//                        bookingList.add("MatchRecord 2");
//                        bookingList.add("MatchRecord 3");
                        // Đặt danh sách này làm dữ liệu cho RecyclerView
//                        RecyclerViewMatchAdapter adapter = (RecyclerViewMatchAdapter) recyclerViewListBooking.getAdapter();
//                        adapter.setBookingList(upcomingBookingList);
//                        adapter.notifyDataSetChanged();
                    } else {
                        if (v.getId() == R.id.button_live) {
                            selectedBookingList = bookingService.findLiveBookings();
                            showWarning = false;
                            isSwipeable = false;
//                            listMatch = new ArrayList<>();
//                            bookingList.add("MatchRecord 1");
//                            bookingList.add("MatchRecord 2");
//                            bookingList.add("MatchRecord 3");
//                            bookingList.add("MatchRecord 4");

//                            ListViewMatchAdapter listViewMatchAdapter = new ListViewMatchAdapter(getContext(), listMatch);
//                            listViewMatch.setAdapter(listViewMatchAdapter);
                        } else {
                            if (v.getId() == R.id.button_today) {
                                // Lấy các trận đấu vào ngày hôm nay
                                Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
                                selectedBookingList = bookingService.findByDate(currentDateTime);
                                showWarning = false;
                                isSwipeable = false;
//                                listMatch = new ArrayList<>();
//                                bookingList.add("MatchRecord 1");
//                                bookingList.add("MatchRecord 2");
//                                bookingList.add("MatchRecord 3");
//                                bookingList.add("MatchRecord 4");
//                                bookingList.add("MatchRecord 5");

//                                ListViewMatchAdapter listViewMatchAdapter = new ListViewMatchAdapter(getContext(), listMatch);
//                                listViewMatch.setAdapter(listViewMatchAdapter);
                            } else {
                                if (v.getId() == R.id.button_tomorrow) {
                                    // Lấy các trận đấu vào ngày mai
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(new Date());
                                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                                    Timestamp nextDayDateTime = new Timestamp(calendar.getTimeInMillis());
                                    selectedBookingList = bookingService.findByDate(nextDayDateTime);
                                    showWarning = false;
                                    isSwipeable = false;
//                                    listMatch = new ArrayList<>();
//                                    bookingList.add("MatchRecord 1");
//                                    bookingList.add("MatchRecord 2");
//                                    bookingList.add("MatchRecord 3");
//                                    bookingList.add("MatchRecord 4");
//                                    bookingList.add("MatchRecord 5");
//                                    bookingList.add("MatchRecord 6");

//                                    ListViewMatchAdapter listViewMatchAdapter = new ListViewMatchAdapter(getContext(), listMatch);
//                                    listViewMatch.setAdapter(listViewMatchAdapter);
                                }
                            }
                        }
                    }
                    bookingList.clear();
                    bookingList.addAll(selectedBookingList);
                    // Tạo một đối tượng RecyclerViewMatchAdapter mới và đặt nó làm adapter cho recyclerViewListBooking
                    RecyclerViewMatchAdapter recyclerViewMatchAdapter = new RecyclerViewMatchAdapter(getContext(), bookingList, showWarning, isSwipeable, v.getId());
                    recyclerViewListBooking.setAdapter(recyclerViewMatchAdapter);
                    recyclerViewListBooking.setLayoutManager(new LinearLayoutManager(getContext()));

                    // Gọi notifyDataSetChanged() trên adapter
                    recyclerViewMatchAdapter.notifyDataSetChanged();

                    button.setBackground(getResources().getDrawable(R.drawable.background_white_radius_10dp));
                    button.setBackgroundTintList(getResources().getColorStateList(R.color.primaryColor));
                    button.setTextColor(getResources().getColor(R.color.white));

                    for (Button otherButton : listButton) {
                        if (otherButton != button) {
                            otherButton.setBackground(getResources().getDrawable(R.drawable.background_border_1dp_radius_10dp));
                            otherButton.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
                            otherButton.setTextColor(getResources().getColor(R.color.blackGray));
                        }
                    }
                }
            });
        }
    }

    public void setWidgets() {
        recyclerViewListBooking = binding.recyclerViewMatch;
        ((AppCompatButton) binding.buttonSeeAllService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).bottomNavigationViewMenu.setSelectedItemId(R.id.menu_option_service);
            }
        });

        listService = new ArrayList<>();
    }

    public void setRecyclerViewListService() {
        recyclerViewListService = binding.recyclerViewListService;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewListService.setLayoutManager(layoutManager);
        recyclerViewListService.addItemDecoration(new RecyclerViewServiceAdapter.StartEndSpaceItemDecoration(55, 20, 55));

        recyclerViewListService.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollHorizontally(1)) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    loadService(10, listService.size(), "Active", 0, "ORDER BY id DESC");
                }
            }
        });

        RecyclerViewServiceAdapter adapter = new RecyclerViewServiceAdapter(this, getContext(), listService, false, getParentFragmentManager());

        recyclerViewListService.setAdapter(adapter);
    }

    public void loadService(int limit, int offset, String status, int isDeleted, String orderBy) {
        if (isLoading) {
            return;
        }

        ServiceServiceImpl service = new ServiceServiceImpl(getContext());
        if (service.countServices(status, isDeleted) == listService.size()) {
            return;
        }

        isLoading = true;
        executorService = Executors.newSingleThreadExecutor();
        binding.progressBar.setVisibility(View.VISIBLE);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final List<Service> listServiceLoad = service.getServicesWithLimitAndOffset(limit, offset, status, isDeleted, orderBy);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listServiceLoad.size() > 0) {
                            listService.addAll(listServiceLoad);
                        }
                        binding.progressBar.setVisibility(View.GONE);
                        recyclerViewListService.getAdapter().notifyDataSetChanged();
                        isLoading = false;
                    }
                });
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}