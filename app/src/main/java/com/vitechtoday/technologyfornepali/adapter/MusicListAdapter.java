package com.vitechtoday.technologyfornepali.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vitechtoday.technologyfornepali.R;
import com.vitechtoday.technologyfornepali.model.AudioTrack;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter <MusicListAdapter.ViewHolder>{
    public  interface  OnItemClickListener {
        void  itemClick(int position);
    }
    public  class  ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.music_textView);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.itemClick(getAdapterPosition()  );
        }



    }
private  OnItemClickListener onItemClickListener;
    private  List<AudioTrack> fileList;
    public  MusicListAdapter(List<AudioTrack> fileList, OnItemClickListener onItemClickListener) {
        this.fileList = fileList;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fm_row, parent, false);
        return  new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(fileList.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        return  fileList.size();
    }
    public  void  setFileList(List<AudioTrack> fileList) {
        this.fileList = fileList;
        notifyDataSetChanged();
    }













}
