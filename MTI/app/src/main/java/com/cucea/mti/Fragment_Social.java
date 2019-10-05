package com.cucea.mti;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class Fragment_Social extends Fragment implements View.OnClickListener {

    ProgressDialog dialog;
    ImageButton btnFacebook,btnTwitter;
    WebView wv1;
    View rootView=null;
    int boton = 0;
    ProgressBar progressBarT4 = null;

    public Fragment_Social() {
    }

    int width, height;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (isConnected(getActivity())) {
            rootView = inflater.inflate(R.layout.fragment__social, container, false);
            progressBarT4 = (ProgressBar) rootView.findViewById(R.id.progressBarT4);

            btnFacebook = (ImageButton)rootView.findViewById(R.id.btnFacebook);
            btnTwitter = (ImageButton)rootView.findViewById(R.id.btnTwitter);
            wv1 = (WebView)rootView.findViewById(R.id.wv1);

            wv1.setWebViewClient(new WebViewClient());
            wv1.setWebChromeClient(new WebChromeClient(){

                public void onProgressChanged(WebView view, int progress) {
                    progressBarT4.setVisibility(View.VISIBLE);
                    wv1.getSettings().setJavaScriptEnabled(true);
                    wv1.getSettings().setUseWideViewPort(true);
                    wv1.getSettings().setLoadWithOverviewMode(true);
                    wv1.getSettings().setBuiltInZoomControls(true);
                    progressBarT4.setProgress(progress);
                    if (progress == 100) {
                        progressBarT4.setVisibility(View.GONE); // Make the bar disappear after URL is loaded
                    }
                }
            });

            progressBarT4.setVisibility(View.VISIBLE);
            wv1.loadUrl("https://www.facebook.com/mtriati");

            btnFacebook.setOnClickListener(this);
            btnTwitter.setOnClickListener(this);

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;

            ViewGroup.LayoutParams params = btnFacebook.getLayoutParams();
            ViewGroup.LayoutParams params1 = btnTwitter.getLayoutParams();
            params.width = (width/2);
            params1.width = (width/2);
            btnFacebook.setLayoutParams(params);
            btnTwitter.setLayoutParams(params1);

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

    public boolean isConnected(Activity activity){
        ConnectivityManager connectivityManager = (ConnectivityManager)activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnFacebook:
                wv1.loadUrl("https://www.facebook.com/mtriati");
                wv1.getSettings().setUseWideViewPort(true);
                wv1.getSettings().setLoadWithOverviewMode(true);
                wv1.getSettings().setBuiltInZoomControls(true);
                break;
            case R.id.btnTwitter:
                wv1.loadUrl("https://twitter.com/mticuceaudg");
                wv1.getSettings().setUseWideViewPort(true);
                wv1.getSettings().setLoadWithOverviewMode(true);
                wv1.getSettings().setBuiltInZoomControls(true);
                break;
        }
    }
}
