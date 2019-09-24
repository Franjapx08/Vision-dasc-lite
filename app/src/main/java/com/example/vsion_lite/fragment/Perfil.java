package com.example.vsion_lite.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vsion_lite.Login;
import com.example.vsion_lite.R;
import com.example.vsion_lite.Teacher.InfDatos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Perfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Perfil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Perfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static Perfil newInstance(String param1, String param2) {
        Perfil fragment = new Perfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Perfil newInstance(){
        Perfil fragment = new Perfil();
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


    TextView nameP, carrerP, correoP ;
    Button off;
    Spinner spinner_alum;


    ArrayList<String> iteraciones;
    int diaFil;
    String idCarrera;
    ListView datos;

    private RequestQueue mq;

    private List<InfDatos> materia;
    private  ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        nameP = (TextView) view.findViewById(R.id.namePerfil);
        carrerP = (TextView) view.findViewById(R.id.carreraPerfil);
        correoP = (TextView) view.findViewById(R.id.correoPerfil);


        spinner_alum = (Spinner) view.findViewById(R.id.spinner_alum);
        datos = (ListView) view.findViewById(R.id.datosP);




        SharedPreferences sharedPref = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        String name = sharedPref.getString("NameD", "");
        String carrera = sharedPref.getString("CarreraId", "");
        String correo = sharedPref.getString("CorreoD", "");


        if (carrera.equals("3e0fc328-dc80-4409-977e-2b2d397e8889") || carrera.equals("4e0be55c-3884-4efb-88d1-11e16913da72")){
            carrerP.setText("IDS");
        }else if (carrera.equals("ad3736aa-1f2b-4a48-aa37-f3bb09af53df") || carrera.equals("472fce7a-6360-496d-b3b6-090e4954bb47")){
            carrerP.setText("ITC");
        }else if (carrera.equals("60d0e694-98aa-4d71-976a-7541b6e870ce")){
            carrerP.setText("LATI & TSUATI");
        }

        idCarrera = carrera;
        nameP.setText(name);
        correoP.setText(correo);




        spinner_alum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //    Toast.makeText(getContext(), "El Elemento seleccionado es posición número: "+position +" El String es: "+spinner_alum.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                diaFil = position;
                if(position == 1){
                    position = 2;
                }else if (position == 2){
                    position = 3;
                }else if (position == 3){
                    position = 4;
                }else if (position == 4){
                    position = 5;
                } else if (position == 5) {
                    position = 6;
                }
                horarioFil(position, idCarrera);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        mq = Volley.newRequestQueue(getActivity());


        off = (Button) view.findViewById(R.id.off);

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferencias = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("NameD", "");
                editor.putString("CorreoD", "");
                editor.putString("ID", "");
                editor.putString("CarreraId", "");
                editor.putInt("SexoD" , 0);
                editor.putInt("LOG" , 2);
                editor.apply();
                Toast.makeText(getContext(), "cerro sessión", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getContext(), Login.class);
                startActivity(i);
                getActivity().finish();
            }
        });


//        nameP.setText(nameD);

        return view;
    }

    public void horarioFil(final int posicion, final String key){
        String url = String.format( "https://visionliteapi.fjavpra.now.sh/api/v1/materiasAlum/"+key);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            // Toast.makeText(getApplication(),"Hay respuesta",Toast.LENGTH_SHORT).show();
                            materia = new ArrayList<>();
                            for (int i=0; i < jsonArray.length(); i++){
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String idMateria = hit.getString("id");
                                String materiaName = hit.getString("name");
                                String hora = hit.getString("hora");
                                int horaI = hit.getInt("horaI");
                                int horaF = hit.getInt("horaF");
                                int dia = hit.getInt("dia");
                                JSONObject Ubicacion = hit.getJSONObject("Salon");
                                JSONObject Carrera = hit.getJSONObject("Carrera");

                                String salon = Ubicacion.getString("name_salon");
                                String carrera = Carrera.getString("name_carrera");

                                materia.add(new InfDatos(idMateria, materiaName, hora, horaI, horaF, dia, carrera, key, salon));

                            }

                            Calendar cal = Calendar.getInstance();
                            int hora = cal.get(Calendar.HOUR_OF_DAY);
                            int day = cal.get(Calendar.DAY_OF_WEEK);

                            iteraciones = new ArrayList<>();
                            for (int j = 0; j < materia.size(); j++){
                                InfDatos res = materia.get(j);
                                String horas = res.getHora();
                                String materia = res.getMateria();
                                String ubicacion = res.getUbicacion();
                                String carrera = res.getCarrera();
                                int dia = res.getDia();
                                int horaI = res.getHorainicio();
                                int horaF = res.getHorafinal();
                                String diaR = "x";
                                if (iteraciones.contains("No resultados")){
                                    iteraciones.remove("No resultados");
                                }
                                if (dia == posicion){

                                    if (dia == 2){
                                        diaR = "lunes";
                                    }else if (dia == 3) {
                                        diaR = "martes";
                                    }else if (dia == 4){
                                        diaR = "miercoles";
                                    }else if (dia == 5){
                                        diaR = "jueves";
                                    }else if (dia == 6){
                                        diaR = "viernes";
                                    }
                                    iteraciones.add("Día: " + diaR);
                                    iteraciones.add("Hora:  " + horas);
                                    iteraciones.add("Materia:  " + materia);
                                    iteraciones.add("Ubicación: " + ubicacion);
                                    iteraciones.add("Carrera: " + carrera);
                                    iteraciones.add("-----------------------------------------------");
                                    adapter = new ArrayAdapter<String>(getContext(), R.layout.resultados, iteraciones);
                                    // adapter.notifyDataSetChanged();
                                }else {
                                    iteraciones.add("No resultados");
                                    adapter = new ArrayAdapter<String>(getContext(), R.layout.resultados, iteraciones);
                                    // adapter.notifyDataSetChanged();
                                }
                            }
                            datos.setAdapter(adapter);
                        } catch (JSONException e) {
                            //Toast.makeText(getApplication(),"No hay respuesta",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mq.add(request);
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
