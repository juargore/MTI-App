package com.cucea.mti;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arturo on 2/2/2017.
 */

public class Variables extends Application {

    Boolean visited = false;

    final ArrayList<HashMap<String, String>> ListLun = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListMar = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListMie = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListJue = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListVie = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListSab = new ArrayList<HashMap<String, String>>();

    ArrayList<HashMap<String, String>> ListEne = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> ListFeb = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListMaz = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListAbr = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListMay = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListJun = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListJul = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListAgo = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListSep = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListOct = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListNov = new ArrayList<HashMap<String, String>>();
    final ArrayList<HashMap<String, String>> ListDic = new ArrayList<HashMap<String, String>>();

    public void setVisited(boolean visited){
        this.visited = visited;
    }

    public Boolean getVisited(){
        return visited;
    }


    public void setListLun(HashMap<String, String> map){
        ListLun.add(map);
    }

    public ArrayList<HashMap<String, String>> getListLun() {
        return ListLun;
    }

    public void setListMar(HashMap<String, String> map){
        ListMar.add(map);
    }

    public ArrayList<HashMap<String, String>> getListMar() {
        return ListMar;
    }

    public void setListMie(HashMap<String, String> map){
        ListMie.add(map);
    }

    public ArrayList<HashMap<String, String>> getListMie() {
        return ListMie;
    }

    public void setListJue(HashMap<String, String> map){
        ListJue.add(map);
    }

    public ArrayList<HashMap<String, String>> getListJue() {
        return ListJue;
    }

    public void setListVie(HashMap<String, String> map){
        ListVie.add(map);
    }

    public ArrayList<HashMap<String, String>> getListSab() {
        return ListSab;
    }

    public void setListSab(HashMap<String, String> map){
        ListSab.add(map);
    }

    public ArrayList<HashMap<String, String>> getListVie() {
        return ListVie;
    }

    public void setListEne(HashMap<String, String> map){
        ListEne.add(map);
    }

    public ArrayList<HashMap<String, String>> getListEne() {
        return ListEne;
    }

    public void setListAbr(HashMap<String, String> map){
        ListAbr.add(map);
    }

    public ArrayList<HashMap<String, String>> getListAbr() {
        return ListAbr;
    }

    public void setListAgo(HashMap<String, String> map){
        ListAgo.add(map);
    }

    public ArrayList<HashMap<String, String>> getListAgo() {
        return ListAgo;
    }

    public void setListDic(HashMap<String, String> map){
        ListDic.add(map);
    }

    public ArrayList<HashMap<String, String>> getListDic() {
        return ListDic;
    }

    public void setListFeb(HashMap<String, String> map){
        ListFeb.add(map);
    }

    public ArrayList<HashMap<String, String>> getListFeb() {
        return ListFeb;
    }

    public void setListJul(HashMap<String, String> map){
        ListJul.add(map);
    }

    public ArrayList<HashMap<String, String>> getListJul() {
        return ListJul;
    }

    public void setListJun(HashMap<String, String> map){
        ListJun.add(map);
    }

    public ArrayList<HashMap<String, String>> getListJun() {
        return ListJun;
    }

    public void setListMay(HashMap<String, String> map){
        ListMay.add(map);
    }

    public ArrayList<HashMap<String, String>> getListMay() {
        return ListMay;
    }

    public void setListMaz(HashMap<String, String> map){
        ListMaz.add(map);
    }

    public ArrayList<HashMap<String, String>> getListMaz() {
        return ListMaz;
    }

    public void setListNov(HashMap<String, String> map){
        ListNov.add(map);
    }

    public ArrayList<HashMap<String, String>> getListNov() {
        return ListNov;
    }

    public void setListOct(HashMap<String, String> map){
        ListOct.add(map);
    }

    public ArrayList<HashMap<String, String>> getListOct() {
        return ListOct;
    }

    public void setListSep(HashMap<String, String> map){
        ListSep.add(map);
    }

    public ArrayList<HashMap<String, String>> getListSep() {
        return ListSep;
    }
}
