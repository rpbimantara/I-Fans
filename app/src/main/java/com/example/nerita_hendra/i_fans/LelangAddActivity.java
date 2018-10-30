
package com.example.nerita_hendra.i_fans;

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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class LelangAddActivity extends AppCompatActivity {
    EditText etName,etOb,etInc,etBin,etDuedate,etdeskripsi;
    ImageView imageUser;
    private Bitmap currentImage;
    SharedPrefManager sharedPrefManager;
    FloatingActionButton fabImageLelang;
    ProgressDialog progressDialog;
    Button savebtn;

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
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new SaveLelangTask().execute();
            }
        });
        etName = findViewById(R.id.editText_namabarangLelang);
        etOb = findViewById(R.id.editText_openBid);
        etInc = findViewById(R.id.editText_INC);
        etBin = findViewById(R.id.editText_BIN);
        etDuedate = findViewById(R.id.editText_Due_date);
        etdeskripsi = findViewById(R.id.editText_Deskripsi);
        fabImageLelang.setOnClickListener(new View.OnClickListener() {
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
//                    new AccountFragment.SaveImageTask().execute(currentImage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    public class SaveLelangTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Saving.....");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            finish();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                @SuppressWarnings("unchecked")
                Integer idC = oc.create("persebaya.lelang", new HashMap() {{
                    put("image", getBase64ImageString(currentImage));
                    put("nama_barang", etName.getText().toString());
                    put("ob", etOb.getText().toString());
                    put("inc", etInc.getText().toString());
                    put("binow", etBin.getText().toString());
                    put("deskripsi_barang", etdeskripsi.getText().toString());
                    put("create_uid", sharedPrefManager.getSpIdUser());
                }});
//                msgResult = idC.toString();
                System.out.println(idC.toString());

            } catch (Exception ex) {
                System.out.println("Error: " + ex);

            }
            return null;
        }
    }

    public String getBase64ImageString(Bitmap photo) {
        String imgString;
        if(photo != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] profileImage = outputStream.toByteArray();

            imgString = Base64.encodeToString(profileImage,
                    Base64.NO_WRAP);
        }else{
            imgString = "";
        }

        return imgString;
    }
}
