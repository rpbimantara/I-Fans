package com.alpha.test.i_fans;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

public class StoreAddActivity extends AppCompatActivity {
    EditText etName,etPrice,etStock,etdeskripsi;
    ImageView imageUser;
    private Bitmap currentImage;
    SharedPrefManager sharedPrefManager;
    FloatingActionButton fabImageStore;
    ProgressDialog progressDialog;
    Button savebtn;
    OdooClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add);
        sharedPrefManager = new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);
        imageUser = findViewById(R.id.image_AddStore);
        imageUser.setImageResource(R.drawable.ic_account);
        fabImageStore = findViewById(R.id.image_fab_Add_Store);
        fabImageStore.setImageResource(R.drawable.ic_camera);
        savebtn = findViewById(R.id.button_save_store);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaveStoreTask().execute();
            }
        });
        etName = findViewById(R.id.editText_namabarangStore);
        etPrice = findViewById(R.id.editText_price_store);
        etStock = findViewById(R.id.editText_stock);
        etdeskripsi = findViewById(R.id.editText_Deskripsi_store);
        fabImageStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
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

    public String getBase64ImageString(Bitmap photo) {
        String imgString;
        if(photo != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 10, outputStream);
            byte[] profileImage = outputStream.toByteArray();

            imgString = Base64.encodeToString(profileImage,
                    Base64.NO_WRAP);
        }else{
            imgString = "";
        }

        return imgString;
    }

    public class SaveStoreTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            client = new OdooClient.Builder(getBaseContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSynchronizedRequests(false)
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            progressDialog.setMessage("Saving.....");
                            progressDialog.show();
                            OdooValues values = new OdooValues();
                            values.put("image_medium", getBase64ImageString(currentImage));
                            values.put("name", etName.getText().toString());
                            values.put("purchase_ok", false);
                            values.put("type", "product");
                            values.put("list_price", etPrice.getText().toString());
//                            values.put("stock", etStock.getText().toString());
                            values.put("description_sale", etdeskripsi.getText().toString());
                            values.put("create_uid", sharedPrefManager.getSpIdUser());

                            client.create("product.template", values, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    // Success response
                                    progressDialog.dismiss();
                                    Toast.makeText(getBaseContext(),"Store Created!",Toast.LENGTH_LONG).show();
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
                    }).build();
            return null;
        }
    }
}
