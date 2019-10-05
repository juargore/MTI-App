package com.cucea.mti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ImageView;
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

public class News_Complete extends ActionBarActivity {

    TextView txtDia, txtMes, txtTitulo, txtSubtitulo, txtDescripcion;
    ImageView imgImagen;
    JSONArray data;
    Bitmap btm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_complete);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (isConnected()) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(Html.fromHtml("<b><font color='#ffffff'>Noticia Completa</font></b>"));
            actionBar.setDisplayShowHomeEnabled(false);

            txtDia = (TextView)findViewById(R.id.txtDia);
            txtMes = (TextView)findViewById(R.id.txtMes);
            txtTitulo = (TextView)findViewById(R.id.txtTitulo);
            txtSubtitulo = (TextView)findViewById(R.id.txtSubtitulo);
            txtDescripcion = (TextView)findViewById(R.id.txtDescripcion);
            imgImagen = (ImageView)findViewById(R.id.imgImagen);


            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(new FadeInBitmapDisplayer(300)).build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new WeakMemoryCache())
                    .discCacheSize(100 * 1024 * 1024).build();

            ImageLoader.getInstance().init(config);

            String id_noticia = getIntent().getExtras().getString("id_noticia");
            String url = "http://agavia.com.mx/proyectos/siip/app/noticias/getNoticiaById.php?id_noticia="+id_noticia;

            try {
                data = new JSONArray(getJSONUrl(url));
                final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map;

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    map = new HashMap<String, String>();
                    map.put("id_noticia", c.getString("id_noticia"));
                    map.put("titulo", c.getString("titulo"));
                    map.put("subtitulo", c.getString("subtitulo"));
                    map.put("fecha_hora", c.getString("fecha_hora"));
                    map.put("imagenUrl", c.getString("imagenUrl"));
                    map.put("descripcion", c.getString("descripcion"));
                    MyArrList.add(map);

                    String string = map.get("fecha_hora");
                    String[] parts = string.split("-");
                    String month = parts[1];
                    int month1 = Integer.parseInt(month);
                    String day = parts[2];


                    txtTitulo.setText(map.get("titulo"));
                    txtSubtitulo.setText(map.get("subtitulo"));
                    txtDescripcion.setText(map.get("descripcion"));
                    txtDia.setText("" + day);
                    txtMes.setText("" +mesString(month1));

                    String urll = map.get("imagenUrl");

                    ImageLoader imageLoader = ImageLoader.getInstance();
                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .cacheInMemory(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageForEmptyUri(R.drawable.no_image_found)
                    .showImageOnFail(R.drawable.no_image_found).build();

                    btm = imageLoader.loadImageSync(urll, options);
                    Drawable drawable =new BitmapDrawable(btm);
                    imgImagen.setImageDrawable(drawable);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Esta App necesita conexion a internet para poder funcionar satisfactoriamente. Por favor, conecte su dispositivo a internet e ingrese de nuevo");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Entendido",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            News_Complete.this.finish();
                            News_Complete.this.moveTaskToBack(true);

                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    // Get JSON Code from URL
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

        if (id == android.R.id.home) {
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


    public String mesString(int m){
        String mes;
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
}
