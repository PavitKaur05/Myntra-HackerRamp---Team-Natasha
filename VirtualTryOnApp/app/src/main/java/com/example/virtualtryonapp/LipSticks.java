package com.example.virtualtryonapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import id.zelory.compressor.Compressor;

public class LipSticks extends AppCompatActivity {
    ImageButton imageButton;
    boolean isFirstPic = true;
    private static int IMAGE_CAPTURE_RC = 123;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private String imageFilePath,token;
    private ArrayList<File> mImages;
    private ArrayList<String> mImagesPath;

    ImageView makeupImg;

    ProgressDialog dialog;
    String imageUploadUrl="http://e74957664ee2.ngrok.io/api/makeup/";

    Button a1,a2,a3,b1,b2,b3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lip_sticks);

        mImagesPath = new ArrayList<>();
        mImages = new ArrayList<>();

        imageButton = findViewById(R.id.imageButton);
        a1= findViewById(R.id.color1a);
        a2= findViewById(R.id.color2a);
        a3= findViewById(R.id.color3a);

        b1= findViewById(R.id.color1b);
        b2= findViewById(R.id.color2b);
        b3= findViewById(R.id.color3b);

        makeupImg = findViewById(R.id.imageView3);



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIntent();
            }

        });

        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String colorCode = (String)a1.getTag();
                process_further(colorCode);
            }

        });

        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String colorCode = (String)a2.getTag();
                process_further(colorCode);
            }

        });

        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String colorCode = (String)a3.getTag();
                process_further(colorCode);
            }

        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String colorCode = (String)b1.getTag();
                process_further(colorCode);
            }

        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String colorCode = (String)b2.getTag();
                process_further(colorCode);
            }

        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String colorCode = (String)b3.getTag();
                process_further(colorCode);
            }

        });



    }

    //for image click
    private void openCameraIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("camera","entered if");
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.d("camera","entered if if");
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            } else {
                Log.d("camera","entered if else");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        Log.d("camera","entered try");
                        //number_of_images++;
                    } catch (IOException e) {
                        Log.d("camera","entered catch");
                        Log.d("exception", "openCameraIntent: IOEXCEPTION PHOTOFILE: " + e.getMessage());
                        e.printStackTrace();
                        return;
                    }
                    Log.d("camera","check1");
                    Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", photoFile);//provider added in manifest file
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Log.d("camera","check2");
                }
                Log.d("camera","check3");
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
        else {//lower spi
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {

                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    Log.d("camera_check", "openCameraIntent: IOEXCEPTION PHOTOFILE: " + e.getMessage());
                    Toast.makeText(getApplicationContext(),"Exception occured while creating ImageFile",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return;
                }
                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                pictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(pictureIntent, IMAGE_CAPTURE_RC);
                Log.d("camera_check","check2");
            }
        }
    }

    private File createImageFile() throws IOException {
        Toast.makeText(getApplicationContext(),"check1",Toast.LENGTH_LONG).show();
        Log.d("camera_check","check1");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        Log.d("camera_check","check1 "+image);
        Toast.makeText(getApplicationContext(),"Returned value "+image,Toast.LENGTH_LONG).show();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("camera_check","check3 enter");
        Toast.makeText(getApplicationContext(),"in onActivity Result with request code "+requestCode,Toast.LENGTH_LONG).show();
        ////if (requestCode == IMAGE_CAPTURE_RC) {
        if (requestCode == 1888) {
            Log.d("camera_check","check3 if");
            Toast.makeText(getApplicationContext(),"Entered if with result code "+resultCode,Toast.LENGTH_LONG).show();
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Entered if if ",Toast.LENGTH_LONG).show();
                Log.d("camera_check","check3 if if ");
                File file = new File(imageFilePath);
                try {
                    mImagesPath.add(imageFilePath);
                    File compressedFile = new Compressor(getApplicationContext()).compressToFile(file);
                    mImages.add(compressedFile);
                    Log.d("camera_check","check3 try");
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Unable to load Image, please try again!",
                            Toast.LENGTH_SHORT).show();
                    Log.d("camera_check","check3 catch");

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void postPhotos(String tag){

        ANRequest request = AndroidNetworking.upload(imageUploadUrl)
                //.addHeaders("Authorization", "Token " + token)
                .addMultipartParameter("color",tag)
                .addMultipartFile("person", mImages.get(0))
                .setTag("Upload Images")
                //.setPriority(HIGH)
                .build();

        request.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                try{
                    String base64String= response.getString("img");
                    String base64Image = base64String.split(",")[1];
                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    makeupImg.setImageBitmap(decodedByte);
                    Toast.makeText(getApplicationContext(),"done1",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(getApplicationContext(),anError.toString(),Toast.LENGTH_LONG).show();
                Log.d("upload", "onError: " + anError.getErrorBody());
                Toast.makeText(getApplicationContext(), "Photos Upload failed, please try again ", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });

    }

    public void process_further(String tag){
        //int color = ((ColorDrawable)a1.getBackground()).getColor();
        //Toast.makeText(getApplicationContext(),"code is "+tag,Toast.LENGTH_LONG).show();
        if(mImagesPath.size()==0)
            Toast.makeText(getApplicationContext(),"You have not uploaded your image for virtual makeup try-on",Toast.LENGTH_LONG).show();
        else{
            dialog= new ProgressDialog(LipSticks.this, R.style.AlertDialog);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Processing....");
            dialog.show();
            postPhotos(tag);

        }
    }




}
