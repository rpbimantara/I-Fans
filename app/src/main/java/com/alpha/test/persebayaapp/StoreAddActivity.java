package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;
import static com.alpha.test.persebayaapp.CommonUtils.getBase64ImageString;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;

public class StoreAddActivity extends AppCompatActivity {
    EditText etName,etPrice,etStock,etdeskripsi;
    ImageView imageUser;
    private Bitmap currentImage;
    SharedPrefManager sharedPrefManager;
    FloatingActionButton fabImageStore;
    ProgressDialog progressDialog;
    Button savebtn;
    OdooClient client;
    String imageCurrent = "";
    String description = "No Item Descriptions ";
    String name = "No Item Descriptions ";
    String price = "No Item Descriptions ";
    String price_float = "No Item Descriptions ";
    String stock = "No Item Descriptions ";
    String stok_float = "";
    String state = "create";


    //Jadi intine, dari halaman store add ini setelah add/edit nanti mereload storefragment. bukakno apps e
    //trus? buka no menu e, iki wes menu e
    //IYO

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
        client = getOdooConnection(getBaseContext());
        if (!getIntent().getExtras().get("id").toString().equalsIgnoreCase("false")){
            state = "edit";
            LoadItem();
        }
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equalsIgnoreCase("create")){
                    if(is_Valid() == true){
                        createbtn();
                    }
                }else{
                    if(is_Valid() == true){
                        savebtn();
                    }
                }
            }
        });
    }

    public Boolean is_Valid() {
        Boolean is_Success = true;
        if(!TextUtils.isEmpty(getBase64ImageString(currentImage))){
            Toast.makeText(getBaseContext(),"Choose at least 1 image!",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(etName.getText())){
            is_Success = false;
            etName.setError("Fill Name.");
        }

        if(TextUtils.isEmpty(etdeskripsi.getText())){
            is_Success = false;
            etdeskripsi.setError("Fill Description.");
        }

        if(!TextUtils.isEmpty(etPrice.getText())){
            if(Integer.valueOf(etPrice.getText().toString())<=0){
                is_Success = false;
                etPrice.setError("Must greather than 0");
            }
        }else{
            is_Success = false;
            etPrice.setError("Fill Price.");
        }

        if(!TextUtils.isEmpty(etStock.getText())){
            if(Integer.valueOf(etStock.getText().toString())<=0){
                is_Success = false;
                etStock.setError("Must greather than 0");
            }
        }else{
            is_Success = false;
            etStock.setError("Fill Stock.");
        }

        return is_Success;
    }

    public void LoadItem(){
        ODomain domain = new ODomain();
        domain.add("id", "=", Integer.valueOf(getIntent().getExtras().get("id").toString()));

        OdooFields fields = new OdooFields();
        fields.addAll("id","image_medium","name", "type","default_code","cated_ig","description_sale","list_price","qty_available");

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
                    imageCurrent = record.getString("image_medium");
                    name = record.getString("name");
                    price = String.valueOf(record.getInt("list_price"));
                    price_float = String.valueOf(record.getInt("list_price"));
                            stok_float = String.valueOf(record.getInt("qty_available"));
                    stock = String.valueOf(formatter.format(record.getFloat("qty_available")));
                    if (!record.getString("description_sale").equalsIgnoreCase("false"))
                    {
                        description = record.getString("description_sale");
                    }
                }
                imageUser.setImageBitmap(StringToBitMap(imageCurrent));
                currentImage = StringToBitMap(imageCurrent);
                etName.setText(name);
                etPrice.setText(price);
                etStock.setText(stock);
                etdeskripsi.setText(description);
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

    public void savebtn(){
        progressDialog.setMessage("Saving.....");
        progressDialog.show();
        OdooValues values = new OdooValues();
        values.put("image_medium", getBase64ImageString(currentImage));
        values.put("name", etName.getText().toString());
        values.put("list_price", etPrice.getText().toString());
        values.put("description_sale", etdeskripsi.getText().toString());
        client.write("product.template", new Integer[]{Integer.valueOf(getIntent().getExtras().get("id").toString())}, values, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                // Success response
                OArguments arguments = new OArguments();
                arguments.add(Integer.valueOf(getIntent().getExtras().get("id").toString()));
                Integer qty = 0;
                if (Integer.valueOf(etStock.getText().toString()) > Integer.valueOf(stok_float)){
                    qty = Integer.valueOf(etStock.getText().toString()) - Integer.valueOf(stok_float);
                }else{
                    qty = Integer.valueOf(stok_float) - Integer.valueOf(etStock.getText().toString());
                }
                arguments.add(qty);
                client.call_kw("product.template", "update_qty", arguments, new IOdooResponse() {
                    @Override
                    public void onResult(OdooResult result) {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(),"Changes Saved!",Toast.LENGTH_LONG).show();
//                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public boolean onError(OdooErrorException error) {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(),String.valueOf(error.getMessage()),Toast.LENGTH_LONG).show();
                        return super.onError(error);
                    }
                });
            }

            @Override
            public boolean onError(OdooErrorException error) {
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(),String.valueOf(error.getMessage()),Toast.LENGTH_LONG).show();
                return super.onError(error);
            }
        });
    }

    public void createbtn(){
        progressDialog.setMessage("Create.....");
        progressDialog.show();
        OArguments arguments = new OArguments();
        arguments.add(getBase64ImageString(currentImage));
        arguments.add(etName.getText().toString());
        arguments.add(etPrice.getText().toString());
        arguments.add(etStock.getText().toString());
        arguments.add(etdeskripsi.getText().toString());
        arguments.add(sharedPrefManager.getSpIdUser());

        client.call_kw("product.template", "create_product",arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                // Success response
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(),"Store Created!",Toast.LENGTH_LONG).show();
//                setResult(RESULT_OK);
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
}
