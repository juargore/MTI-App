package com.cucea.mti;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_Home extends Fragment  implements ViewFactory{

    ListView listView1;
    View rootView;
    Bitmap btm;
    static String json = null;
    DownloadURL downloadURL;
    ProgressDialog pDialog;
    TareaAsincrona tarea2;
    final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

    public Fragment_Home(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment__home, container, false);

        downloadURL = new DownloadURL();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (downloadURL.isConnected(getActivity())) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Descargando contenido...");
            pDialog.setCancelable(false);
            pDialog.setMax(100);

            tarea2 = new TareaAsincrona();
            tarea2.execute();
        } else {
            downloadURL.MsgInternet(getActivity());
        }

        return rootView;
    }

    public void metodoPrincipal(){
        downloadURL.UniversalImageLoader(getActivity());
        listView1 = (ListView)rootView.findViewById(R.id.list);
        String url = "http://agavia.com.mx/proyectos/siip/app/noticias/getNoticias.php";
        JSONArray data = downloadURL.getJson(url);

        try {
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<>();
                map.put("id_noticia", c.getString("id_noticia"));
                map.put("titulo", c.getString("titulo"));
                map.put("subtitulo", c.getString("subtitulo"));
                map.put("fecha_hora", c.getString("fecha_hora"));
                map.put("imagenUrl", c.getString("imagenUrl"));
                MyArrList.add(map);

                String urll = map.get("imagenUrl");

                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .showImageForEmptyUri(R.drawable.no_image_found)
                        .showImageOnFail(R.drawable.no_image_found).build();

                btm = imageLoader.loadImageSync(urll, options);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("--", "Error al descargar contenido...");
        }
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
                convertView = layoutInflater.inflate(R.layout.plantilla_pantallas_news, null);
                holder = new ViewHolder();
                holder.txtDia = (TextView)convertView.findViewById(R.id.txtDia);
                holder.txtMes = (TextView)convertView.findViewById(R.id.txtMes);
                holder.txtTitulo = (TextView)convertView.findViewById(R.id.txtTitulo);
                holder.txtSubtitulo = (TextView)convertView.findViewById(R.id.txtSubtitulo);
                holder.imgImagen = (ImageView) convertView.findViewById(R.id.imgImagen);

                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> newsItem = MyArr.get(position);

            String string = newsItem.get("fecha_hora");
            String[] parts = string.split("-"); //2016-04-27 Divide la fecha cada que ve el guion = 2016, 04, 27
            String month = parts[1]; // Accede a la posicion [1] = 04
            int month1 = Integer.parseInt(month);
            String day = parts[2]; // Accede a la posicion [2] = 27

            holder.txtDia.setText("" + day);
            holder.txtMes.setText("" +mesString(month1));
            holder.txtTitulo.setText("" + newsItem.get("titulo"));
            holder.txtSubtitulo.setText("" + newsItem.get("subtitulo"));

            String url = MyArr.get(position).get("imagenUrl");

            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageForEmptyUri(R.drawable.no_image_found)
                    .showImageOnFail(R.drawable.no_image_found).build();

            imageLoader.displayImage(url, holder.imgImagen, options);
            return convertView;
        }
    }

    static class ViewHolder{
        TextView txtDia, txtMes, txtTitulo, txtSubtitulo;
        ImageView imgImagen;
    }

    public String mesString(int m){
        String mes = "";
        switch (m){
            case 1:
                mes = "ENE";
                break;
            case 2:
                mes = "FEB";
                break;
            case 3:
                mes = "MAR";
                break;
            case 4:
                mes = "ABR";
                break;
            case 5:
                mes = "MAY";
                break;
            case 6:
                mes = "JUN";
                break;
            case 7:
                mes = "JUL";
                break;
            case 8:
                mes = "AGO";
                break;
            case 9:
                mes = "SEP";
                break;
            case 10:
                mes = "OCT";
                break;
            case 11:
                mes = "NOV";
                break;
            case 12:
                mes = "DIC";
                break;
            default:
                mes = "NO";
                break;
        }
        return mes;
    }

    @Override
    public View makeView() {
        return null;
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
                //Toast.makeText(getActivity(), "Tarea finalizada!",Toast.LENGTH_SHORT).show();
            }

            listView1.setAdapter(new ImageAdapter(getActivity(),MyArrList));
            listView1.setFastScrollEnabled(true);
            listView1.setVerticalScrollBarEnabled(false);

            listView1.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    String id_noticia = MyArrList.get(position).get("id_noticia").toString();

                    Intent A = new Intent("com.cucea.mti.News_Complete");
                    A.putExtra("id_noticia", id_noticia);
                    startActivity(A);
                    System.gc();
                }
            });
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getActivity(), "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();
        }
    }

}