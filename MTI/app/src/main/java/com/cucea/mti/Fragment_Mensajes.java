package com.cucea.mti;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Mensajes extends Fragment {

    ListView listaMsg;
    ListViewMensajesAdapter adapter;
    RelativeLayout layTransparente;

    public Fragment_Mensajes() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__mensajes, container, false);

        final List<Mensajes> mensajesAlmacenados = new ArrayList<Mensajes>();
        final DbManager manager = new DbManager(rootView.getContext());
        Cursor c = manager.cargarAllRegistros();
        listaMsg = (ListView)rootView.findViewById(R.id.listaMsg);
        layTransparente = (RelativeLayout) rootView.findViewById(R.id.layTransparente);

        if(c.getCount() == 0){
            layTransparente.setVisibility(View.VISIBLE);
        } else {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                mensajesAlmacenados.add(new Mensajes(c.getString(0), c.getString(1), c.getString(2), c.getString(3)));
            }

            adapter = new ListViewMensajesAdapter(getActivity(), mensajesAlmacenados);
            listaMsg.setAdapter(adapter);
        }

        listaMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final String id = (String) mensajesAlmacenados.get(i).id.toString();
                String descripcion = (String) mensajesAlmacenados.get(i).descripcion.toString();
                String titulo = (String) mensajesAlmacenados.get(i).titulo.toString();

                new AlertDialog.Builder(getActivity())
                        .setMessage(""+descripcion)
                        .setTitle(""+titulo)
                        .setIcon(R.mipmap.msg)
                        .setPositiveButton("Eliminar Mensaje", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                new AlertDialog.Builder(getActivity())
                                        .setMessage("Estas seguro de eliminar este mensaje?")
                                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                manager.eliminarRegistro(id);
                                                refreshFragment();
                                            }})
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        })
                                        .show();
                            }})
                        .show();
            }
        });

        return rootView;
    }

    public void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

}
