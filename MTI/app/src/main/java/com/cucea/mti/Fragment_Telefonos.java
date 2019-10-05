package com.cucea.mti;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_Telefonos extends Fragment {

    View rootView;
    DownloadURL downloadURL;
    ListView listView_Telefonos;
    Bitmap btm;

    public Fragment_Telefonos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment__telefonos, container, false);
        downloadURL = new DownloadURL();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (downloadURL.isConnected(getActivity())) {
            downloadURL.UniversalImageLoader(getActivity());
            listView_Telefonos = (ListView)rootView.findViewById(R.id.listView_Telefonos);
            String url = "http://agavia.com.mx/proyectos/siip/app/telefonos/getTelefonos.php";
            JSONArray data = downloadURL.getJson(url);

            try {
                final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map;

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
                    map = new HashMap<>();
                    map.put("id_telefono", c.getString("id_telefono"));
                    map.put("telefono", c.getString("telefono"));
                    map.put("dependencia", c.getString("dependencia"));
                    map.put("icono_url", c.getString("icono_url"));
                    MyArrList.add(map);

                    String urll = map.get("icono_url");
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                            .cacheOnDisc(true).resetViewBeforeLoading(true)
                            .cacheInMemory(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .showImageForEmptyUri(R.drawable.ic_drawer)
                            .showImageOnFail(R.drawable.ic_drawer).build();

                    btm = imageLoader.loadImageSync(urll, options);
                }

                listView_Telefonos.setAdapter(new ImageAdapter(getActivity(),MyArrList));
                listView_Telefonos.setFastScrollEnabled(true);
                listView_Telefonos.setVerticalScrollBarEnabled(false);

                listView_Telefonos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        String no_telefono = MyArrList.get(position).get("telefono");

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+no_telefono.replace(" ","")));
                        startActivity(callIntent);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error", "Error al descargar JSON");
            }

        } else {
            downloadURL.MsgInternet(getActivity());
        }
        return rootView;
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
                convertView = layoutInflater.inflate(R.layout.plantilla_telefonos, null);
                holder = new ViewHolder();
                holder.txtDependencia_Telefonos = (TextView)convertView.findViewById(R.id.txtDependencia_Telefonos);
                holder.txtNumero_Telefonos = (TextView)convertView.findViewById(R.id.txtNumero_Telefonos);
                holder.ivIcono_Telefonos = (ImageView) convertView.findViewById(R.id.ivIcono_Telefonos);

                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> newsItem = MyArr.get(position);
            holder.txtNumero_Telefonos.setText("" + newsItem.get("telefono"));
            holder.txtDependencia_Telefonos.setText("" + newsItem.get("dependencia"));

            String url = MyArr.get(position).get("icono_url");

            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageForEmptyUri(R.mipmap.logomti)
            .showImageOnFail(R.mipmap.logomti).build();

            imageLoader.displayImage(url, holder.ivIcono_Telefonos, options);
            return convertView;
        }
    }

    static class ViewHolder{
        TextView txtDependencia_Telefonos, txtNumero_Telefonos;
        ImageView ivIcono_Telefonos;
    }
}
