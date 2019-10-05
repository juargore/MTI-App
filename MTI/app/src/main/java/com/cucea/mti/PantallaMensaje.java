package com.cucea.mti;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PantallaMensaje extends ActionBarActivity implements View.OnClickListener{

    TextView txtHeader, txtMsg, txtFecha;
    Button btnAtrasMsg, btnGuardarMsg;
    DbManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);

        getSupportActionBar().setTitle(Html.fromHtml("<b><font color='#ffffff'>NUEVO MENSAJE PUSH</font></b>"));

        manager = new DbManager(this);
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtMsg = (TextView)findViewById(R.id.txtMsg);
        txtFecha = (TextView)findViewById(R.id.txtFecha);
        btnAtrasMsg = (Button)findViewById(R.id.btnAtrasMsg);
        btnGuardarMsg = (Button)findViewById(R.id.btnGuardarMsg);

        btnGuardarMsg.setOnClickListener(this);
        btnAtrasMsg.setOnClickListener(this);

        String header = getIntent().getExtras().getString("header");
        String msg = getIntent().getExtras().getString("msg");

        txtHeader.setText(header);
        txtMsg.setText(msg);
        txtFecha.setText(date);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnAtrasMsg:
                this.finish();
                break;
            case R.id.btnGuardarMsg:
                manager.insertarRegistro(txtHeader.getText().toString(), txtMsg.getText().toString(),txtFecha.getText().toString());
                Toast.makeText(this, "Mensaje Guardado exitosamente!", Toast.LENGTH_LONG).show();
                this.finish();
                break;
        }
    }
}
