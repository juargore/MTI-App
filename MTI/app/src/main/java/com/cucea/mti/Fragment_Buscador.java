package com.cucea.mti;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

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
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Fragment_Buscador extends Fragment implements ViewFactory, View.OnClickListener{

    EditText etBuscar;
    Button btnBuscar;
    ListView listResultados;
    View rootView;
    JSONArray data;
    Bitmap btm;
    Bitmap[] myImageList;
    TextView txtResultadosBuscador;
    ProgressDialog pDialog;
    TareaAsincrona tarea2;
    ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    boolean ver = false;
    DownloadURL downloadURL;

    public Fragment_Buscador() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment__buscador, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (isConnected()) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Buscando coincidencias. Espera...");
            pDialog.setCancelable(true);
            pDialog.setMax(100);
            downloadURL = new DownloadURL();

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(new FadeInBitmapDisplayer(300)).build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    getActivity())
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new WeakMemoryCache())
                    .discCacheSize(100 * 1024 * 1024).build();
            ImageLoader.getInstance().init(config);

            listResultados = (ListView)rootView.findViewById(R.id.listResultados);
            etBuscar = (EditText)rootView.findViewById(R.id.etBuscar);
            btnBuscar = (Button)rootView.findViewById(R.id.btnBuscar);
            txtResultadosBuscador = (TextView) rootView.findViewById(R.id.txtResultadosContador);
            btnBuscar.setOnClickListener(this);

        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage("Esta App necesita conexión a internet para poder funcionar satisfactoriamente. Por favor, conecte su dispositivo a internet e ingrese de nuevo");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Entendido",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            getActivity().finish();
                            getActivity().moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

        return rootView;
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {
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
    public void onClick(View view) {
        MyArrList = new ArrayList<>();
        tarea2 = new TareaAsincrona();
        tarea2.execute();
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
                convertView = layoutInflater.inflate(R.layout.plantilla_resultados_buscador, null);
                holder = new ViewHolder();

                holder.txtRes = (TextView)convertView.findViewById(R.id.txtRes);
                holder.imgRes = (ImageView) convertView.findViewById(R.id.imgRes);

                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> newsItem = MyArr.get(position);
            holder.txtRes.setText("" + newsItem.get("nombre_completo"));
            String url = MyArr.get(position).get("fotografia");

            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageForEmptyUri(R.mipmap.iconuser)
                    .showImageOnFail(R.mipmap.iconuser).build();

            imageLoader.displayImage(url, holder.imgRes, options);

            return convertView;
        }
    }

    static class ViewHolder{
        TextView txtRes;
        ImageView imgRes;
    }

    public void buscarPersonas(){

        String valor = etBuscar.getText().toString();
        String url = "http://agavia.com.mx/proyectos/siip/app/buscador/buscador.php?persona_buscada="+valor;

        try {
            data = new JSONArray(getJSONUrl(url));

            myImageList = new Bitmap[data.length()];
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                if(c.has("resultado")){ //No hubo coincidencias
                    ver = false;
                } else {
                    ver = true;
                    map = new HashMap<String, String>();
                    map.put("id", c.getString("id"));
                    map.put("nombre_completo", c.getString("nombre_completo"));
                    map.put("fotografia", c.getString("fotografia"));
                    map.put("tipo", c.getString("tipo"));
                    MyArrList.add(map);

                    String urll = map.get("fotografia");

                    ImageLoader imageLoader = ImageLoader.getInstance();
                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .cacheInMemory(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .showImageForEmptyUri(R.mipmap.iconuser)
                            .showImageOnFail(R.mipmap.iconuser).build();

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

                    myImageList[i] = btm;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View makeView() {return null;}

    private class TareaAsincrona extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            buscarPersonas();
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
            }

            if(ver == true){
                int contador = MyArrList.size();
                txtResultadosBuscador.setText("Encontramos "+contador+" coincidencias");

                listResultados.setAdapter(new ImageAdapter(getActivity(), MyArrList));
                listResultados.setFastScrollEnabled(true);
                listResultados.setVerticalScrollBarEnabled(false);

                listResultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        String tipo_persona = MyArrList.get(position).get("tipo").toString();
                        String id_persona = MyArrList.get(position).get("id").toString();

                        Intent A = new Intent(getActivity(), Buscador_Complete.class);
                        A.putExtra("id_persona", id_persona);
                        Log.e("ID",""+id_persona);
                        A.putExtra("tipo_persona", tipo_persona);
                        startActivity(A);
                    }
                });

            } else {
                txtResultadosBuscador.setText("Encontramos 0 coincidencias");
                Toast.makeText(getActivity(), "No hubo coincidencias con ese criterio", Toast.LENGTH_LONG).show();
            }

            if(getActivity().getCurrentFocus()!=null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getActivity(), "Búsqueda cancelada!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}