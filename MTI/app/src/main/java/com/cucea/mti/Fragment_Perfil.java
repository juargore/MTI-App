package com.cucea.mti;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class Fragment_Perfil extends Fragment implements View.OnClickListener{

    ImageView ivPerfilUsuario;
    TextView txtNombre_Perfil, txtNombrePrograma_Perfil,
            txtCodigo_Perfil, txtEmail_Perfil,
            txtCelular_Perfil, txtTitulo_Perfil,
            txtMore_Perfil, txtModalidad_Perfil,
            txtCurp_Perfil, txtFechaNacimiento_Perfil;
    String universidad, nacimiento, sexo, rfc, casa, director, asesor;
    Context context;
    Bitmap btm;
    DownloadURL downloadURL;

    public Fragment_Perfil() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__perfil, container, false);
        downloadURL = new DownloadURL();

        if (downloadURL.isConnected(getActivity())) {
            txtMore_Perfil = (TextView) rootView.findViewById(R.id.txtMore_Perfil);
            ivPerfilUsuario = (ImageView)rootView.findViewById(R.id.ivPerfil_Perfil);
            txtNombre_Perfil = (TextView) rootView.findViewById(R.id.txtNombre_Perfil);
            txtNombrePrograma_Perfil = (TextView) rootView.findViewById(R.id.txtNombrePrograma_Perfil);
            txtCodigo_Perfil = (TextView) rootView.findViewById(R.id.txtCodigo_Perfil);
            txtTitulo_Perfil = (TextView) rootView.findViewById(R.id.txtTitulo_Perfil);
            txtEmail_Perfil = (TextView) rootView.findViewById(R.id.txtEmail_Perfil);
            txtCelular_Perfil = (TextView) rootView.findViewById(R.id.txtCelular_Perfil);
            txtModalidad_Perfil = (TextView) rootView.findViewById(R.id.txtModalidad_Perfil);
            txtFechaNacimiento_Perfil = (TextView) rootView.findViewById(R.id.txtFechaNacimiento_Perfil);
            txtCurp_Perfil = (TextView) rootView.findViewById(R.id.txtCurp_Perfil);

            txtMore_Perfil.setOnClickListener(this);
            downloadURL.UniversalImageLoader(getActivity());

            SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", MODE_PRIVATE);
            String id_alumno = prefs.getString("id_alumno", "0");
            String url = "http://agavia.com.mx/proyectos/siip/app/perfil/getPerfilCompleto.php?id_alumno="+id_alumno;
            JSONArray data = downloadURL.getJson(url);

            try {
                JSONObject c = data.getJSONObject(0);
                txtNombre_Perfil.setText(""+c.getString("nombre_completo"));
                txtNombrePrograma_Perfil.setText(""+c.getString("nombre_programa"));
                txtCodigo_Perfil.setText(""+c.getString("codigo"));
                txtTitulo_Perfil.setText(""+c.getString("titulo"));
                txtEmail_Perfil.setText(""+c.getString("email"));
                txtCelular_Perfil.setText(""+c.getString("celular"));
                txtModalidad_Perfil.setText(""+c.getString("modalidad"));
                txtFechaNacimiento_Perfil.setText(""+c.getString("fecha_nacimiento"));
                txtCurp_Perfil.setText(""+c.getString("curp"));

                universidad = c.getString("universidad");
                nacimiento = c.getString("lugar_nacimiento");
                sexo = c.getString("sexo");
                rfc = c.getString("rfc");
                casa = c.getString("telefono");
                director = c.getString("director");
                asesor = c.getString("asesor");

                String urll = c.getString("foto");
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true)
                        .cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .showImageForEmptyUri(R.drawable.no_image_found)
                        .showImageOnFail(R.drawable.no_image_found).resetViewBeforeLoading(true).cacheOnDisc(true)
                        .build();

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
                    ivPerfilUsuario.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivPerfilUsuario.setImageBitmap(btm2);
                    ivPerfilUsuario.setBackground(drawCircle(tam, tam, Color.WHITE));
                }
                ivPerfilUsuario.setPadding(7, 7, 7, 7); //Grosor del borde

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error", "Error al descargar JSON");
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

    @Override
    public void onClick(View view) {
        LayoutInflater inflater1 = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder imageDialog2 = new AlertDialog.Builder(getActivity());

        View layout1 = inflater1.inflate(R.layout.plantilla_mas_perfil,
                (ViewGroup)getView().findViewById(R.id.layMas_Perfil));

        TextView txtUniversidad_Perfil = (TextView) layout1.findViewById(R.id.txtUniversidad_Perfil);
        TextView txtNacimiento_Perfil = (TextView) layout1.findViewById(R.id.txtNacimiento_Perfil);
        TextView txtSexo_Perfil = (TextView) layout1.findViewById(R.id.txtSexo_Perfil);
        TextView txtRFC_Perfil = (TextView) layout1.findViewById(R.id.txtRFC_Perfil);
        TextView txtCasa_Perfil = (TextView) layout1.findViewById(R.id.txtCasa_Perfil);
        TextView txtDirector_Perfil = (TextView) layout1.findViewById(R.id.txtDirector_Perfil);
        TextView txtAsesor_Perfil = (TextView) layout1.findViewById(R.id.txtAsesor_Perfil);

        txtUniversidad_Perfil.setText(""+universidad);
        txtNacimiento_Perfil.setText(""+nacimiento);
        txtSexo_Perfil.setText(""+sexo);
        txtRFC_Perfil.setText(""+rfc);
        txtCasa_Perfil.setText(""+casa);
        txtDirector_Perfil.setText(""+director);
        txtAsesor_Perfil.setText(""+asesor);

        imageDialog2.setView(layout1);
        AlertDialog alert11 = imageDialog2.create();
        alert11.show();

    }
}