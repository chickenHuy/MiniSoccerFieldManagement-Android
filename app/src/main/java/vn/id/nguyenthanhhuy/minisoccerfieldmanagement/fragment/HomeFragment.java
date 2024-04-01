package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.R;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ListViewMatchAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.RecyclerViewServiceAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ViewPagerAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.application.MainApplication;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ViewPager viewPagerImage;
    private String[] imagePathArray;
    private ViewPagerAdapter viewPagerImageAdapter;
    private int duration = 7000;

    private Button buttonUpcoming;
    private Button buttonLive;
    private Button buttonToday;
    private Button buttonTomorrow;
    private ArrayList<Button> listButton;
    private ListView listViewMatch;

    private RecyclerView recyclerViewListService;

    private TextView text_view_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

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
        List<String> listMatch = new ArrayList<>();
        listMatch.add("MatchRecord 1");
        listMatch.add("MatchRecord 2");
        listMatch.add("MatchRecord 3");

        ListViewMatchAdapter listViewMatchAdapter = new ListViewMatchAdapter(getContext(), listMatch);
        listViewMatch.setAdapter(listViewMatchAdapter);

        for (Button button : listButton) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.button_upcoming) {
                        List<String> listMatch = new ArrayList<>();
                        listMatch.add("MatchRecord 1");
                        listMatch.add("MatchRecord 2");
                        listMatch.add("MatchRecord 3");

                        ListViewMatchAdapter listViewMatchAdapter = new ListViewMatchAdapter(getContext(), listMatch);
                        listViewMatch.setAdapter(listViewMatchAdapter);
                    } else {
                        if (v.getId() == R.id.button_live) {
                            List<String> listMatch = new ArrayList<>();
                            listMatch.add("MatchRecord 1");
                            listMatch.add("MatchRecord 2");
                            listMatch.add("MatchRecord 3");
                            listMatch.add("MatchRecord 4");

                            ListViewMatchAdapter listViewMatchAdapter = new ListViewMatchAdapter(getContext(), listMatch);
                            listViewMatch.setAdapter(listViewMatchAdapter);
                        } else {
                            if (v.getId() == R.id.button_today) {
                                List<String> listMatch = new ArrayList<>();
                                listMatch.add("MatchRecord 1");
                                listMatch.add("MatchRecord 2");
                                listMatch.add("MatchRecord 3");
                                listMatch.add("MatchRecord 4");
                                listMatch.add("MatchRecord 5");

                                ListViewMatchAdapter listViewMatchAdapter = new ListViewMatchAdapter(getContext(), listMatch);
                                listViewMatch.setAdapter(listViewMatchAdapter);
                            } else {
                                if (v.getId() == R.id.button_tomorrow) {
                                    List<String> listMatch = new ArrayList<>();
                                    listMatch.add("MatchRecord 1");
                                    listMatch.add("MatchRecord 2");
                                    listMatch.add("MatchRecord 3");
                                    listMatch.add("MatchRecord 4");
                                    listMatch.add("MatchRecord 5");
                                    listMatch.add("MatchRecord 6");

                                    ListViewMatchAdapter listViewMatchAdapter = new ListViewMatchAdapter(getContext(), listMatch);
                                    listViewMatch.setAdapter(listViewMatchAdapter);
                                }
                            }
                        }
                    }

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
        listViewMatch = binding.listViewMatch;
    }

    public void setRecyclerViewListService() {
        recyclerViewListService = binding.recyclerViewListService;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewListService.setLayoutManager(layoutManager);
        recyclerViewListService.addItemDecoration(new RecyclerViewServiceAdapter.StartEndSpaceItemDecoration(55, 20, 55));

        List<String> serviceList = new ArrayList<>();
        serviceList.add("Service 1");
        serviceList.add("Service 2");
        serviceList.add("Service 3");
        serviceList.add("Service 4");
        serviceList.add("Service 5");
        serviceList.add("Service 6");
        serviceList.add("Service 7");

        RecyclerViewServiceAdapter adapter = new RecyclerViewServiceAdapter(getContext(), serviceList, false);

        recyclerViewListService.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}