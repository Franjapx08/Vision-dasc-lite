package com.example.vsion_lite;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslCertificate;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomInfoWindowAdapter  implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;

    //private Context context;

    public CustomInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    /*
    public void onCreate() {
        super.onCreate();
        Context mContext = getApplicationContext();
    }
    */



    String idEdificio;
    private String nameR;
    private RequestQueue mq;



    @Override
    public View getInfoContents(final Marker m) {
        idEdificio = "619f4743-4f04-4bc3-95ef-030f1909514a";
        String url = String.format("https://visionliteapi.fjavpra.now.sh/api/v1/edificiosAll/"+idEdificio);
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
                                for (int j = 0; j <=2; j++ ){
                                    j++;
                                    if (j == 1){
                                        System.out.println("respuestaaaaaaa  "+ name);
                                        nameR = name;
                                        System.out.println("asasasasas  " + nameR);
                                        //((TextView)vMacrocentro.findViewById(R.id.info_window_nombre)).setText(name);
                                        //((TextView)vMacrocentro.findViewById(R.id.info_window_nombre)).setText(name);
                                        setInfo(m, nameR);
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            //  Toast.makeText(getApplication(), "No hay respuesta", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        //mq.add(request);
        Volley.newRequestQueue(inflater.getContext().getApplicationContext()).add(request);

        //  mq = Volley.newRequestQueue(getBaseContext().getApplicationContext());
        //Carga layout personalizado.

        return null;
    }

    private View setInfo(Marker m, String nameR) {
        if(m.getTitle().equals("DELE Departamento de Lenguas Extranjeras")){
            System.out.println("asas2e2e2e2e22asasas  " + nameR);
            View vLenguas = inflater.inflate(R.layout.dialog_lenguas, null);
            vLenguas.findViewById(R.id.info_window_nombre);
            String[] infoLienguas = m.getTitle().split("&");
            String urlLenguas = m.getSnippet();
            // nameR
            // System.out.println("valor inf card    " + nameR);
            ((TextView)vLenguas.findViewById(R.id.info_window_nombre)).setText(nameR);
//                ((TextView)v.findViewById(R.id.info_window_nombre)).setText("Lina Cortés");
//                ((TextView)v.findViewById(R.id.info_window_placas)).setText("Placas: SRX32");
            return vLenguas;
        }
        if(m.getTitle().equals("Rectoria UABCS")) {
            View vRectoria = inflater.inflate(R.layout.dialog_lenguas, null);
            String[] infoRectoria = m.getTitle().split("&");
            String urlRectoria = m.getSnippet();
            return vRectoria;
        }
        if(m.getTitle().equals("AD-20 Biblioteca")) {
            View vBiblioteca = inflater.inflate(R.layout.dialog_lenguas, null);
            String[] infoBiblioteca = m.getTitle().split("&");
            String urlBiblioteca = m.getSnippet();
            return vBiblioteca;
        }
        if(m.getTitle().equals("Depto Académico de Sistemas Computacionales")) {
            View vDasc = inflater.inflate(R.layout.dialog_lenguas, null);
            String[] infoDasc = m.getTitle().split("&");
            String urlDasc = m.getSnippet();

            return vDasc;
        }

        if(m.getTitle().equals("AD-46 Macrocentro de cómputo")) {
            final View vMacrocentro = inflater.inflate(R.layout.dialog_lenguas, null);
            //  inflater.getContext().getApplicationContext();

            String[] infoMacrocentro = m.getTitle().split("&");
            String urlMacrocentro = m.getSnippet();
            System.out.println("asasasasas  " + nameR);

            return vMacrocentro;
        }

        if(m.getTitle().equals("Poliforo de la universidad")) {
            View vPoliforo = inflater.inflate(R.layout.dialog_lenguas, null);
            String[] infoPoliforo = m.getTitle().split("&");
            String urlPoliforo = m.getSnippet();

            return vPoliforo;
        }
        return null;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }
}
