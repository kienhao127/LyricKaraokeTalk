package com.example.cpu11341_local.lyrickaraoketalk.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cpu11341_local.lyrickaraoketalk.R;
import com.example.cpu11341_local.lyrickaraoketalk.model.Song;

import java.util.ArrayList;

/**
 * Created by CPU11341-local on 29-Dec-17.
 */

public class SongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener mItemClickListener;
    ArrayList<Song> songs = new ArrayList<>();

    public SongListAdapter(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:{
                return new SearchBoxHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_box,parent,false));
            }
            case 1:{
                return new TitleItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.title_item,parent,false));
            }
            default:{
                return new SongItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false));
            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                break;
            case 1:
                TitleItemHolder titleItemHolder = (TitleItemHolder) holder;
                titleItemHolder.title.setText("Bài hát đề cử");
                break;
            default:
                SongItemHolder songItemHolder = (SongItemHolder) holder;
                songItemHolder.artistName.setText(songs.get(position - 2).getArtist());
                songItemHolder.songName.setText(songs.get(position - 2).getName());
                break;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final SongListAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return songs.size() + 2;
    }

    public class SearchBoxHolder extends RecyclerView.ViewHolder {
        EditText searchBox;

        public SearchBoxHolder(View view){
            super(view);
            searchBox = (EditText) view.findViewById(R.id.searchBox);
        }
    }

    public class TitleItemHolder extends RecyclerView.ViewHolder {
        TextView title;

        public TitleItemHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.titleItem);
        }
    }

    public class SongItemHolder extends RecyclerView.ViewHolder {
        TextView songName;
        TextView artistName;

        public SongItemHolder(View view){
            super(view);
            songName = (TextView) view.findViewById(R.id.songName);
            artistName = (TextView) view.findViewById(R.id.artistName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}
