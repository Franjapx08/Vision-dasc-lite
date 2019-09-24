package com.example.vsion_lite;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vsion_lite.Teacher.InfDatos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class registro extends AppCompatActivity {


    EditText correR, nombreR, sexoR, passR1, passR2;
    Button acep;
    TextView yaTengo;
    boolean pass = true;
    boolean encontro;

    Spinner spiner_carrera, spiner_turno, spiner_sexo;

    String carreraO, nombreO, correoO, passO;
    int sexoO;

    private RequestQueue mq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setTitle("Registro");

        spiner_carrera = (Spinner) findViewById(R.id.spinner_carrera);
      //  spiner_turno = (Spinner) findViewById(R.id.spinner_turno);
        spiner_sexo = (Spinner) findViewById(R.id.spinner_sexo);

        spiner_carrera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    pass = false;
                    carreraO = null;
                }else if (position == 1){
                    carreraO = "3e0fc328-dc80-4409-977e-2b2d397e8889";
                    pass = true;
                }else if (position == 2){
                    carreraO = "4e0be55c-3884-4efb-88d1-11e16913da72";
                    pass = true;
                }else if (position == 3){
                      carreraO = "ad3736aa-1f2b-4a48-aa37-f3bb09af53df";
                    pass = true;
                }else if (position == 4){
                    carreraO = "472fce7a-6360-496d-b3b6-090e4954bb47";
                    pass = true;
                }else if (position == 5){
                    carreraO = "60d0e694-98aa-4d71-976a-7541b6e870ce";
                    pass = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pass = false;
            }
        });

        spiner_sexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    pass = false;
                    sexoO = 0;
                }else if (position == 1 ){
                    sexoO = 1;
                    pass = true;
                }else if (position == 2){
                    sexoO = 2;
                    pass = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pass = false;
            }
        });

        yaTengo = (TextView) findViewById(R.id.yatengo);

        yaTengo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        });

        nombreR = (EditText) findViewById(R.id.nombreR);
        correR = (EditText) findViewById(R.id.correR);
        passR1 = (EditText) findViewById(R.id.contra1);
        passR2 = (EditText) findViewById(R.id.contra2);




        acep = (Button) findViewById(R.id.aceptar_registro);

        acep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(nombreR.getText().toString()) || TextUtils.isEmpty(correR.getText().toString()) || TextUtils.isEmpty(passR1.getText().toString()) || TextUtils.isEmpty(passR2.getText().toString())
                || TextUtils.isEmpty(carreraO) || sexoO == 0){
                    Toast.makeText(getApplicationContext(), "Faltan parametros" , Toast.LENGTH_LONG).show();

                }else {
                    if (passR1.getText().toString().equals(passR2.getText().toString())) {
                        nombreO = nombreR.getText().toString();
                        correoO = correR.getText().toString();
                        passO = passR1.getText().toString();

                        mq = Volley.newRequestQueue(getApplication());
                        // checkUs(correoO, carreraO);
                        String url = String.format("https://visionliteapi.fjavpra.now.sh/api/v1/checkAlum/" + correoO + "/" + carreraO);
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONObject>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray jsonArray = response.getJSONArray("results");
                                            //  Toast.makeText(getApplication(),"Hay respuesta",Toast.LENGTH_SHORT).show();

                                            if (jsonArray.length() >= 1) {
                                                System.out.println("usuario encontrado");
                                                Toast.makeText(getApplication(), "Usuario en uso", Toast.LENGTH_SHORT).show();
                                                encontro = true;
                                            } else {
                                                String url = String.format("https://visionliteapi.fjavpra.now.sh/api/v1/alumnosCreate/" + nombreO + "/" + correoO + "/" + sexoO + "/" + carreraO + "/" + passO);
                                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                                                        new Response.Listener<JSONObject>() {
                                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                                            @Override
                                                            public void onResponse(JSONObject response) {
                                                                try {
                                                                    JSONArray jsonArray = response.getJSONArray("results");
                                                                    //  Toast.makeText(getApplication(),"Hay respuesta",Toast.LENGTH_SHORT).show();
                                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                                        JSONObject hit = jsonArray.getJSONObject(i);
                                                                        String id = hit.getString("id");
                                                                        String name = hit.getString("name");
                                                                        String carreraId = hit.getString("CarreraId");

                                                                    }
                                                                } catch (JSONException e) {
                                                                  //  Toast.makeText(getApplication(), "No hay respuesta", Toast.LENGTH_SHORT).show();
                                                                    e.printStackTrace();
                                                                    System.out.println("registradooo");
                                                                    Toast.makeText(getApplication(), "Registrado con éxito", Toast.LENGTH_SHORT).show();
                                                                    Intent i = new Intent(getApplication(), Login.class);
                                                                    startActivity(i);
                                                                    finish();
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
                                        } catch (JSONException e) {
                                           // Toast.makeText(getApplication(), "No hay respuesta", Toast.LENGTH_SHORT).show();
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

                    }else {
                        Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


}
