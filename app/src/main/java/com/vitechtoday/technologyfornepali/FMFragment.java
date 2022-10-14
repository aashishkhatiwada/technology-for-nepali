package com.vitechtoday.technologyfornepali;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vitechtoday.technologyfornepali.data.FmProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FMFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class            FMFragment extends Fragment  {
    private static ProgressDialog progressDialog;
    private  OnFmPlaybackCallback onFmPlaybackCallback;
    private ListView fm_listView;
    private ArrayAdapter<String> fmAdapter;
    private Map<String, String> fmQueryMap;
    private List<String> fmList = new ArrayList<String>();
    private Intent intent;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static FMService fmService;
    private FMService.FmHandler fmBinder;
    private boolean serviceBound;
private    String fm;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceBound = true;

            FMService.FmHandler fmBinder = (FMService.FmHandler) iBinder;
            fmService = fmBinder.getFmService();
            getActivity().startForegroundService(intent);
            fmService.playFM(fmQueryMap.get(fm));
            Log.d("fm fragment", "service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceBound = false;
        }
    };
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FMFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FMFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static FMFragment newInstance(String param1, String param2) {
FMFragment        fragment = new FMFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public  static  FMService getFmService() {
        if (fmService != null)
        return  fmService;
       return  null;
    }
public  static  ProgressDialog  getProgressDialog() {
        if (progressDialog !=null)
            return progressDialog;
            return null;
}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(requireActivity(), FMService.class);

        fmQueryMap = FmProvider.provide();
        fmAdapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, fmList);
        for (String fm : fmQueryMap.keySet()) {
            fmList.add(fm);
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_f_m, container, false);

        fm_listView = view.findViewById(R.id.fmList_ListView);
        fm_listView.setAdapter(fmAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intent.setAction("com.vitechtoday.technologyfornepali.FMService");

progressDialog= new ProgressDialog(getContext());

        fm_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
fm = (String) adapterView.getItemAtPosition(i);
showDialog();
                if (!serviceBound) {
                    getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                }
                if (fmService !=null) {

                    fmService.playFM(fmQueryMap.get(adapterView.getItemAtPosition(i)));

                    fmService.showNotification("playing " + adapterView.getItemAtPosition(i));
                }
        }
        });


    }

    private void showDialog() {
            //progressDialog.setTitle("TechnologyForNepali");
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        // progressDialog.setCancelable(false);
        progressDialog.show();


    }


    public void unbindAndStopService(OnStopCallback callback) {
        if ((serviceBound) && (fmService != null)) {
            getActivity().unbindService(serviceConnection);
            fmService.stopFM();
            fmService.stopSelf();
            if (callback !=null)
            callback.onStop(FMFragment.this);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        //if (serviceBound)
            //getContext().unbindService(serviceConnection);

    }

    @Override
    public void onPause() {
        super.onPause();
    }


}