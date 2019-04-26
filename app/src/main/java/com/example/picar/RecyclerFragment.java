package com.example.picar;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
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


import com.example.picar.activities.MapsActivity;
import com.example.picar.activities.RideStatusActivity;
import com.example.picar.database.AppDatabase;
import com.example.picar.database.entity.Position;
import com.example.picar.database.entity.Transit;
import com.example.picar.database.entity.User;
import com.example.picar.directionHelpers.FetchUrl;
import com.example.picar.directionHelpers.FetchUrlCardView;
import com.example.picar.directionHelpers.TaskLoadedCallback;
import com.example.picar.directionHelpers.TaskLoadedCallbackCardView;
import com.example.picar.retrofit.PiCarApi;
import com.example.picar.retrofit.model.DriverId;
import com.example.picar.retrofit.model.DriverInfoForTransit;
import com.example.picar.retrofit.model.PassengerID;
import com.example.picar.retrofit.model.PositionDestination;
import com.example.picar.retrofit.model.StatusUpdateResponse;
import com.example.picar.retrofit.model.type_message.Message;
import com.example.picar.retrofit.model.user_type.UserInfo;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerFragment extends Fragment implements OnMapReadyCallback {
    Context context;
    private GoogleMap mMap;
    private MapView mMapView;
    FragmentManager fragmentManager;
    private PiCarApi api;
    private List<UserInfo> driverList = new ArrayList<>();
    public Fragment newInstace() {
        return new RecyclerFragment();
    }
    RecyclerViewAdapterUserInfo adapteur;
    String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWJqZWN0IjoiNWNhYmE2YmUwOWJhYjkyN2QxMWIwMTRhIiwiaWF0IjoxNTU1MzM2MDkwfQ.Ivk36K7629DVF_oSCeDqNO_N_DhDS8n37_mN09qmHXE";
    String userId = "";
    public void isListUSerArroudEmpty(User user){
            PositionDestination posDes = new PositionDestination(user.getCurrent_position_id(), user.getDestination_id());
            Call<List<DriverId>> call = api.getNearByDriver(posDes);
            call.enqueue(new Callback<List<DriverId>>() {
                @Override
                public void onResponse(Call<List<DriverId>> call, Response<List<DriverId>> response) {
                    List<DriverId> list = response.body();
                    if(response.body() != null){
                        for (DriverId driver:list) {
                            Call<UserInfo> call2 = api.getUserById(driver.getDriverId());
                            call2.enqueue(new Callback<UserInfo>() {
                                @Override
                                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                                    UserInfo aUser = response.body();
                                    driverList.add(aUser);
                                    adapteur.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<UserInfo> call, Throwable t) {

                                }
                            });
                        }
                    }else{
                        noUserArround();
                    }
                }

                @Override
                public void onFailure(Call<List<DriverId>> call, Throwable t) {

                }
            });
    }
    public void noUserArround(){
        String id = "5cc0cba41b30070017e13fc2";
        Call<Transit> callTest = api.getTransit(id);
        callTest.enqueue(new Callback<Transit>() {
            @Override
            public void onResponse(Call<Transit> call, Response<Transit> response) {
                Transit aUser = response.body();
                Call<UserInfo> call2 = api.getUserById(aUser.getDriverID());
                call2.enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        UserInfo aUser = response.body();
                        driverList.add(aUser);
                        adapteur.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {

                    }
                });
            }
            @Override
            public void onFailure(Call<Transit> call, Throwable t) {

            }
        });
    }
    public class GetTransit extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
        AppDatabase db ;
        public GetTransit() {
            this.db = AppDatabase.getInstance(getContext());
        }
        @Override
        protected Void doInBackground(Void... params) {
            User user = db.userDao().getUser();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://cryptic-stream-69346.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(PiCarApi.class);
            userId = user.get_id();
            isListUSerArroudEmpty(user);
//            PositionDestination posDes = new PositionDestination(user.getCurrent_position_id(), user.getDestination_id());
//            Call<List<DriverId>> call = api.getNearByDriver(posDes);
//            call.enqueue(new Callback<List<DriverId>>() {
//                @Override
//                public void onResponse(Call<List<DriverId>> call, Response<List<DriverId>> response) {
//                    List<DriverId> list = response.body();
//                    for (DriverId driver:list) {
//                        Call<UserInfo> call2 = api.getUserById(driver.getDriverID());
//                        call2.enqueue(new Callback<UserInfo>() {
//                            @Override
//                            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
//                                UserInfo aUser = response.body();
//                                driverList.add(aUser);
//                                adapteur.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onFailure(Call<UserInfo> call, Throwable t) {
//
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<DriverId>> call, Throwable t) {
//
//                }
//            });
            return null;
        }

        @Override
        protected void onCancelled() {
        }
    }
    private String getUrl(LatLng current, LatLng destination, String directionMode) {
        String str_origin = "origin=" + current.latitude + "," + current.longitude;
        // Destination of route
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;

//        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(new RecyclerViewAdapter(list));
    }

    private void setUpMarkers(MarkerOptions mCurrentMarkerOptions,MarkerOptions mDestinationMarkerOptions,
                              Position mCurrentAddress,Position mDestinationAddress,
                              Context context,GoogleMap googleMap) {

        mCurrentMarkerOptions = new MarkerOptions().position(new LatLng(mCurrentAddress.getLat(), mCurrentAddress.getLng()));
        mDestinationMarkerOptions = new MarkerOptions().position(new LatLng(mDestinationAddress.getLat(), mDestinationAddress.getLng()));
        ArrayList<MarkerOptions> markers = new ArrayList<>();
        markers.add(mCurrentMarkerOptions);
        markers.add(mDestinationMarkerOptions);
        new FetchUrlCardView(context,googleMap).execute(getUrl(mCurrentMarkerOptions.getPosition(), mDestinationMarkerOptions.getPosition(), "driving"), "driving");

        //show all marker on the map
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        googleMap.animateCamera(cu);
    }

    public void getPositionFromPosId(String idPos,String idDestination,GoogleMap googleMap,
                                     MarkerOptions mCurrentMarkerOptions,MarkerOptions mDestinationMarkerOptions){
        if(idPos == null){
            return;
        }

        Call<Position> call = api.getPosition(token,idPos);
        call.enqueue(new Callback<Position>() {
            @Override
            public void onResponse(Call<Position> call, Response<Position> response) {
                Position pos = response.body();
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(pos.getLat(), pos.getLng()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
                Call<Position> call2 = api.getPosition(token,idDestination);
                call2.enqueue(new Callback<Position>() {
                    @Override
                    public void onResponse(Call<Position> call, Response<Position> response) {
                        Position des = response.body();
                        setUpMarkers(mCurrentMarkerOptions,mDestinationMarkerOptions,pos,des,RecyclerFragment.this.context,googleMap);
                    }

                    @Override
                    public void onFailure(Call<Position> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Position> call, Throwable t) {

            }
        });
   }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);

//        List<String> list = new ArrayList<>();
//        list.add("one");
//        list.add("two");
        GetTransit tr = new GetTransit();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapteur = new RecyclerViewAdapterUserInfo(driverList);

        recyclerView.setAdapter(adapteur);
        tr.execute();

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
        MarkerOptions mCurrentMarkerOptions;
        MarkerOptions mDestinationMarkerOptions;
        String driverId = "";
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

                    PassengerID pass = new PassengerID(userId);
                    Call<StatusUpdateResponse> callDelPassager = api.delPassager(driverId,pass);
                    callDelPassager.enqueue(new Callback<StatusUpdateResponse>() {
                        @Override
                        public void onResponse(Call<StatusUpdateResponse> call, Response<StatusUpdateResponse> response) {
                            Call<Transit> callAdPassager = api.addPassager(driverId,pass);
                            callAdPassager.enqueue(new Callback<Transit>() {
                                @Override
                                public void onResponse(Call<Transit> call, Response<Transit> response) {
                                    Intent i = new Intent(getActivity(), RideStatusActivity.class);
                                    i.putExtra("id_driver",driverId);
                                    i.putExtra("id_user",userId);
                                    //add driver id in extra
                                    startActivity(i);
                                }

                                @Override
                                public void onFailure(Call<Transit> call, Throwable t) {
                                    getActivity().finish();
                                }
                            });
                        }
                        @Override
                        public void onFailure(Call<StatusUpdateResponse> call, Throwable t) {
                        }
                    });

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

    private class RecyclerViewAdapterUserInfo extends RecyclerView.Adapter<RecyclerViewHolder>{
        private List<UserInfo> mList;

        public RecyclerViewAdapterUserInfo(List<UserInfo> list){
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
            recyclerViewHolder.mTextView_name.setText(mList.get(i).getUser_info().getName());
            recyclerViewHolder.mTextView_car.setText(mList.get(i).getUser_info().getEmail());
            recyclerViewHolder.mTextView_rating.setText(mList.get(i).getUser_info().getPhone());
            recyclerViewHolder.mTextView_wait_time.setText(mList.get(i).getUser_info().get_id());
            recyclerViewHolder.driverId = mList.get(i).getUser_info().get_id();
            recyclerViewHolder.mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    getPositionFromPosId(mList.get(i).getUser_info().getCurrent_position_id(),mList.get(i).getUser_info().getDestination_id(),googleMap,
                            recyclerViewHolder.mCurrentMarkerOptions,recyclerViewHolder.mDestinationMarkerOptions);
                }
            });
        }

        @Override
        public int getItemCount() {
            //Nombres de driver
            return mList.size();
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
