package com.cucea.mti;

/**
 * Created by Arturo on 1/25/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class tab_miercoles extends Fragment {

    ListView listView_Miercoles;
    TextView txtCiclo_Miercoles;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab_miercoles,container,false);

        listView_Miercoles = (ListView)view.findViewById(R.id.listView_Miercoles);
        txtCiclo_Miercoles = (TextView)view.findViewById(R.id.txtCiclo_Miercoles);

        ArrayList list = ((Variables) getActivity().getApplication()).getListMie();
        listView_Miercoles.setAdapter(new ImageAdapter(getActivity(),list));
        listView_Miercoles.setFastScrollEnabled(true);
        listView_Miercoles.setVerticalScrollBarEnabled(false);

        return  view;
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

                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> newsItem = MyArr.get(position);

            txtCiclo_Miercoles.setText(""+ newsItem.get("inicio_ciclo")+" - "+newsItem.get("fin_ciclo"));
            holder.txtHora_Horario.setText(""+ newsItem.get("miercoles_inicio")+" - "+newsItem.get("miercoles_fin"));
            holder.txtMateria_Horario.setText("" + newsItem.get("Nombre_Asignatura"));
            holder.txtNRC_Horario.setText("NRC: " + newsItem.get("NRC"));
            holder.txtSalon_Horario.setText("" + newsItem.get("Salon")+" - "+newsItem.get("Nombre_Docente"));

            return convertView;
        }
    }

    static class ViewHolder{
        TextView txtHora_Horario, txtNRC_Horario, txtMateria_Horario, txtSalon_Horario;
    }


}
