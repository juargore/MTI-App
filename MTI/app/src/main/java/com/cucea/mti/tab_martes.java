package com.cucea.mti;

/**
 * Created by Arturo on 1/25/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class tab_martes extends Fragment {

    ListView listView_Martes;
    TextView txtCiclo_Martes;
    ProgressDialog pDialog;
    TareaAsincrona tarea2;
    View view;
    ArrayList list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_martes,container,false);

        Log.e("Valor de view2:", String.valueOf(view));

        pDialog = new ProgressDialog(getActivity());
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Actualizando...");
        pDialog.setMax(100);

        listView_Martes = (ListView)view.findViewById(R.id.listView_Martes);
        txtCiclo_Martes = (TextView)view.findViewById(R.id.txtCiclo_Martes);

        tarea2 = new TareaAsincrona();
        tarea2.execute();

        return  view;
    }

    public void metodoPrincipal(){
        list = ((Variables) getActivity().getApplication()).getListMar();
    }

    public class ImageAdapter extends BaseAdapter
    {
        //private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
        private LayoutInflater layoutInflater;

        public ImageAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            //context = c;
            MyArr = list;
            layoutInflater = LayoutInflater.from(c);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return MyArr.get(position);
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolder holder;

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.plantilla_horarios, null);
                holder = new ViewHolder();

                holder.txtHora_Horario = (TextView)convertView.findViewById(R.id.txtHora_Horario);
                holder.txtNRC_Horario = (TextView)convertView.findViewById(R.id.txtNRC_Horario);
                holder.txtMateria_Horario = (TextView) convertView.findViewById(R.id.txtMateria_Horario);
                holder.txtSalon_Horario = (TextView)convertView.findViewById(R.id.txtSalon_Horario);
                //holder.txtCiclo_Martes = (TextView)convertView.findViewById(R.id.txtCiclo_Martes);

                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> newsItem = MyArr.get(position);

            txtCiclo_Martes.setText(""+ newsItem.get("inicio_ciclo")+" - "+newsItem.get("fin_ciclo"));
            holder.txtHora_Horario.setText(""+ newsItem.get("martes_inicio")+" - "+newsItem.get("martes_fin"));
            holder.txtMateria_Horario.setText("" + newsItem.get("Nombre_Asignatura"));
            holder.txtNRC_Horario.setText("NRC: " + newsItem.get("NRC"));
            holder.txtSalon_Horario.setText("" + newsItem.get("Salon")+" - "+newsItem.get("Nombre_Docente"));

            return convertView;
        }
    }

    static class ViewHolder{
        TextView txtHora_Horario, txtNRC_Horario, txtMateria_Horario, txtSalon_Horario;
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
                //Toast.makeText(getActivity(), "Tarea finalizada!",
                //      Toast.LENGTH_SHORT).show();
            }

            if(!list.isEmpty()){
                listView_Martes.setAdapter(new ImageAdapter(getActivity(),list));
                listView_Martes.setFastScrollEnabled(true);
                listView_Martes.setVerticalScrollBarEnabled(false);
            }

        }
    }


}
