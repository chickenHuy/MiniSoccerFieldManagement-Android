package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.adapter.ViewPagerAdapter;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    ViewPager viewPagerImage;
    String[] arrayImagePath;
    ViewPagerAdapter viewPagerImageAdapter;

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

        setViewPagerImages();
    }

    public void setViewPagerImages() {
        viewPagerImage = binding.viewPager;

        try {
            arrayImagePath = requireActivity().getAssets().list("viewPagerImages");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (arrayImagePath != null) {
            for (int i = 0; i < arrayImagePath.length; i++) {
                arrayImagePath[i] = "viewPagerImages/" + arrayImagePath[i];
            }

            viewPagerImageAdapter = new ViewPagerAdapter(getContext(), arrayImagePath);
            viewPagerImage.setAdapter(viewPagerImageAdapter);

            viewPagerImage.setClipToPadding(false);
            viewPagerImage.setPadding(50, 0, 50, 0);

            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int currentItem = viewPagerImage.getCurrentItem();

                    if (currentItem == arrayImagePath.length - 1) {
                        viewPagerImage.setCurrentItem(0);
                    } else {
                        viewPagerImage.setCurrentItem(currentItem + 1);
                    }

                    handler.postDelayed(this, 3000);
                }
            };

            handler.postDelayed(runnable, 3000);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}