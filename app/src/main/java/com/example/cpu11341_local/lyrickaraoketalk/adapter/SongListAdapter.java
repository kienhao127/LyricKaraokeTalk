package com.example.cpu11341_local.lyrickaraoketalk.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
            default:
                SongItemHolder songItemHolder = (SongItemHolder) holder;
                songItemHolder.artistName.setText(songs.get(position - 1).getArtist());
                songItemHolder.songName.setText(songs.get(position - 1).getName());
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
        return songs.size() + 1;
    }

    public class SearchBoxHolder extends RecyclerView.ViewHolder {
        EditText searchBox;
        TextView title;

        public SearchBoxHolder(View view){
            super(view);
            searchBox = (EditText) view.findViewById(R.id.searchBox);
            title = (TextView) view.findViewById(R.id.titleItem);

            searchBox.clearFocus();
            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (searchBox.getText().toString().trim().length() > 0){
                        title.setText("Kết quả tìm kiếm");
                    } else {
                        title.setText("Bài hát đề cử");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
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
