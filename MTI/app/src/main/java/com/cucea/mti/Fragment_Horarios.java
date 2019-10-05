package com.cucea.mti;

/**
 * Created by Arturo on 1/26/2017.
 */

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Horarios extends Fragment {

    ViewPager pager;
    PagerTabStrip tab_strp;
    DownloadURL downloadURL;
    ProgressDialog pDialog;
    TareaAsincrona tarea2;
    View rootView;

    public Fragment_Horarios() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment__horarios, container, false);
        downloadURL = new DownloadURL();

        if (downloadURL.isConnected(getActivity())) {
            Boolean b = ((Variables) getActivity().getApplication()).getVisited();
            tab_strp = (PagerTabStrip)rootView.findViewById(R.id.tab_strip);
            tab_strp.setTextColor(Color.WHITE);
            tab_strp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tab_strp.setTabIndicatorColor(Color.LTGRAY);

            pager = (ViewPager)rootView.findViewById(R.id.pager);
            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
            tab_adapter mapager = new tab_adapter(fragmentManager);
            pager.setAdapter(mapager);

            if(b == true){ //Ya entro antes...
                //Ya no descargar de nuevo
            } else { //No ha entrado todavia
                //Descargar contenido
                pDialog = new ProgressDialog(getActivity());
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setMessage("Descargando contenido...");
                pDialog.setCancelable(false);
                pDialog.setMax(100);

                tarea2 = new TareaAsincrona();
                tarea2.execute();

                ((Variables) getActivity().getApplication()).setVisited(true);
            }

        } else {
            downloadURL.MsgInternet(getActivity());
        }
        return rootView;
    }

    public void metodoPrincipal(){
        downloadURL.UniversalImageLoader(getActivity());
        SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", MODE_PRIVATE);
        String id_alumno = prefs.getString("id_alumno", "0");

        String url = "http://agavia.com.mx/proyectos/siip/app/horarios/getHorarioById_Alumno.php?id_alumno="+id_alumno;
        JSONArray data = downloadURL.getJson(url);

        try {
            HashMap<String, String> map;
            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<>();

                if(c.has("lunes_inicio")){
                    map.put("lunes_inicio", c.getString("lunes_inicio"));
                    map.put("lunes_fin", c.getString("lunes_fin"));
                    map.put("inicio_ciclo", c.getString("inicio_ciclo"));
                    map.put("fin_ciclo", c.getString("fin_ciclo"));
                    map.put("Nombre_Asignatura", c.getString("Nombre_Asignatura"));
                    map.put("NRC", c.getString("NRC"));
                    map.put("Nombre_Docente", c.getString("Nombre_Docente"));
                    map.put("Salon", c.getString("Salon"));

                    ((Variables) getActivity().getApplication()).setListLun(map);
                }
                if(c.has("martes_inicio")){
                    map.put("martes_inicio", c.getString("martes_inicio"));
                    map.put("martes_fin", c.getString("martes_fin"));
                    map.put("inicio_ciclo", c.getString("inicio_ciclo"));
                    map.put("fin_ciclo", c.getString("fin_ciclo"));
                    map.put("Nombre_Asignatura", c.getString("Nombre_Asignatura"));
                    map.put("NRC", c.getString("NRC"));
                    map.put("Nombre_Docente", c.getString("Nombre_Docente"));
                    map.put("Salon", c.getString("Salon"));

                    ((Variables) getActivity().getApplication()).setListMar(map);
                }
                if(c.has("miercoles_inicio")){
                    map.put("miercoles_inicio", c.getString("miercoles_inicio"));
                    map.put("miercoles_fin", c.getString("miercoles_fin"));
                    map.put("inicio_ciclo", c.getString("inicio_ciclo"));
                    map.put("fin_ciclo", c.getString("fin_ciclo"));
                    map.put("Nombre_Asignatura", c.getString("Nombre_Asignatura"));
                    map.put("NRC", c.getString("NRC"));
                    map.put("Nombre_Docente", c.getString("Nombre_Docente"));
                    map.put("Salon", c.getString("Salon"));

                    ((Variables) getActivity().getApplication()).setListMie(map);
                }
                if(c.has("jueves_inicio")){
                    map.put("jueves_inicio", c.getString("jueves_inicio"));
                    map.put("jueves_fin", c.getString("jueves_fin"));
                    map.put("inicio_ciclo", c.getString("inicio_ciclo"));
                    map.put("fin_ciclo", c.getString("fin_ciclo"));
                    map.put("Nombre_Asignatura", c.getString("Nombre_Asignatura"));
                    map.put("NRC", c.getString("NRC"));
                    map.put("Nombre_Docente", c.getString("Nombre_Docente"));
                    map.put("Salon", c.getString("Salon"));

                    ((Variables) getActivity().getApplication()).setListJue(map);
                }
                if(c.has("viernes_inicio")){
                    map.put("viernes_inicio", c.getString("viernes_inicio"));
                    map.put("viernes_fin", c.getString("viernes_fin"));
                    map.put("inicio_ciclo", c.getString("inicio_ciclo"));
                    map.put("fin_ciclo", c.getString("fin_ciclo"));
                    map.put("Nombre_Asignatura", c.getString("Nombre_Asignatura"));
                    map.put("NRC", c.getString("NRC"));
                    map.put("Nombre_Docente", c.getString("Nombre_Docente"));
                    map.put("Salon", c.getString("Salon"));

                    ((Variables) getActivity().getApplication()).setListVie(map);
                }
                if(c.has("sabado_inicio")){
                    map.put("sabado_inicio", c.getString("sabado_inicio"));
                    map.put("sabado_fin", c.getString("sabado_fin"));
                    map.put("inicio_ciclo", c.getString("inicio_ciclo"));
                    map.put("fin_ciclo", c.getString("fin_ciclo"));
                    map.put("Nombre_Asignatura", c.getString("Nombre_Asignatura"));
                    map.put("NRC", c.getString("NRC"));
                    map.put("Nombre_Docente", c.getString("Nombre_Docente"));
                    map.put("Salon", c.getString("Salon"));

                    ((Variables) getActivity().getApplication()).setListSab(map);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Cayo aqui", "Cayo aqui");
            Toast.makeText(getActivity(), "Sin horarios registrados todavia", Toast.LENGTH_SHORT).show();
        }
    }

    private class TareaAsincrona extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            metodoPrincipal();
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pDialog.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {

            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    TareaAsincrona.this.cancel(true);
                }
            });

            pDialog.setProgress(0);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                pDialog.dismiss();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getActivity(), "Descarga de contenido cancelado!", Toast.LENGTH_SHORT).show();
        }
    }
}
