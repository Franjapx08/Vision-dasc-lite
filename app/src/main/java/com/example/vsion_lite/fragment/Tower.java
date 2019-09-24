package com.example.vsion_lite.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vsion_lite.Edificios.Adapter_edi;
import com.example.vsion_lite.Edificios.DatosEdificios;
import com.example.vsion_lite.Edificios.DatosSalon;
import com.example.vsion_lite.R;
import com.example.vsion_lite.Teacher.Adapter;
import com.example.vsion_lite.Teacher.Artis;
import com.example.vsion_lite.Teacher.DataU;
import com.example.vsion_lite.Teacher.InfDatos;
import com.example.vsion_lite.Teacher.InfTeacher;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tower.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tower#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tower extends Fragment implements Adapter_edi.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tower() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tower.
     */
    // TODO: Rename and change types and number of parameters
    public static Tower newInstance(String param1, String param2) {
        Tower fragment = new Tower();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Tower newInstance(){
        Tower fragment = new Tower();
        Bundle args = new Bundle();
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

    private RecyclerView mylist;
    private Adapter_edi mAdapter;

    private List<InfTeacher> teachers2;
    private List<InfDatos> datos2;

    private ArrayList<DataU> dat ;
    private ArrayList<Artis> maes ;
    private int count;
    private int idd;
    boolean next = false;

    private DatabaseReference db;
    private List<InfTeacher> names = new ArrayList<>();
    private SearchView searchView;

    private RequestQueue mq;

    private ArrayList<DatosEdificios> datosEd;
    private ArrayList<DatosSalon> datoSalon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tower, container, false);



        mylist = (RecyclerView) view.findViewById(R.id.list_edi);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mylist.setLayoutManager(llm);
        mylist.setHasFixedSize(true);

      /*  initializeData();
        initializeAdapter();
        */

        mq = Volley.newRequestQueue(getActivity());

        parseJSON();
        return view;
    }

    private void initializeData(){
        teachers2 = new ArrayList<>();
        //teachers2.add(new InfTeacher("xd", "xd", 1, R.drawable.userteacher2, 1, "x"));
    }


    private void parseJSON() {

        String url = "https://visionliteapi.fjavpra.now.sh/api/v1/salonEdi";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //   JSONObject jsonObject = null;
                            JSONArray jsonArray = response.getJSONArray("results");
                           // 
                            teachers2 = new ArrayList<>();
                            datoSalon = new ArrayList<>();
                            for (int i=0; i < jsonArray.length(); i++){
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String name = hit.getString("name");
                                String ids = hit.getString("id");
                                JSONObject edi = hit.getJSONObject("Edificio");
                                String ubi = edi.getString("name_edi");
                               // String id_edi = hit.getString("EdificioId");
                                // int idsd = Integer.parseInt(id);
                                   teachers2.add(new InfTeacher(name, ubi, ids, R.drawable.tower1, 1, "none"));

                                //datoSalon.add(new DatosSalon(ids, name, ids));

                            }



/*
                            teachers2 = new ArrayList<>();
                            //System.out.println("FOOOOOOR");
                            for (int i = 0; i < datoSalon.size(); i++) {
                                // System.out.println("entrooooo");
                                DatosSalon datos = datoSalon.get(i);
                                String names = datos.nombre;
                                String idss = datos.id;
                                //System.out.println("NOMBREEEEEEE" + names);
                                teachers2.add(new InfTeacher(names, names, idss, R.drawable.tower1, 1, "none"));

                                mAdapter = new Adapter_edi(getContext(), teachers2);
                                mylist.setAdapter(mAdapter);
                                mAdapter.setOnItemClickListener(Tower.this);
                            }
*/


                            mAdapter = new Adapter_edi( getContext(),teachers2);
                            mylist.setAdapter(mAdapter);
                            mAdapter.setOnItemClickListener(Tower.this);

                        } catch (JSONException e) {
                            // Toast.makeText(getActivity(),"No hay respuesta1",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }


                    }



                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getActivity(),"No hay respuesta",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        mq.add(request);



      //  datoSalon.add(new DatosSalon("perro", "oerro", "asas"));
/*
        teachers2 = new ArrayList<>();
        System.out.println("FOOOOOOR");
        for (int i = 0; i < datoSalon.size(); i++){
            System.out.println("entrooooo");
            DatosSalon datos = datoSalon.get(i);
            String name = datos.nombre;
            String ids = datos.id;
            System.out.println("NOMBREEEEEEE" + name);
            teachers2.add(new InfTeacher(name, name, ids, R.drawable.tower1, 1, "none"));

            mAdapter = new Adapter_edi( getContext(),teachers2);
            mylist.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(Tower.this);
        }
*/
    }

    private void initializeAdapter(){
        mAdapter = new Adapter_edi( getContext(),teachers2);
        mylist.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(Tower.this);
    }















    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) searchItem.getActionView();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                /*                FirebaseAuth.getInstance().signOut();
                getActivity().onBackPressed();
                      */
                searchView = (android.support.v7.widget.SearchView) item.getActionView();
                searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
                return true;

        }

        return false;
    }

    @Override
    public void onItemClick(int position) {

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
}
