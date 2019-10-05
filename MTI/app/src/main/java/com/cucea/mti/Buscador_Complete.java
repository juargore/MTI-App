package com.cucea.mti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Buscador_Complete extends ActionBarActivity {

    ImageView ivSexo, ivPerfil;
    TextView txtPais, txtNombre_Buscador, txtNombrePrograma, txtContacto;
    ListView listBuscador;
    JSONArray data, data2;
    Bitmap btm;
    String url, url2;
    DownloadURL downloadURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador__complete);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        downloadURL = new DownloadURL();

        if (downloadURL.isConnected(this)) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<b><font color='#ffffff'>Perfil Completo</font></b>"));
            getSupportActionBar().setDisplayShowHomeEnabled(false);

            txtPais = (TextView)findViewById(R.id.txtPais);
            txtNombre_Buscador = (TextView)findViewById(R.id.txtNombre_Buscador);
            txtNombrePrograma = (TextView)findViewById(R.id.txtNombrePrograma);
            txtContacto = (TextView)findViewById(R.id.txtContacto);
            ivSexo = (ImageView)findViewById(R.id.ivSexo);
            ivPerfil = (ImageView)findViewById(R.id.ivPerfil);
            listBuscador = (ListView)findViewById(R.id.listBuscador);

            downloadURL.UniversalImageLoader(this);
            String id_persona = getIntent().getExtras().getString("id_persona");
            String tipo_persona = getIntent().getExtras().getString("tipo_persona");

            if(tipo_persona.equals("1")){ //Es alumno
                url = "http://agavia.com.mx/proyectos/siip/app/buscador/getAlumnoById.php?id_alumno="+id_persona;
                url2 = "http://agavia.com.mx/proyectos/siip/app/buscador/getCVUAlumno.php?id_alumno="+id_persona;
            } else { //Es docente
                url = "http://agavia.com.mx/proyectos/siip/app/buscador/getDocenteById.php?id_docente="+id_persona;
                url2 = "http://agavia.com.mx/proyectos/siip/app/buscador/getCVUDocente.php?id_docente="+id_persona;
            }

            try {
                data = new JSONArray(getJSONUrl(url));
                JSONObject j = data.getJSONObject(0);

                txtPais.setText(j.getString("nombre_pais"));
                txtNombre_Buscador.setText(j.getString("nombre_completo"));
                txtNombrePrograma.setText(j.getString("nombre_programa"));
                txtContacto.setText(j.getString("email"));

                if(j.getString("sexo").equals("1")){
                    ivSexo.setImageResource(R.drawable.male);
                } else {
                    ivSexo.setImageResource(R.drawable.female);
                }

                String url1 = j.getString("fotografia");
                Log.e("--",""+url1);

                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .showImageForEmptyUri(R.mipmap.iconuser)
                        .showImageOnFail(R.mipmap.iconuser).build();

                if(url1.equals("") || url1 == null){
                    Drawable drawable = this.getResources().getDrawable(R.mipmap.iconuser);
                    btm = ((BitmapDrawable) drawable).getBitmap();
                    Log.e("--","Entro 3");
                } else {
                    if(downloadURL.checkIfUrlExists(url1)){
                        Log.e("--","Entro 1");
                        btm = imageLoader.loadImageSync(url1, options);
                    } else{
                        Log.e("--","Entro 2");
                        Drawable drawable = this.getResources().getDrawable(R.mipmap.iconuser);
                        btm = ((BitmapDrawable) drawable).getBitmap();
                    }
                }

                Bitmap btm2 = downloadURL.getCircleBitmap(this, btm);
                Point size = new Point();
                int width = size.x;
                int tam = (width/5)*2;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ivPerfil.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivPerfil.setImageBitmap(btm2);
                    ivPerfil.setBackground(drawCircle(tam, tam, Color.WHITE));
                }
                ivPerfil.setPadding(7, 7, 7, 7);

                String resultado = getJSONUrl(url2);
                if(!resultado.equals("null")){
                    data2 = new JSONArray(resultado);
                    final ArrayList<HashMap<String, String>> MyArrList1 = new ArrayList<HashMap<String, String>>();
                    HashMap<String, String> map1;

                    for(int i = 0; i < data2.length(); i++){
                        JSONObject c = data2.getJSONObject(i);
                        map1 = new HashMap<>();
                        map1.put("descripcion", c.getString("descripcion"));
                        map1.put("fecha", c.getString("fecha"));
                        MyArrList1.add(map1);
                    }

                    listBuscador.setAdapter(new ImageAdapter(this,MyArrList1));
                    listBuscador.setFastScrollEnabled(true);
                    listBuscador.setVerticalScrollBarEnabled(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Esta App necesita conexión a internet para poder funcionar satisfactoriamente. Por favor, conecte su dispositivo a internet e ingrese de nuevo");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Entendido",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            Buscador_Complete.this.finish();
                            Buscador_Complete.this.moveTaskToBack(true);

                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public static ShapeDrawable drawCircle (int width, int height, int color) {
        ShapeDrawable oval = new ShapeDrawable (new OvalShape());
        oval.setIntrinsicHeight (height);
        oval.setIntrinsicWidth (width);
        oval.getPaint ().setColor (color);
        return oval;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                Log.i("Warning", "Error checking internet connection", e);
                return false;
            }
        }
        return false;
    }


    public String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                //Log.e("Log", "Failed to download file.."+" Codigo: "+statusCode);
                Log.e("Log", "Failed to download file: "+url);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }

        switch (item.getItemId()) {
            case R.id.menuAcercaDe:
                LayoutInflater inflater1 = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
                AlertDialog.Builder imageDialog2 = new AlertDialog.Builder(this);

                View layout1 = inflater1.inflate(R.layout.vista_about, null);

                imageDialog2.setView(layout1);
                AlertDialog alert11 = imageDialog2.create();
                alert11.show();

                break;
            case R.id.menuSalir:
                new AlertDialog.Builder(this)
                        .setMessage(("\u00bfEst\u00e1s seguro de cerrar sesi\u00f3n y salir de la Aplicaci\u00f3n?"))
                        .setTitle(Html.fromHtml("Cerrar Sesi\u00f3n"))
                        .setIcon(R.mipmap.logomti)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolder holder;

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.plantilla_cvu, null);
                holder = new ViewHolder();

                holder.txtDescCVU = (TextView)convertView.findViewById(R.id.txtDescCVU);
                holder.txtFechaCVU = (TextView)convertView.findViewById(R.id.txtFechaCVU);

                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> newsItem = MyArr.get(position);

            holder.txtDescCVU.setText("" + newsItem.get("descripcion"));
            holder.txtFechaCVU.setText("" + newsItem.get("fecha"));

            return convertView;
        }
    }

    static class ViewHolder{
        TextView txtDescCVU, txtFechaCVU;
    }

}
