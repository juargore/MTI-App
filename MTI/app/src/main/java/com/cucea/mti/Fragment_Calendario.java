package com.cucea.mti;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fragment_Calendario extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    View rootView;
    DownloadURL downloadURL;
    ListView listView_Calendario;
    Date_Divider dd = new Date_Divider();
    Boolean controlador = false;

    public Fragment_Calendario() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment__calendario, container, false);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        downloadURL = new DownloadURL();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("E n e r o");
        categories.add("F e b r e r o");
        categories.add("M a r z o");
        categories.add("A b r i l");
        categories.add("M a y o");
        categories.add("J u n i o");
        categories.add("J u l i o");
        categories.add("A g o s t o");
        categories.add("S e p t i e m b r e");
        categories.add("O c t u b r e");
        categories.add("N o v i e m b r e");
        categories.add("D i c i e m b r e");
        
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0, true);
        View v = spinner.getSelectedView();
        ((TextView)v).setTextColor(Color.WHITE);

        if (downloadURL.isConnected(getActivity())) {
            downloadURL.UniversalImageLoader(getActivity());
            listView_Calendario = (ListView)rootView.findViewById(R.id.listView_Calendario);
            String url = "http://agavia.com.mx/proyectos/siip/app/calendario/getEventos.php";
            JSONArray data = downloadURL.getJson(url);

            try {
                HashMap<String, String> map;
                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    map = new HashMap<String, String>();
                    map.put("id_evento", c.getString("id_evento"));
                    map.put("fecha_inicio", c.getString("fecha_inicio"));
                    map.put("fecha_fin", c.getString("fecha_fin"));
                    map.put("titulo", c.getString("titulo"));
                    map.put("descripcion", c.getString("descripcion"));

                    String fecha = dd.getDateName(c.getString("fecha_inicio"));

                    if(fecha.contains("Enero")){
                        if(((Variables) getActivity().getApplication()).getListEne().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListEne(map);
                        }
                    }
                    if(fecha.contains("Febrero")){
                        if(((Variables) getActivity().getApplication()).getListFeb().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListFeb(map);
                        }
                    }
                    if(fecha.contains("Marzo")){
                        if(((Variables) getActivity().getApplication()).getListMar().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListMar(map);
                        }
                    }
                    if(fecha.contains("Abril")){
                        if(((Variables) getActivity().getApplication()).getListAbr().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListAbr(map);
                        }
                    }
                    if(fecha.contains("Mayo")){
                        if(((Variables) getActivity().getApplication()).getListMay().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListMay(map);
                        }
                    }
                    if(fecha.contains("Junio")){
                        if(((Variables) getActivity().getApplication()).getListJun().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListJun(map);
                        }
                    }
                    if(fecha.contains("Julio")){
                        if(((Variables) getActivity().getApplication()).getListJul().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListJul(map);
                        }
                    }
                    if(fecha.contains("Agosto")){
                        if(((Variables) getActivity().getApplication()).getListAgo().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListAgo(map);
                        }
                    }
                    if(fecha.contains("Septiembre")){
                        if(((Variables) getActivity().getApplication()).getListSep().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListSep(map);
                        }
                    }
                    if(fecha.contains("Octubre")){
                        if(((Variables) getActivity().getApplication()).getListOct().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListOct(map);
                        }
                    }
                    if(fecha.contains("Noviembre")){
                        if(((Variables) getActivity().getApplication()).getListNov().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListNov(map);
                        }
                    }
                    if(fecha.contains("Diciembre")){
                        if(((Variables) getActivity().getApplication()).getListDic().isEmpty()){ 
                            ((Variables) getActivity().getApplication()).setListDic(map);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Cayo aqui", "Cayo aqui");
            }

        } else {
            downloadURL.MsgInternet(getActivity());
        }
        return rootView;
    }

    public class ImageAdapter extends BaseAdapter
    {
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
        private LayoutInflater layoutInflater;

        public ImageAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            MyArr = list;
            layoutInflater = LayoutInflater.from(c);
        }

        public int getCount() {
            return MyArr.size();
        }

        public Object getItem(int position) {
            return MyArr.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.plantilla_calendario, null);
                holder = new ViewHolder();
                holder.txtFecha_Calendario = (TextView)convertView.findViewById(R.id.txtFecha_Calendario);
                holder.txtTitulo_Calendario = (TextView)convertView.findViewById(R.id.txtTitulo_Calendario);
                holder.txtDescripcion_Calendario = (TextView) convertView.findViewById(R.id.txtDescripcion_Calendario);
                holder.txtHoraInicio_Calendario = (TextView)convertView.findViewById(R.id.txtHoraInicio_Calendario);
                holder.txtHoraFin_Calendario = (TextView) convertView.findViewById(R.id.txtHoraFin_Calendario);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> newsItem = MyArr.get(position);
            String date = newsItem.get("fecha_inicio");
            String date2 = newsItem.get("fecha_fin");
            Date_Divider dd = new Date_Divider();

            holder.txtFecha_Calendario.setText(""+dd.getDateName(date));
            holder.txtTitulo_Calendario.setText("" + newsItem.get("titulo"));
            holder.txtDescripcion_Calendario.setText("" + newsItem.get("descripcion"));
            holder.txtHoraInicio_Calendario.setText(""+dd.getHoraInicio(date));
            holder.txtHoraFin_Calendario.setText("" +dd.getHoraFin(date2));

            return convertView;
        }
    }

    static class ViewHolder{
        TextView txtFecha_Calendario, txtHoraInicio_Calendario,
                txtHoraFin_Calendario, txtTitulo_Calendario, txtDescripcion_Calendario;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, final int pos, long l) {
        //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) view).setTextColor(Color.WHITE);

        final ArrayList<HashMap<String, String>> listEne = ((Variables) getActivity().getApplication()).getListEne();
        final ArrayList<HashMap<String, String>> listFeb = ((Variables) getActivity().getApplication()).getListFeb();
        final ArrayList<HashMap<String, String>> listMaz = ((Variables) getActivity().getApplication()).getListMaz();
        final ArrayList<HashMap<String, String>> listAbr = ((Variables) getActivity().getApplication()).getListAbr();
        final ArrayList<HashMap<String, String>> listMay = ((Variables) getActivity().getApplication()).getListMay();
        final ArrayList<HashMap<String, String>> listJun = ((Variables) getActivity().getApplication()).getListJun();
        final ArrayList<HashMap<String, String>> listJul = ((Variables) getActivity().getApplication()).getListJul();
        final ArrayList<HashMap<String, String>> listAgo = ((Variables) getActivity().getApplication()).getListAgo();
        final ArrayList<HashMap<String, String>> listSep = ((Variables) getActivity().getApplication()).getListSep();
        final ArrayList<HashMap<String, String>> listOct = ((Variables) getActivity().getApplication()).getListOct();
        final ArrayList<HashMap<String, String>> listNov = ((Variables) getActivity().getApplication()).getListNov();
        final ArrayList<HashMap<String, String>> listDic = ((Variables) getActivity().getApplication()).getListDic();

        switch (pos){
            case 0:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listEne));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
            case 1:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listFeb));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
            case 2:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listMaz));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
            case 3:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listAbr));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
            case 4:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listMay));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
            case 5:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listJun));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
            case 6:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listJul));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
            case 7:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listAgo));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
            case 8:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listSep));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
            case 9:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listOct));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
            case 10:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listNov));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
            case 11:
                listView_Calendario.setAdapter(new ImageAdapter(getActivity(),listDic));
                listView_Calendario.setFastScrollEnabled(true);
                listView_Calendario.setVerticalScrollBarEnabled(false);
                break;
        }

        listView_Calendario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String descripcion = "";
                String titulo = "";
                switch (pos){
                    case 0:
                        descripcion = listEne.get(0).get("descripcion").toString();
                        titulo = listEne.get(0).get("titulo").toString();
                        break;
                    case 1:
                        descripcion = listFeb.get(0).get("descripcion").toString();
                        titulo = listFeb.get(0).get("titulo").toString();
                        break;
                    case 2:
                        descripcion = listMaz.get(0).get("descripcion").toString();
                        titulo = listMaz.get(0).get("titulo").toString();
                        break;
                    case 3:
                        descripcion = listAbr.get(0).get("descripcion").toString();
                        titulo = listAbr.get(0).get("titulo").toString();
                        break;
                    case 4:
                        descripcion = listMay.get(0).get("descripcion").toString();
                        titulo = listMay.get(0).get("titulo").toString();
                        break;
                    case 5:
                        descripcion = listJun.get(0).get("descripcion").toString();
                        titulo = listJun.get(0).get("titulo").toString();
                        break;
                    case 6:
                        descripcion = listJul.get(0).get("descripcion").toString();
                        titulo = listJul.get(0).get("titulo").toString();
                        break;
                    case 7:
                        descripcion = listAgo.get(0).get("descripcion").toString();
                        titulo = listAgo.get(0).get("titulo").toString();
                        break;
                    case 8:
                        descripcion = listSep.get(0).get("descripcion").toString();
                        titulo = listSep.get(0).get("titulo").toString();
                        break;
                    case 9:
                        descripcion = listOct.get(0).get("descripcion").toString();
                        titulo = listOct.get(0).get("titulo").toString();
                        break;
                    case 10:
                        descripcion = listNov.get(0).get("descripcion").toString();
                        titulo = listNov.get(0).get("titulo").toString();
                        break;
                    case 11:
                        descripcion = listDic.get(0).get("descripcion").toString();
                        titulo = listDic.get(0).get("titulo").toString();
                        break;
                }

                new AlertDialog.Builder(getActivity())
                        .setTitle(""+titulo)
                        .setMessage(""+descripcion)
                        .show();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
