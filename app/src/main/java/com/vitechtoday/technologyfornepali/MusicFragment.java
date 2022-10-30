package com.vitechtoday.technologyfornepali;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vitechtoday.technologyfornepali.adapter.MusicListAdapter;
import com.vitechtoday.technologyfornepali.model.AudioTrack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends Fragment  {
public  static  final String CURRENTLY_PLAYING_AUDIO_INFO_KEY = "Currently_played_audio";
public  static  final  String EXTRA_FILELIST_KEY = "extra fileList";
public static  final  String INJECTER_KEY = "injecter";
public  static  final  String PATH_KEY = "path";
public  static  final String ITEM_INDEX_CLICKED_KEY = "itemIndex";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Cursor cursor;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  RecyclerView recyclerView;
private  MusicListAdapter musicListAdapter;
private LinearLayoutManager linearLayoutManager;
private List<AudioTrack> fileList;
private Handler handler;
private MusicListAdapter.OnItemClickListener onItemClickListener = new MusicListAdapter.OnItemClickListener() {
    @Override
    public void itemClick(int position) {
        Intent intent = new Intent(getActivity(), MusicPlayerActivity.class);
        Bundle injector  = new Bundle();
        injector.putSerializable(MusicFragment.EXTRA_FILELIST_KEY, (Serializable) fileList);
        intent.putExtra(MusicFragment.CURRENTLY_PLAYING_AUDIO_INFO_KEY, getString(R.string.dummy_text_string)+ fileList.get(position).getArtist()+" "+ fileList.get(position).getTitle());
        intent.putExtra(MusicFragment.INJECTER_KEY, injector);
        intent.putExtra(MusicFragment.PATH_KEY, fileList.get(position).getPath());
        intent.putExtra(MusicFragment.ITEM_INDEX_CLICKED_KEY, position);
        startActivity(intent);
    }
};

    public MusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicFragment newInstance(String param1, String param2) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_music, container, false);
         recyclerView = view.findViewById(R.id.music_recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                cursor = getContext().getContentResolver().query(MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), new  String[] {MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA}, null, null, MediaStore.Audio.Media.DISPLAY_NAME + " ASC");
                fileList = new ArrayList<AudioTrack>();
                if ((cursor != null)&&(cursor.moveToFirst()))
                    do {
                        fileList.add(new AudioTrack(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)), cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)), cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)), cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)), cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))));
                    }while (cursor.moveToNext());

                musicListAdapter = new MusicListAdapter(fileList, onItemClickListener::itemClick);

                recyclerView.setAdapter(musicListAdapter);

            }
        });

    }

}
