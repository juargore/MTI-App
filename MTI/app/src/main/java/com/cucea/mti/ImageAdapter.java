package com.cucea.mti;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final String[] titulosMenu;
    private int height;

    public ImageAdapter(Context context, String[] titulosMenu, int height) {
        this.context = context;
        this.titulosMenu = titulosMenu;
        this.height = height;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;


        if (convertView == null) {
            gridView = new View(context);

            gridView = inflater.inflate(R.layout.botones_menu, null);

            RelativeLayout lay1 = (RelativeLayout) gridView.findViewById(R.id.lay1);
            TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
            ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);

            lay1.getLayoutParams().height = (height/14)*4;

            textView.setText(titulosMenu[position]);
            //textView.setText(Html.fromHtml(titulosMenu[position].toString()));

            String mobile = titulosMenu[position];

            //Colocar imagenes en cada boton
            if (mobile.equals("Home")) {
                imageView.setImageResource(R.mipmap.btnhome);
            } else if (mobile.equals("Mensajes")) {
                    imageView.setImageResource(R.mipmap.mensaje);
            } else if (mobile.equals("Social")) {
                imageView.setImageResource(R.mipmap.btnsocial);
            } else if (mobile.equals("Perfil")) {
                imageView.setImageResource(R.mipmap.btnperfil);
            } else if (mobile.equals("SIIAU")) {
                imageView.setImageResource(R.mipmap.btnsiiau);
            } else if (mobile.equals("Buscador")) {
                imageView.setImageResource(R.mipmap.iconbuscador);
            } else if (mobile.equals("Horarios")) {
                imageView.setImageResource(R.mipmap.btnhorario);
            } else if (mobile.equals("Calendario")) {
                imageView.setImageResource(R.mipmap.btncalendario);
            } else if (mobile.equals("Telefonos")) {
                imageView.setImageResource(R.mipmap.btntelefonoemergencia);
            } else {
                imageView.setImageResource(R.drawable.ic_drawer);
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return titulosMenu.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}