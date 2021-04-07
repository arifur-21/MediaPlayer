package com.example.videoapi;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.videoapi.adapter.VideoAdapter;
import com.example.videoapi.databinding.FragmentHomeBinding;
import com.example.videoapi.modelClass.VideoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private List<VideoModel> videoList = new ArrayList<>();
    private VideoAdapter adapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        binding.recycleViewId.setLayoutManager(new GridLayoutManager(getActivity(), 3)); //3 = column count
        adapter = new VideoAdapter(videoList, getActivity());
        binding.recycleViewId.setAdapter(adapter);
        checkPermissions();

    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);

                loadVideos();
            }
            else {

                loadVideos();
            }
        }
    }

    private void loadVideos() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String[] projection = {MediaStore.Video.Media._ID,MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION };
                String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";
                Cursor cursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null,sortOrder );

                if (cursor != null){
                    int idColum = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                    int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                    int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                    while (cursor.moveToNext()){
                        long id = cursor.getLong(idColum);
                        String title = cursor.getString(titleColumn);
                        int duration = cursor.getInt(durationColumn);

                        Uri data = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                        String duration_formatted;
                        int sec = (duration / 1000) % 60;
                        int min = (duration / (1000 * 60)) % 60;
                        int hrs = duration / (1000 * 60 * 60);

                        if (hrs == 0) {
                            duration_formatted = String.valueOf(min).concat(":".concat(String.format(Locale.UK, "%02d", sec)));
                        } else {
                            duration_formatted = String.valueOf(hrs).concat(":".concat(String.format(Locale.UK, "%02d", min).concat(":".concat(String.format(Locale.UK, "%02d", sec)))));
                        }
                        videoList.add(new VideoModel(id, data, title, duration_formatted));
                            adapter.notifyItemChanged(videoList.size() -1);

                    }
                }
            }
        }.start();
    }
}