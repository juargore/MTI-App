package com.cucea.mti;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Arturo on 10/19/2016.
 */
public class ListViewMensajesAdapter extends BaseAdapter {

    private Context context;
    private List<Mensajes> data;

    public ListViewMensajesAdapter (Context context, List<Mensajes> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.plantilla_lista_mensajes, null);
        }

        TextView txtEncabezado = (TextView) view.findViewById(R.id.txtEncabezado);
        TextView txtMensaje = (TextView) view.findViewById(R.id.txtMensaje);
        TextView txtFechaMsg = (TextView) view.findViewById(R.id.txtFechaMsg);

        txtEncabezado.setText(data.get(i).titulo);
        txtMensaje.setText(data.get(i).descripcion);
        txtFechaMsg.setText(data.get(i).fecha);

        return view;
    }
}
