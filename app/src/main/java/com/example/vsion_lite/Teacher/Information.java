package com.example.vsion_lite.Teacher;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import com.example.vsion_lite.Edificios.DatosSalon;
import com.example.vsion_lite.R;
import com.example.vsion_lite.fragment.Data;
import com.example.vsion_lite.fragment.Teacher;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static com.example.vsion_lite.Teacher.Valores.*;

public class Information<CollectionReference> extends AppCompatActivity {

    TextView id, name, email;
    TextView horaT, diasT, ubicacionT, label, materiaT, grupoSemestre, horasT, dispon;
    InfTeacher teacher;
    ImageView sex;

    Spinner spiner;
    ListView datos;
    ArrayList<String> iteraciones;

    private int sexo;
    private List<InfDatos> dat;
    private List<InfTeacher> teacher2;
    private DatabaseReference db;
    String id_maestroG;
    int maestroEn;
    int maestroDia;
    int maestroHor;
    private  ArrayAdapter<String> adapter;
    private int horaI;

    private RequestQueue mq;
    private List<InfDatos> materia;

    int diaFil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        setTitle("Direcctorio Maestros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        spiner = (Spinner) findViewById(R.id.spinner_countries);
        datos = (ListView) findViewById(R.id.datosss);
        datos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 Toast.makeText(getApplicationContext(), "El Elemento seleccionado es posición número: "+position +" El String es: "+spiner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
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
                horarioFil(position, id_maestroG);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        label = (TextView) findViewById(R.id.ubi);
        name = (TextView) findViewById(R.id.iname);
        email = (TextView) findViewById(R.id.iemail);
        sex = (ImageView) findViewById(R.id.sex);
        ubicacionT = (TextView) findViewById(R.id.ub2i);
        materiaT = (TextView) findViewById(R.id.ub3);
        grupoSemestre = (TextView) findViewById(R.id.ub4);
        horasT = (TextView) findViewById(R.id.ub5);
        dispon = (TextView) findViewById(R.id.dispon);

        String song = getIntent().getStringExtra(JSON_SONG);
        teacher = gson.fromJson(song, InfTeacher.class);

        name.setText(teacher.getName());
        email.setText(teacher.getEmail());
        sexo = teacher.getSexo();
        if (sexo !=1 ){
            sex.setImageResource(R.drawable.userteacher2);
        }else {
            sex.setImageResource(R.drawable.userteacher1);
        }

        String id_maestro = teacher.getId();
        id_maestroG = id_maestro;

        mq = Volley.newRequestQueue(getApplication());
        parseJSON(id_maestro);

       // iddd = id_maestro;
        //String idd = id_maestro;
       // db = FirebaseDatabase.getInstance().getReference();
       // db.addChildEventListener(childEventListener);
    }

    public void horarioFil(final int posicion, final String key){
        String url = String.format( "https://visionliteapi.fjavpra.now.sh/api/v1/maestrosMateria/"+key);
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
                                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.resultados, iteraciones);
                                   // adapter.notifyDataSetChanged();
                                }else {
                                    iteraciones.add("No resultados");
                                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.resultados, iteraciones);
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

