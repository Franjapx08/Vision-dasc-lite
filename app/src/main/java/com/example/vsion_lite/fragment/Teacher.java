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
 * {@link Teacher.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Teacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Teacher extends Fragment implements Adapter.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Teacher() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Teacher.
     */
    // TODO: Rename and change types and number of parameters
    public static Teacher newInstance(String param1, String param2) {
        Teacher fragment = new Teacher();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Teacher newInstance(){
        Teacher fragment = new Teacher();
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

    private RecyclerView mylist;
    private Adapter mAdapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);

       // initializeData();
        mylist = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mylist.setLayoutManager(llm);
        mylist.setHasFixedSize(true);

        mq = Volley.newRequestQueue(getActivity());

        parseJSON();

        return view;
    }


    private void parseJSON() {
        String url = "https://visionliteapi.fjavpra.now.sh/api/v1/maestros";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //   JSONObject jsonObject = null;
                            JSONArray jsonArray = response.getJSONArray("results");
                            
                            teachers2 = new ArrayList<>();
                            for (int i=0; i < jsonArray.length(); i++){
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String name = hit.getString("name");
                                int sexo = hit.getInt("sexo");
                                String correo = hit.getString("corre");
                                String ids = hit.getString("id");
                               // int idsd = Integer.parseInt(id);
                                if (sexo == 1){
                                    teachers2.add(new InfTeacher(name, correo, ids, R.drawable.userteacher1, sexo, "none"));
                                }else {
                                    teachers2.add(new InfTeacher(name, correo, ids, R.drawable.userteacher2, sexo, "none"));
                                }
                            }
                            mAdapter = new Adapter( getContext(),teachers2);
                            mylist.setAdapter(mAdapter);
                            mAdapter.setOnItemClickListener(Teacher.this);
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
    }

    /*
    private void initializeData(){
        db = FirebaseDatabase.getInstance().getReference();
        db.addChildEventListener(childEventListener);
    }
        //orderByChild("nombre_evento").startAt("A");
    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            // Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
            // Log.d(TAG, "onChildAdded:" + dataSnapshot.getChildrenCount());
            datos2 = new ArrayList<>();
            teachers2 = new ArrayList<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count++;
                    DataU datos = snapshot.getValue(DataU.class);
                    Artis artist = snapshot.getValue(Artis.class);
                    String name = artist.nombre;
                    String email = artist.email;
                    int id = artist.id_maestro;
                    int sexo = artist.sexo;

                    int id_maestro2 = datos.id_maestros;
                    String materia = datos.materia;
                    String horas = datos.hora;
                    int horaI = datos.horaInicio;
                    int horaF = datos.horaFinal;
                    int diaBase = datos.dia;
                    String grupo = datos.grupo;
                    String semestre = datos.semestre;
                    String carrera = datos.carrera;
                    String ubicacion = datos.ubicacion;
                    int clases = datos.clases;
                    if (name != null) {
                        if (sexo == 1) {
                            teachers2.add(new InfTeacher(name, email, id, R.drawable.userteacher1, sexo, ubicacion));
                        } else {
                            teachers2.add(new InfTeacher(name, email, id, R.drawable.userteacher2, sexo, ubicacion));
                        }

                    } else {
                        datos2.add(new InfDatos(id_maestro2, materia, horas, horaI, horaF, diaBase, grupo, semestre, carrera, ubicacion, clases));
                    }
                }
                //InfTeacher data = (InfTeacher) teachers2;
                //names.adddata.getName();
                mAdapter = new Adapter( getContext(),teachers2);
                mylist.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(Teacher.this);
                for (int j = 0; j < datos2.size(); j++){
                    InfDatos datosas = datos2.get(j);
                    int idd = datosas.getIdMaestros();
                    if (idd == 3){
                        int dia = datosas.getDia();
                        if (dia == 2){
                            String disa = datosas.getMateria();
                            String ubi = datosas.getUbicacion();
                            System.out.println("Encontre el id del" + idd);
                            System.out.println("Se llama" + disa);
                            System.out.println("Y se ubica en " + ubi);
                        }
                    }else {
                        System.out.println("Pero el dua no");
                    }
                   // System.out.println("asasaigdiywsagfdiuywasdgiuasdgiusad" + disa);
                }
                System.out.println("asasaigdiywsagfdiuywasdgiuasdgiusad");
            }

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
            String commentKey = dataSnapshot.getKey();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            Toast.makeText(getContext(), "Failed to load comments.",
                    Toast.LENGTH_SHORT).show();
        }
    };

    */
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
    public void onItemClick(int position) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
