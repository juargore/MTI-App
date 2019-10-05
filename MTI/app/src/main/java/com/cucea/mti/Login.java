package com.cucea.mti;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Login extends Activity implements View.OnClickListener {

    private Button btnLogin;
    EditText etUser, etPassword;
    TextView txtOlvido;
    String url;
    JSONObject jObject;
    CheckBox chbxRecordar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.get("data")!=null) {
            //here can get notification message
            Log.e("--","ENtro al bundle");
            String datas = bundle.get("data").toString();
            Log.e("--","RES> "+datas);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (isConnected()) {
            etUser = (EditText) findViewById(R.id.etUser);
            etPassword = (EditText) findViewById(R.id.etPassword);
            btnLogin = (Button) findViewById(R.id.btnLogin);
            txtOlvido = (TextView) findViewById(R.id.txtOlvido);
            chbxRecordar = (CheckBox) findViewById(R.id.chbxRecordar);

            etUser.setText("398637532");
            etPassword.setText("123");

            btnLogin.setOnClickListener(this);
            txtOlvido.setOnClickListener(this);

        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
            builder1.setMessage("Esta App necesita conexión a internet para poder funcionar satisfactoriamente. " +
                    "Por favor, conecte su dispositivo a internet e ingrese de nuevo");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Entendido",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            Login.this.finish();
                            Login.this.moveTaskToBack(true);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtOlvido:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
                builder1.setMessage("Por disposición de la Administración, es necesario que te acerques a las oficinas de la Coordinación de la Maestría " +
                        "para poder recuperar tu Usuario y/o Contraseña y actualicen tu información. ");
                builder1.setCancelable(true);
                builder1.setTitle("RECUPERAR CONTRASEÑA");
                builder1.setPositiveButton("Entendido",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                break;

            case R.id.btnLogin:
                url = "http://agavia.com.mx/proyectos/siip/app/login/login.php?codigo="+etUser.getText().toString()+"&contrasena="+etPassword.getText().toString();
                try {
                    jObject = new JSONObject(getJSONUrl(url));
                    Log.e("--",jObject+"");

                    String S = jObject.getString("status");

                    if(S.contains("Success")){

                        SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();

                        String id = jObject.getString("id_alumno");
                        editor.putString("id_alumno", id);
                        editor.commit();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);

                        if(chbxRecordar.isChecked()){
                        editor.putBoolean("recordar_contrasena", true);
                        editor.commit();
                        }
                        this.finish();
                    }
                    else{
                        Toast.makeText(this, "Usuario y/o contraseña Incorrectos", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
        }
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
                //Log.e("Log", "Failed to download file.."+" Codigo: "+statusCode);
                Log.e("Log", "Res: "+url);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}
