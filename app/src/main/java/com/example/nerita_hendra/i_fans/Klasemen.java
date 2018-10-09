package com.example.nerita_hendra.i_fans;

public class Klasemen {
    private String txtNoUrut,txtTeamKlasemen, txtPlayKlasemen,txtSelisihGol,txtPoint;

    public Klasemen(String txtNoUrut, String txtTeamKlasemen, String txtPlayKlasemen, String txtSelisihGol, String txtPoint) {
        this.txtNoUrut = txtNoUrut;
        this.txtTeamKlasemen = txtTeamKlasemen;
        this.txtPlayKlasemen = txtPlayKlasemen;
        this.txtSelisihGol = txtSelisihGol;
        this.txtPoint = txtPoint;
    }

    public String getTxtNoUrut() {
        return txtNoUrut;
    }

    public void setTxtNoUrut(String txtNoUrut) {
        this.txtNoUrut = txtNoUrut;
    }

    public String getTxtTeamKlasemen() {
        return txtTeamKlasemen;
    }

    public void setTxtTeamKlasemen(String txtTeamKlasemen) {
        this.txtTeamKlasemen = txtTeamKlasemen;
    }

    public String getTxtPlayKlasemen() {
        return txtPlayKlasemen;
    }

    public void setTxtPlayKlasemen(String txtPlayKlasemen) {
        this.txtPlayKlasemen = txtPlayKlasemen;
    }

    public String getTxtSelisihGol() {
        return txtSelisihGol;
    }

    public void setTxtSelisihGol(String txtSelisihGol) {
        this.txtSelisihGol = txtSelisihGol;
    }

    public String getTxtPoint() {
        return txtPoint;
    }

    public void setTxtPoint(String txtPoint) {
        this.txtPoint = txtPoint;
    }
}
