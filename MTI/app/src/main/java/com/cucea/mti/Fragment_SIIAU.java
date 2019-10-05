package com.cucea.mti;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_SIIAU extends Fragment {

    ListView listView_Kardex;
    DownloadURL downloadURL;
    ImageView ivPerfil_Kardex;
    TextView txtNombre_Kardex, txtNombrePrograma_Kardex, txtCreditos_Kardex, txtPromedio_Kardex;
    Bitmap btm;
    double calificaciones[];
    int creditos[];

    public Fragment_SIIAU() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__siiau, container, false);
        downloadURL = new DownloadURL();

        if (downloadURL.isConnected(getActivity())) {
            downloadURL.UniversalImageLoader(getActivity());

            listView_Kardex = (ListView)rootView.findViewById(R.id.listView_Kardex);
            ivPerfil_Kardex = (ImageView)rootView.findViewById(R.id.ivPerfil_Kardex);
            txtNombre_Kardex = (TextView)rootView.findViewById(R.id.txtNombre_Kardex);
            txtNombrePrograma_Kardex = (TextView)rootView.findViewById(R.id.txtNombrePrograma_Kardex);
            txtCreditos_Kardex = (TextView)rootView.findViewById(R.id.txtCreditos_Kardex);
            txtPromedio_Kardex = (TextView)rootView.findViewById(R.id.txtPromedio_Kardex);

            SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", MODE_PRIVATE);
            String id_alumno = prefs.getString("id_alumno", "0");

            String url = "http://agavia.com.mx/proyectos/siip/app/kardex/getKardexById.php?id_alumno="+id_alumno;
            String url2 = "http://agavia.com.mx/proyectos/siip/app/perfil/getPerfilSencillo.php?id_alumno="+id_alumno;
            JSONArray data = downloadURL.getJson(url);
            JSONArray data2 = downloadURL.getJson(url2);

            try {
                JSONObject j = data2.getJSONObject(0);
                txtNombre_Kardex.setText(""+j.getString("nombre_completo"));
                txtNombrePrograma_Kardex.setText(""+j.getString("nombre_programa"));
                String urll = j.getString("foto");

                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .showImageForEmptyUri(R.drawable.ic_drawer)
                        .showImageOnFail(R.drawable.ic_drawer).build();

                if(urll.equals("") || urll == null){
                    Drawable drawable = this.getResources().getDrawable(R.mipmap.iconuser);
                    btm = ((BitmapDrawable) drawable).getBitmap();
                } else {
                    if(downloadURL.checkIfUrlExists(urll)){
                        btm = imageLoader.loadImageSync(urll, options);
                    } else{
                        Drawable drawable = this.getResources().getDrawable(R.mipmap.iconuser);
                        btm = ((BitmapDrawable) drawable).getBitmap();
                    }
                }

                Bitmap btm2 = downloadURL.getCircleBitmap(getActivity(), btm);

                Point size = new Point();
                int width = size.x;
                int tam = (width/5)*2;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ivPerfil_Kardex.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivPerfil_Kardex.setImageBitmap(btm2);
                    ivPerfil_Kardex.setBackground(drawCircle(tam, tam, Color.WHITE));
                }
                ivPerfil_Kardex.setPadding(7, 7, 7, 7);


                final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map;

                Log.e("--",""+data);

                calificaciones = new double [data.length()];
                creditos = new int [data.length()];



                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);

                    map = new HashMap<>();
                    map.put("nombre_asignatura", c.getString("nombre_asignatura"));
                    map.put("nrc", c.getString("nrc"));
                    map.put("clave", c.getString("clave"));
                    map.put("calificacion", c.getString("calificacion"));
                    map.put("tipo", c.getString("tipo"));
                    map.put("creditos", c.getString("creditos"));
                    MyArrList.add(map);

                    calificaciones[i] = Double.parseDouble(c.getString("calificacion"));
                    creditos[i] = Integer.parseInt(c.getString("creditos"));
                }

                //Suma de calificaciones y creditos
                double finalCal=0.0, finalCred=0;
                for(int i=0; i<calificaciones.length; i++){
                    finalCal = finalCal + calificaciones[i];
                    finalCred = finalCred + creditos[i];
                }

                txtPromedio_Kardex.setText(""+String.format("%.2f", (finalCal/calificaciones.length)));
                txtCreditos_Kardex.setText(""+finalCred);

                listView_Kardex.setAdapter(new ImageAdapter(getActivity(),MyArrList));
                listView_Kardex.setFastScrollEnabled(true);
                listView_Kardex.setVerticalScrollBarEnabled(false);

                listView_Kardex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {

                        //Al hacer click...
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error", "Error al descargar JSON");
                Toast.makeText(getActivity(), "Sin calificaciones registradas todavia", Toast.LENGTH_SHORT).show();
            }

        } else {
            downloadURL.MsgInternet(getActivity());
        }
        return rootView;
    }

    public static ShapeDrawable drawCircle (int width, int height, int color) {
        ShapeDrawable oval = new ShapeDrawable (new OvalShape());
        oval.setIntrinsicHeight (height);
        oval.setIntrinsicWidth (width);
        oval.getPaint ().setColor (color);
        return oval;
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
                convertView = layoutInflater.inflate(R.layout.plantilla_calificaciones, null);
                holder = new ViewHolder();

                holder.txtTitulo_Kardex = (TextView)convertView.findViewById(R.id.txtTitulo_Kardex);
                holder.txtNRC_Kardex = (TextView)convertView.findViewById(R.id.txtNRC_Kardex);
                holder.txtClave_Kardex = (TextView) convertView.findViewById(R.id.txtClave_Kardex);
                holder.txtTipo_Kardex = (TextView)convertView.findViewById(R.id.txtTipo_Kardex);
                holder.txtCreditos_Kardex = (TextView) convertView.findViewById(R.id.txtCreditos_Kardex);
                holder.txtCalificacion_Kardex = (TextView) convertView.findViewById(R.id.txtCalificacion_Kardex);

                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> newsItem = MyArr.get(position);
            holder.txtTitulo_Kardex.setText(""+ newsItem.get("nombre_asignatura"));
            holder.txtNRC_Kardex.setText("" + newsItem.get("nrc"));
            holder.txtClave_Kardex.setText("" + newsItem.get("clave"));
            holder.txtCalificacion_Kardex.setText("" + newsItem.get("calificacion"));
            holder.txtTipo_Kardex.setText("" + newsItem.get("tipo"));
            holder.txtCreditos_Kardex.setText("" + newsItem.get("creditos"));

            return convertView;
        }
    }

    static class ViewHolder{
        TextView txtTitulo_Kardex,txtNRC_Kardex, txtClave_Kardex,
                txtTipo_Kardex, txtCreditos_Kardex, txtCalificacion_Kardex;
    }
}
