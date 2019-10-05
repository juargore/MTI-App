package com.cucea.mti;

/**
 * Created by Arturo on 1/25/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class tab_adapter extends FragmentPagerAdapter {

    public tab_adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Log.e("Valor de i:", ""+i);
        switch (i) {
            case 0:
                tab_lunes tlun = new tab_lunes();
                return tlun;
            case 1:
                tab_martes tmar = new tab_martes();
                return tmar;
            case 2:
                tab_miercoles tmie = new tab_miercoles();
                return tmie;
            case 3:
                tab_jueves tjue = new tab_jueves();
                return tjue;
            case 4:
                tab_viernes tvie = new tab_viernes();
                return tvie;
            case 5:
                tab_sabado tsab = new tab_sabado();
                return tsab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 6;
    }//set the number of tabs

    @Override
    public CharSequence getPageTitle(int position) {
        Log.e("Valor de position:", ""+position);
        switch (position) {
            case 0:
                return "L u n e s";
            case 1:
                return "M a r t e s";
            case 2:
                return "M i é r c o l e s";
            case 3:
                return "J u e v e s";
            case 4:
                return "V i e r n e s";
            case 5:
                return "S á b a d o";
            default:
                return "L u n e s";
        }
    }

}
