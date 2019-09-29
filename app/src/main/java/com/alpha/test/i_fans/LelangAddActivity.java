
package com.alpha.test.i_fans;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

import static com.alpha.test.i_fans.CommonUtils.StringToBitMap;
import static com.alpha.test.i_fans.CommonUtils.getBase64ImageString;
import static com.alpha.test.i_fans.CommonUtils.getOdooConnection;
import static com.alpha.test.i_fans.CommonUtils.tanggal;

public class LelangAddActivity extends AppCompatActivity {
    EditText etName,etOb,etInc,etBin,etdeskripsi;
    TextView txtDuedate;
    ImageView imageUser;
    private Bitmap currentImage;
    SharedPrefManager sharedPrefManager;
    FloatingActionButton fabImageLelang;
    ProgressDialog progressDialog;
    Button savebtn;
    OdooClient client;
    String imageCurrent = "";
    String txtName = "";
    String txtOb = "";
    String txtInc ="";
    String txtBin = "";
    String Duedate ="";
    String txtdeskripsi = "";
    String state = "create";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lelang_add);
        sharedPrefManager = new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);
        imageUser = findViewById(R.id.image_AddLelang);
        imageUser.setImageResource(R.drawable.ic_account);
        fabImageLelang = findViewById(R.id.image_fab_Add_Lelang);
        fabImageLelang.setImageResource(R.drawable.ic_camera);
        savebtn = findViewById(R.id.button_save_lelang);
        client = getOdooConnection(getBaseContext());
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equalsIgnoreCase("create")){
                    createLelang();
//                    new SaveLelangTask().execute();
                }else{

                }
            }
        });
        etName = findViewById(R.id.editText_namabarangLelang);
        etOb = findViewById(R.id.editText_openBid);
        etInc = findViewById(R.id.editText_INC);
        etBin = findViewById(R.id.editText_BIN);
        txtDuedate = findViewById(R.id.add_due_date);
        etdeskripsi = findViewById(R.id.editText_Deskripsi);
        txtDuedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(LelangAddActivity.this, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = month + "/" + dayOfMonth + "/" + year;
                        txtDuedate.setText(date);
                    }
                },year,month,day);
                dialog.show();
            }
        });

        fabImageLelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
        if (!getIntent().getExtras().get("id").toString().equalsIgnoreCase("false")){
            state = "edit";
            etOb.setEnabled(false);
            LoadItem();
        }
    }


    public void createLelang(){
        progressDialog.setMessage("Create.....");
        progressDialog.show();
        OdooValues values = new OdooValues();
        values.put("image_medium", getBase64ImageString(currentImage));
        values.put("name", etName.getText().toString());
        values.put("ob", etOb.getText().toString());
        values.put("inc", etInc.getText().toString());
        values.put("binow", etBin.getText().toString());
        values.put("purchase_ok", false);
        values.put("type", "lelang");
        values.put("description_sale", etdeskripsi.getText().toString());
        values.put("due_date", txtDuedate.getText().toString());
        values.put("create_uid", sharedPrefManager.getSpIdUser());

        client.create("product.template", values, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                // Success response
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(),"Auction Created!",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Toast.makeText(getBaseContext(),String.valueOf(error.getMessage()),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return super.onError(error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri photoUri = data.getData();
            if (photoUri != null){
                try{
                    currentImage = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                    imageUser.setImageBitmap(currentImage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

//    public class SaveLelangTask extends AsyncTask<Void,Void,Void>{
//        @Override
//        protected Void doInBackground(Void... voids) {
//            client = new OdooClient.Builder(getBaseContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSynchronizedRequests(false)
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            progressDialog.setMessage("Saving.....");
//                            progressDialog.show();
//                            OdooValues values = new OdooValues();
//                            values.put("image_medium", getBase64ImageString(currentImage));
//                            values.put("name", etName.getText().toString());
//                            values.put("ob", etOb.getText().toString());
//                            values.put("inc", etInc.getText().toString());
//                            values.put("binow", etBin.getText().toString());
//                            values.put("purchase_ok", false);
//                            values.put("type", "lelang");
//                            values.put("description_sale", etdeskripsi.getText().toString());
//                            values.put("due_date", txtDuedate.getText().toString());
//                            values.put("create_uid", sharedPrefManager.getSpIdUser());
//
//                            client.create("product.template", values, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    // Success response
//                                    progressDialog.dismiss();
//                                    Toast.makeText(getBaseContext(),"Auction Created!",Toast.LENGTH_LONG).show();
//                                    finish();
//                                }
//
//                                @Override
//                                public boolean onError(OdooErrorException error) {
//                                    Toast.makeText(getBaseContext(),String.valueOf(error.getMessage()),Toast.LENGTH_LONG).show();
//                                    progressDialog.dismiss();
//                                    return super.onError(error);
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }

    public void EditLelang(){

    }

    public void LoadItem(){
        ODomain domain = new ODomain();
        domain.add("id", "=", Integer.valueOf(getIntent().getExtras().get("id").toString()));

        OdooFields fields = new OdooFields();
        fields.addAll("id","image_medium","name", "ob","inc","binow","due_date","create_uid");

        int offset = 0;
        int limit = 80;

        String sorting = "id ASC";

        client.searchRead("product.template", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

                symbols.setGroupingSeparator('.');
                formatter.setDecimalFormatSymbols(symbols);
                for (OdooRecord record : records) {
                    txtName = record.getString("name");
                    imageCurrent = record.getString("image_medium");
                    txtOb = String.valueOf(Math.round(record.getFloat("ob")));
                    txtInc = String.valueOf(Math.round(record.getFloat("inc")));
                    txtBin = String.valueOf(Math.round(record.getFloat("binow")));
                    Duedate = tanggal(record.getString("due_date"));
                    txtdeskripsi = record.getString("create_uid");
                }
                imageUser.setImageBitmap(StringToBitMap(imageCurrent));
                etName.setText(txtName);
                etOb.setText(txtOb);
                etInc.setText(txtInc);
                etBin.setText(txtBin);
                txtDuedate.setText(Duedate);
                etdeskripsi.setText(txtdeskripsi);
            }
        });
    }

}