    private void parseJSON(final String key) {

        String url = String.format( "https://visionliteapi.fjavpra.now.sh/api/v1/maestrosMateria/"+key);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            Toast.makeText(getApplication(),"Hay respuesta",Toast.LENGTH_SHORT).show();
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

                            for (int j = 0; j < materia.size(); j++){
                                InfDatos res = materia.get(j);
                                String horas = res.getHora();
                                String materia = res.getMateria();
                                String ubicacion = res.getUbicacion();
                                String carrera = res.getCarrera();
                                int dia = res.getDia();
                                int horaI = res.getHorainicio();
                                int horaF = res.getHorafinal();
                                if (dia == day){
                                    if (hora >= horaI && hora < horaF){
                                        label.setText("Actualmente se encuentra en:");
                                        ubicacionT.setText(ubicacion);
                                        materiaT.setText(materia);
                                        grupoSemestre.setText("Carrera" + " " + carrera);
                                        horasT.setText("Horario: " + horas);
                                        dispon.setCompoundDrawablesWithIntrinsicBounds(
                                                R.drawable.icon_status_session, //left
                                                0, //top
                                                0, //right
                                                0);//bottom
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplication(),"No hay respuesta",Toast.LENGTH_SHORT).show();
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

    /*
    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
            Log.d(TAG, "onChildAdded:" + dataSnapshot.getChildrenCount());
            // dat = new ArrayList<>();
            teacher2 = new ArrayList<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //calendar
                    Calendar cal = Calendar.getInstance();
                    int hora = cal.get(Calendar.HOUR_OF_DAY);
                    int day = cal.get(Calendar.DAY_OF_WEEK);
                    //datos obtenidos
                    DataU datos = snapshot.getValue(DataU.class);
                    // dat.add(datos);
                    int id_maestro2 = datos.id_maestros;
                    if (id_maestro2 == iddd){
                        maestroEn++;
                        int dia = datos.dia;
                        if (dia == day){
                            maestroDia++;
                            int horaI = datos.horaInicio;
                            int horaF = datos.horaFinal;
                            if (hora >= horaI && hora < horaF ){
                                maestroHor++;
                                String horas = datos.hora;
                                String materia = datos.materia;
                                String ubicacion = datos.ubicacion;
                                String carrera = datos.carrera;
                                String grupo = datos.grupo;
                                String semestre = datos.semestre;
                                label.setText("Actualmente se encuentra en:");
                                ubicacionT.setText(ubicacion);
                                //teacher2.add(new InfTeacher("hola", grupo, horaI, horaF, dia, horas));
                                materiaT.setText(materia);
                                grupoSemestre.setText("Grupo "+ grupo + " - " + semestre + " semestre" + " " + carrera);
                                horasT.setText("Horario: " + horas);
                                dispon.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.icon_status_session, //left
                                        0, //top
                                        0, //right
                                        0);//bottom
                            }
                        }
                    }
                }

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
            Toast.makeText(getApplicationContext(), "Failed to load comments.",
                    Toast.LENGTH_SHORT).show();
        }
    };

    */
/*
    public void horarioFil(int posicion){
        if (diaFil == 0){
        }else if(posicion == 1) {
            //lunes
            diaFil = 2;
            db = FirebaseDatabase.getInstance().getReference();
            db.addChildEventListener(childEventListener2);
        }else if(diaFil == 2) {
            //martes
//           iteraciones.clear();
            diaFil = 3;
            db = FirebaseDatabase.getInstance().getReference();
            db.addChildEventListener(childEventListener2);
        }else if(diaFil == 3) {
            //miercoles
            diaFil = 4;
            db = FirebaseDatabase.getInstance().getReference();
            db.addChildEventListener(childEventListener2);
        }else if(diaFil == 4) {
            //jueves
            diaFil = 5;
            db = FirebaseDatabase.getInstance().getReference();
            db.addChildEventListener(childEventListener2);
        }else if(diaFil == 5) {
            //vieres
            diaFil = 6;
            db = FirebaseDatabase.getInstance().getReference();
            db.addChildEventListener(childEventListener2);
        }
    }

    ChildEventListener childEventListener2 = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            dat = new ArrayList<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataU datos = snapshot.getValue(DataU.class);
                    Artis artist = snapshot.getValue(Artis.class);
                    String name = artist.nombre;
                    String email = artist.email;
                    int id = artist.id_maestro;
                    int sexo = artist.sexo;
                    int id_maestro2 = datos.id_maestros;
                    if (id_maestro2 == iddd ){
                        int dia = datos.dia;
                        String materia = datos.materia;
                        String horas = datos.hora;
                        int horaI = datos.horaInicio;
                        int horaF = datos.horaFinal;
                        int clases2 = datos.clases;
                        String grupo = datos.grupo;
                        String semestre = datos.semestre;
                        String carrera = datos.carrera;
                        String ubicacion = datos.ubicacion;
                        dat.add(new InfDatos(id_maestro2, materia, horas, horaI, horaF, dia, grupo, semestre, carrera, ubicacion, clases2));
                    }
                }
                iteraciones = new ArrayList<>();
                for (int j = 0; j < dat.size(); j++) {
                    InfDatos datosas = dat.get(j);
                        int dia = datosas.getDia();
                        int clases2 = datosas.getClases();
                    if (iteraciones.contains("No resultados")){
                        iteraciones.remove("No resultados");
                    }
                        if (clases2 == 1){
                            if (dia <=  diaFil && dia == diaFil) {
                                String disa = datosas.getMateria();
                                String ubi = datosas.getUbicacion();
                                String horas = datosas.hora();
                                String materia = datosas.getMateria();
                                String ubicacion = datosas.getUbicacion();
                                String carrera = datosas.getCarrera();
                                String grupo = datosas.getGrupo();
                                String semestre = datosas.getSemestre();
                                String diaR = "x";
                                horaI = datosas.getHorainicio();
                                int horaF = datosas.getHorafinal();
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
                                ////////me quede en el 6
                                iteraciones.add("Día: " + diaR);
                                iteraciones.add("Hora:  " + horas);
                                iteraciones.add("Materia:  " + materia);
                                iteraciones.add("Ubicación: " + ubicacion);
                                iteraciones.add("Grupo: " + grupo + " " + carrera + " Semestre " + semestre);
                                iteraciones.add("-----------------------------------------------");
                                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.resultados, iteraciones);
                                adapter.notifyDataSetChanged();

                            }
                        }else {
                            iteraciones.add("No resultados");
                            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.resultados, iteraciones);
                            adapter.notifyDataSetChanged();
                        }
                }
                datos.setAdapter(adapter);
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
            Toast.makeText(getApplicationContext(), "Failed to load comments.",
                    Toast.LENGTH_SHORT).show();
        }
    };


*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed (){

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
