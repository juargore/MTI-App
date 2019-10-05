package com.cucea.mti;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.android.push.BasePushMessageReceiver;
import com.arellomobile.android.push.PushManager;
import com.arellomobile.android.push.utils.RegisterBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {

    ActionBarDrawerToggle actionBarD;
    DrawerLayout drawer_layout;
    GridView gridView;
    String[] TITULOS_MENU  = new String[] {
            "Home", "Mensajes", "Social", "Perfil", "SIIAU", "Buscador", "Horarios", "Calendario", "Telefonos" };

    //Funciones de mensajeria Push de Push Woosh
    BroadcastReceiver mBroadcastReceiver = new RegisterBroadcastReceiver()
    {
        @Override
        public void onRegisterActionReceive(Context context, Intent intent)
        {
            checkMessage(intent);
        }
    };

    //Push message receiver
    private BroadcastReceiver mReceiver = new BasePushMessageReceiver()
    {
        @Override
        protected void onMessageReceive(Intent intent)
        {
            //JSON_DATA_KEY contains JSON payload of push notification.
            //showMessage("push message is " + intent.getExtras().getString(JSON_DATA_KEY));
            String a = intent.getExtras().getString(JSON_DATA_KEY);

            try {
                JSONObject jsonObject = new JSONObject(intent.getExtras().getString(JSON_DATA_KEY));
                String header = "";

                if(a.contains("header")){
                    header = jsonObject.getString("header");
                } else {
                    header = "MTI";
                }

                String msg = jsonObject.getString("title");

                Intent intMs = new Intent(MainActivity.this, PantallaMensaje.class);
                intMs.putExtra("header", header);
                intMs.putExtra("msg", msg);
                intMs.putExtra("identificador", "1");
                startActivity(intMs);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    //Registration of the receivers
    public void registerReceivers()
    {
        IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");
        registerReceiver(mReceiver, intentFilter, getPackageName() +".permission.C2D_MESSAGE", null);
        registerReceiver(mBroadcastReceiver, new IntentFilter(getPackageName() + "." + PushManager.REGISTER_BROAD_CAST_ACTION));
    }

    public void unregisterReceivers()
    {
        //Unregister receivers on pause
        try
        { unregisterReceiver(mReceiver); }
        catch (Exception e) { }

        try
        { unregisterReceiver(mBroadcastReceiver); }
        catch (Exception e) { }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        //Re-register receivers on resume
        registerReceivers();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        //Unregister receivers on pause
        unregisterReceivers();
    }

    private void checkMessage(Intent intent)
    {
        if (null != intent)//Si sí llegó algo
        {
            if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
            {
                //showMessage("push message is " + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
                String b = intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT);

                try {
                    JSONObject jsonObject = new JSONObject(intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
                    String header = "";

                    if(b.contains("header")){
                        header = jsonObject.getString("header");
                    } else {
                        header = "MTI";
                    }

                    String msg = jsonObject.getString("title");

                    Intent intMs = new Intent(MainActivity.this, PantallaMensaje.class);
                    intMs.putExtra("header", header);
                    intMs.putExtra("msg", msg);
                    intMs.putExtra("identificador", "1");
                    startActivity(intMs);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (intent.hasExtra(PushManager.REGISTER_EVENT))
            {
                //showMessage("register");
                Log.e("--","register");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_EVENT))
            {
                //showMessage("unregister");
                Log.e("--","unregister");
            }
            else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
            {
                //showMessage("register error");
                Log.e("--","register error");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
            {
                //showMessage("unregister error");
                Log.e("--","unregister error");
            }

            resetIntentValues();
        }
    }

    /**
     * Will check main Activity intent and if it contains any PushWoosh data, will clear it
     */
    private void resetIntentValues()
    {
        Intent mainAppIntent = getIntent();

        if (mainAppIntent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.PUSH_RECEIVE_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_ERROR_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_ERROR_EVENT);
        }

        setIntent(mainAppIntent);
    }

    private void showMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);

        checkMessage(intent);

        setIntent(new Intent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Register receivers for push notifications
        registerReceivers();

        //Create and start push manager
        PushManager pushManager = new PushManager(this, "0A02A-9C8DD", "881509519537");
        pushManager.onStartup(this);

        checkMessage(getIntent());

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<b><font color='#ffffff'>MTI</font></b>"));

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int width = size.x;

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        gridView = (GridView) findViewById(R.id.gridView1);

        gridView.getLayoutParams().width = width;
        gridView.setAdapter(new ImageAdapter(this, TITULOS_MENU, height));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                displayView(position, ((TextView) v.findViewById(R.id.grid_item_label)).getText().toString());

            }
        });

        actionBarD = new ActionBarDrawerToggle(this, drawer_layout, R.mipmap.ic_menu_white_24dp, R.string.app_name, R.string.app_name)
        {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        drawer_layout.setDrawerListener(actionBarD);

        if (savedInstanceState == null) {
            // Inicia la App en el primer fragment
            displayView(0, "MTI");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarD.onOptionsItemSelected(item)) {
            return true;
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
                                SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();
                                editor.putBoolean("recordar_contrasena", false);
                                editor.commit();
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void displayView(int position, String name) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new Fragment_Home();
                break;
            case 1:
                fragment = new Fragment_Mensajes();
                break;
            case 2:
                fragment = new Fragment_Social();
                break;
            case 3:
                fragment = new Fragment_Perfil();
                break;
            case 4:
                fragment = new Fragment_SIIAU();
                break;
            case 5:
                fragment = new Fragment_Buscador();
                break;
            case 6:
                fragment = new Fragment_Horarios();
                break;
            case 7:
                fragment = new Fragment_Calendario();
                break;
            case 8:
                fragment = new Fragment_Telefonos();
                break;
            case 9:
                new AlertDialog.Builder(this)
                        .setTitle("B-IN")
                        .setMessage("�Seguro que deseas cerrar sesi�n en MTI y salir de la App?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //bd.deleteUser(wdb);

                                MainActivity.this.finish();
                                MainActivity.this.moveTaskToBack(true);

                                android.os.Process.killProcess(android.os.Process.myPid());
                            }})
                        .setNegativeButton("NO", null).show();
                break;

            default:

                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

            setTitle(name.toUpperCase());
            drawer_layout.closeDrawer(gridView);
        }
        else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        String mTitle = title.toString();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarD.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarD.onConfigurationChanged(newConfig);
    }
}
