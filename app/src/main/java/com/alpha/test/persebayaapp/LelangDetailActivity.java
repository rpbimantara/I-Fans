package com.alpha.test.persebayaapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;
import static com.alpha.test.persebayaapp.CommonUtils.formater;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.tanggal;

public class LelangDetailActivity extends AppCompatActivity {
    TextView txtNamaBarang,txtBid,txtDeskripsi,txtInfoDetail;
    Button btnEdit,btnDelete;
    ImageView imageDetail;
    OdooClient client;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lelang_detail);
        Toolbar toolbar = findViewById(R.id.detail_lelang_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPrefManager = new SharedPrefManager(this);
        imageDetail = findViewById(R.id.lelang_detail_imageView);
        txtNamaBarang = findViewById(R.id.textView_nama_barang_lelang);
        btnEdit = findViewById(R.id.button_edit_lelang);
        btnDelete = findViewById(R.id.button_delete_lelang);
        txtBid = findViewById(R.id.textView_bid_detail_lelang);
        txtDeskripsi = findViewById(R.id.textView_deskripsi_lelang);
        txtInfoDetail = findViewById(R.id.textView_info_detail_lelang);
        client = getOdooConnection(getBaseContext());
        LoadData();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fabIntent = new Intent(LelangDetailActivity.this,LelangAddActivity.class);
                fabIntent.putExtra("id",Integer.valueOf(getIntent().getExtras().get("id").toString()));
                startActivity(fabIntent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LelangDetailActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Are You Sure to Delete This Item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!TextUtils.isEmpty(getIntent().getExtras().get("id").toString())) {
                            OdooValues values = new OdooValues();
                            values.put("is_deleted", true);
                            client.write("product.template", new Integer[]{Integer.valueOf(getIntent().getExtras().get("id").toString())}, values, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    // Success response
                                    Toast.makeText(getApplicationContext(),"Admin will review your action.",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
    public void LoadData(){
        ODomain domain = new ODomain();
        domain.add("id", "=", Integer.valueOf(getIntent().getExtras().get("id").toString()));

        OdooFields fields = new OdooFields();
        fields.addAll("id","image_medium","name", "ob","inc","binow","due_date","create_uid","description_sale");

        int offset = 0;
        int limit = 80;

        String sorting = "id ASC";

        client.searchRead("product.template", domain, fields, offset, limit, sorting,new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] Records = result.getRecords();
                for (final OdooRecord record : Records) {
                    imageDetail.setImageBitmap(StringToBitMap(record.getString("image_medium")));
                    txtNamaBarang.setText(record.getString("name"));
                    txtBid.setText("Open Bid : "
                            + formater(record.getFloat("ob"))
                            + "\n"
                            + "BIN : "
                            + formater(record.getFloat("binow"))
                            + "\n"
                            + "INC : "
                            + formater(record.getFloat("inc"))
                    );
                    if (record.getInt("create_uid") == sharedPrefManager.getSpIdUser()){
                        btnDelete.setVisibility(View.VISIBLE);
                        btnEdit.setVisibility(View.VISIBLE);
                    }else{
                        btnDelete.setVisibility(View.INVISIBLE);
                        btnEdit.setVisibility(View.INVISIBLE);
                    }
                    txtDeskripsi.setText("Deskripsi : \n\n" + record.getString("description_sale"));
                    txtInfoDetail.setText(record.getString("create_uid")+" - "+tanggal(record.getString("due_date")));
                }
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                return super.onError(error);
            }
        });
    }

}
