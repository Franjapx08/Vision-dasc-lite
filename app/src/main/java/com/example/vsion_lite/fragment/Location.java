package com.example.vsion_lite.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vsion_lite.CustomInfoWindowAdapter;
import com.example.vsion_lite.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Location.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Location#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Location extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private GoogleMap mMap;
    private android.location.Location loc;
    private LocationManager locManager;
    SupportMapFragment mapFragment;
    private LatLng myLocation;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Location() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Location.
     */
    // TODO: Rename and change types and number of parameters
    public static Location newInstance(String param1, String param2) {
        Location fragment = new Location();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Location newInstance(){
        Location fragment = new Location();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView info_window_nombre;
    boolean dale = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_location, container, false);
        info_window_nombre = (TextView) view.findViewById(R.id.info_window_nombre);

        if (dale){
            info_window_nombre.setText("perro");
        }
        init();
        return view;
    }

    private void init() {
        mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mapFragment = SupportMapFragment.newInstance();
        fragmentTransaction.replace(R.id.map, mapFragment).commit();

        mapFragment.getMapAsync(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        final Context context = getActivity().getApplicationContext();
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck == PackageManager.PERMISSION_DENIED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);

       // LocationManager lm= (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
      //  lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, (android.location.LocationListener) this);


        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        myLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
//        LatLng sydney = new LatLng(-34, 151);

        final CameraPosition camPos = new CameraPosition.Builder()
                .target(myLocation)   //Centramos el mapa en Madrid
                .zoom(18)         //Establecemos el zoom en 19
                .bearing(0)      //Establecemos la orientación con el noreste arriba
                .tilt(90)         //Bajamos el punto de vista de la cámara 70 grados
                .build();
        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);

        //Add a marker in Sydney and move the camera
        //mMap.setBuiltInZoomControls(true);

        final LatLng lenguas = new LatLng(24.100405, -110.316734);
        mMap.addMarker(new MarkerOptions().anchor(0.0f, 1.0f).position(lenguas).title("DELE Departamento de Lenguas Extranjeras"));
     //   dale = true;
        final LatLng rectoria = new LatLng(24.101110, -110.316664);
        mMap.addMarker(new MarkerOptions().anchor(0.0f, 1.0f).position(rectoria).title("Rectoria UABCS"));

        final LatLng biblioteca = new LatLng(24.101849, -110.316296);
        mMap.addMarker(new MarkerOptions().anchor(0.0f, 1.0f).position(biblioteca).title("AD-20 Biblioteca"));

        final LatLng dasc = new LatLng(24.102437, -110.316264);
        mMap.addMarker(new MarkerOptions().anchor(0.0f, 1.0f).position(dasc).title("Depto Académico de Sistemas Computacionales"));

        final LatLng macrocentro = new LatLng(24.102775, -110.316076);
        mMap.addMarker(new MarkerOptions().anchor(0.0f, 1.0f).position(macrocentro).title("AD-46 Macrocentro de cómputo"));

        final LatLng poliforo = new LatLng(24.103196, -110.315867);
        mMap.addMarker(new MarkerOptions().anchor(0.0f, 1.0f).position(poliforo).title("Poliforo de la universidad"));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(getActivity())));
                //new AlertDialog.Builder(getContext()).setView(R.layout.prueba_emergente).show();
                //Toast.makeText(getContext(), "Pulsaste aqui: \n Lat: "+sydney.latitude+" long: "+sydney.longitude, Toast.LENGTH_LONG).show();

                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(this);



    }

    @Override
    public void onInfoWindowClick(Marker marker) {


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.logout, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                getActivity().onBackPressed();
                return true;

        }

        return false;
    }


}
