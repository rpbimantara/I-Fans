package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.Context;

import oogbox.api.odoo.OdooClient;

public interface InterfaceLelang {
    public void Addbidder(final String idlelang, final String nilai, final String status, Context context, SharedPrefManager sharedPrefManager, OdooClient client, ProgressDialog progressDialog);
}
