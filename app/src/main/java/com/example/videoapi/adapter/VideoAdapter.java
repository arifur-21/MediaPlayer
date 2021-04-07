package com.example.videoapi.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videoapi.R;
import com.example.videoapi.modelClass.VideoModel;


import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{
    List<VideoModel> viedeoModels;
    Context context;

    public VideoAdapter(List<VideoModel> viedeoModels, Context context) {
        this.viedeoModels = viedeoModels;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viedeo_item,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        final VideoModel item = viedeoModels.get(position);
        holder.title.setText(item.getTitle());
        holder.duration.setText(item.getDuration());
        Glide.with(context).load(item.getData()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("videoId",item.getId());
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_playerFragment,bundle);

            }
        });

    }

    @Override
    public int getItemCount() {
        return viedeoModels.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title,duration;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_thumbnail);
            title = itemView.findViewById(R.id.tv_title);
            duration = itemView.findViewById(R.id.tv_duration);
        }
    }
}
