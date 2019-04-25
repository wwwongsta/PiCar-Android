package com.example.picar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picar.activities.CardViewActivity;
import com.example.picar.activities.MainActivity;


import com.example.picar.activities.RideStatusActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


import java.util.ArrayList;
import java.util.List;

public class RecyclerFragment extends Fragment implements OnMapReadyCallback {
    Context context;
    private GoogleMap mMap;
    private MapView mMapView;
    FragmentManager fragmentManager;
    public Fragment newInstace() {
        return new RecyclerFragment();
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);

        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter(list));

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback{
        private CardView mCardView;
        private TextView mTextView_name,mTextView_car,mTextView_rating,mTextView_wait_time;
        private MapView mMapView;
        private GoogleMap mMap;
        public Button select;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container){
            super(inflater.inflate(R.layout.card_view,container, false));
            mCardView = itemView.findViewById(R.id.card_container);
            mTextView_name = itemView.findViewById(R.id.text_name);
            mTextView_car = itemView.findViewById(R.id.text_car);
            mTextView_rating = itemView.findViewById(R.id.text_rating);
            mTextView_wait_time = itemView.findViewById(R.id.text_waiting_time);

            mMapView = itemView.findViewById(R.id.mapView);


            if(mMapView != null){
                mMapView.onCreate(null);
                mMapView.onResume();
                mMapView.getMapAsync(this);
            }


            select = itemView.findViewById(R.id.Accept);
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //SendNotification to driver
                    Intent i = new Intent(getActivity(), RideStatusActivity.class);
                    //add driver id in extra
                    startActivity(i);
                }
            });
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(getContext());
            mMap = googleMap;
            //mMap.setMyLocationEnabled(true);
            //setMapLocation();
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
        private List<String> mList;

        public RecyclerViewAdapter(List<String> list){
            this.mList = list;
        }
        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new RecyclerViewHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
            recyclerViewHolder.mTextView_name.setText(mList.get(i));
            recyclerViewHolder.mTextView_car.setText(mList.get(i));
            recyclerViewHolder.mTextView_rating.setText(mList.get(i));
            recyclerViewHolder.mTextView_wait_time.setText(mList.get(i));

        }

        @Override
        public int getItemCount() {
            //Nombres de driver
            return mList.size();
        }
    }
}
