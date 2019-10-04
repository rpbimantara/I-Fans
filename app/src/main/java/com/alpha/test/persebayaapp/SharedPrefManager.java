package com.alpha.test.persebayaapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SP_Persebaya_APP = "spPersebayaApp";
    public static final String SP_ID_USER = "spIdUser";
    public static final String SP_ID_PARTNER = "spIdPartner";
    public static final String SP_ID_Liga = "spIdLiga";
    public static final String SP_ID_CLUB = "spIdClub";
    public static final String SP_NAMA_CLUB = "spNamaClub";
    public static final String SP_NAMA_USER = "spNamaUser";
    public static final String SP_PASSWORD_USER = "spPasswordUser";
    public static final String SP_REG_ID = "spRegId";
    public static final String SP_SUDAH_LOGIN = "spSudahLogin";
    public static final String SP_FAB = "spFAB";
    public static final String SP_FAB_Belanja = "spFABBelanja";
    public static final String SP_IMAGE_NEWS = "spImageNews";
    public static final String SP_RETURN_FROM_RV = "StringFromRV";
    public static final String SP_Host_url = "StringURL";
    public static final String SP_db = "StringDB";
    public static final String SP_COIN_USER = "Coin";
    public static final String SP_SESSION_ID = "SESSION_ID";
    public static final String SP_USER_STATE = "spState";


    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_Persebaya_APP,Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.apply();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.apply();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.apply();
    }

    public Integer getSpIdUser() {
        return sp.getInt(SP_ID_USER,0);
    }

    public Integer getSpIdPartner() {
        return sp.getInt(SP_ID_PARTNER,0);
    }

    public Integer getSPIdLiga() {
        return sp.getInt(SP_ID_Liga,0);
    }

    public Integer getSpIdClub() {
        return sp.getInt(SP_ID_CLUB,0);
    }

    public Integer getSpCoinUser() {
        return sp.getInt(SP_COIN_USER,0);
    }

    public String getSpNamaClub() {
        return sp.getString(SP_NAMA_CLUB,"");
    }

    public String getSpNamaUser() {
        return sp.getString(SP_NAMA_USER,"");
    }

    public String getSpPasswordUser() {
        return sp.getString(SP_PASSWORD_USER,"");
    }

    public String getSpRegId() {
        return sp.getString(SP_REG_ID,"");
    }

    public Boolean getSPSudahLogin() {
        return sp.getBoolean(SP_SUDAH_LOGIN,false);
    }

    public String getSpImageNews() {
        return  sp.getString(SP_IMAGE_NEWS,"");
    }

    public String getSpReturnFromRv() {
        return sp.getString(SP_RETURN_FROM_RV,"");
    }

    public String getSpUserState(){return sp.getString(SP_USER_STATE,"");}

    public String getSpFab() {
        return sp.getString(SP_FAB,"");
    }
    public String getSpFabBelanja() {
        return sp.getString(SP_FAB_Belanja,"Store");
    }

    public String getSP_Host_url() {
        return sp.getString(SP_Host_url,"http://103.133.56.224:8069");
    }

    public String getSP_db() {
        return sp.getString(SP_db,"development");
    }

    public String getSpSessionId() {
            return sp.getString(SP_SESSION_ID,"");
    }
}
