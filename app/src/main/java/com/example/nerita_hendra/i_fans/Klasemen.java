package com.example.nerita_hendra.i_fans;

public class Klasemen {
    private String txtNoUrut, txtFotoClub,txtTeamKlasemen, txtPlayKlasemen,txtWin,txtDraw,txtLose, txtSelisihGol,txtPoint;
    private Integer txtLayoutColor;

    public Klasemen(String txtNoUrut, String txtFotoClub, String txtTeamKlasemen, String txtPlayKlasemen, String txtWin,String txtDraw,String txtLose, String txtSelisihGol, String txtPoint, Integer txtLayoutColor) {
        this.txtNoUrut = txtNoUrut;
        this.txtFotoClub = txtFotoClub;
        this.txtTeamKlasemen = txtTeamKlasemen;
        this.txtPlayKlasemen = txtPlayKlasemen;
        this.txtWin = txtWin;
        this.txtDraw = txtDraw;
        this.txtLose = txtLose;
        this.txtSelisihGol = txtSelisihGol;
        this.txtPoint = txtPoint;
        this.txtLayoutColor = txtLayoutColor;
    }

    public String getTxtWin() {
        return txtWin;
    }

    public void setTxtWin(String txtWin) {
        this.txtWin = txtWin;
    }

    public String getTxtDraw() {
        return txtDraw;
    }

    public void setTxtDraw(String txtDraw) {
        this.txtDraw = txtDraw;
    }

    public String getTxtLose() {
        return txtLose;
    }

    public void setTxtLose(String txtLose) {
        this.txtLose = txtLose;
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

    public String getTxtFotoClub() {
        return txtFotoClub;
    }

    public void setTxtFotoClub(String txtFotoClub) {
        this.txtFotoClub = txtFotoClub;
    }

    public Integer getTxtLayoutColor() {
        return txtLayoutColor;
    }

    public void setTxtLayoutColor(Integer txtLayoutColor) {
        this.txtLayoutColor = txtLayoutColor;
    }
}
