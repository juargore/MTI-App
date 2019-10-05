package com.cucea.mti;

/**
 * Created by Arturo on 1/27/2017.
 */

public class Date_Divider {

    public String getDateName (String fecha){ //2016-10-20 09:00:00
        String[] Date_Hour = fecha.split(" "); //Divide en 2016-10-20 y 09:00:00
        String Date = Date_Hour[0]; //2016-10-20
        String Hour = Date_Hour[1]; //09:00:00

        String[] DateM = Date.split("-");
        String mes = DateM[1];
        String dia = DateM[2];
        String anio = DateM[0];

        int month = Integer.parseInt(mes);

        String mesConLetra = ""+getMesLetra(month)+" "+dia+" - "+anio;

        return mesConLetra; //Octubre 20 - 2016
    }

    public String getHoraInicio(String fecha){
        String[] Date_Hour = fecha.split(" "); //09:00:00

        String hour = Date_Hour[1];
        return hour.substring(0, hour.length()-3); //09:00:00
    }

    public String getHoraFin(String fecha){
        String[] Date_Hour = fecha.split(" "); //09:00:00

        String hour = Date_Hour[1];
        return hour.substring(0, hour.length()-3); //09:00:00
    }


    public String getMesLetra(int Date){

        String mes = "";
        switch (Date){
            case 1:
                mes = "Enero";
                break;
            case 2:
                mes = "Febrero";
                break;
            case 3:
                mes = "Marzo";
                break;
            case 4:
                mes = "Abril";
                break;
            case 5:
                mes = "Mayo";
                break;
            case 6:
                mes = "Junio";
                break;
            case 7:
                mes = "Julio";
                break;
            case 8:
                mes = "Agosto";
                break;
            case 9:
                mes = "Septiembre";
                break;
            case 10:
                mes = "Octubre";
                break;
            case 11:
                mes = "Noviembre";
                break;
            case 12:
                mes = "Diciembre";
                break;
            default:
                mes = "NO";
                break;
        }
        return mes;
    }
}
