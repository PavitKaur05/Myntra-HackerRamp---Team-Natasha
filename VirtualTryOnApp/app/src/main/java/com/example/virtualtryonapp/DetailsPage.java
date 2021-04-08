package com.example.virtualtryonapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import id.zelory.compressor.Compressor;

import static com.bumptech.glide.Priority.HIGH;

public class DetailsPage extends AppCompatActivity {
    private TextView lproductBrand,lproductName,lproductPrice;
    ImageView lproductImage;
    Button btn1,btn2,selectGallery,clickImage;
    LinearLayout btn1Options;

    boolean isFirstPic = true;
    private static int IMAGE_CAPTURE_RC = 123;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private String imageFilePath,token;
    private ArrayList<File> mImages;
    private ArrayList<String> mImagesPath;
    ImageButton clickPhoto;
    Button openCamera;
    Button save;
    private int photosUploadedCount = 0;
    int i=1;
    String imageUploadUrl="http://e74957664ee2.ngrok.io/api/tryon/";
    ConstraintLayout clickDialog;
    ImageView imageToSend;

    String id;
    ProgressDialog dialog;



    AlertDialog reportSubmitLoading;

    private static final int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_page);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String brand = intent.getStringExtra("brand");
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String imageLink = intent.getStringExtra("imageLink");
        //Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();
        //System.out.println("message"+id);
        //Log.d("message",id);


        lproductImage = findViewById(R.id.productimg);
        lproductBrand = findViewById(R.id.productbrand);
        lproductName = findViewById(R.id.producttype);
        lproductPrice = findViewById(R.id.productprice);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn1Options = findViewById(R.id.ll_options);
        selectGallery = findViewById(R.id.select_gallery);
        clickImage = findViewById(R.id.click_image);

        clickDialog = findViewById(R.id.dialog_page);
        imageToSend = findViewById(R.id.image_to_send);

        lproductBrand.setText(brand);
        lproductName.setText(name);
        lproductPrice.setText("Rs. "+price);
        Picasso.get().load(imageLink).into(lproductImage);

        mImagesPath = new ArrayList<>();
        mImages = new ArrayList<>();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btn1Options.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(),"see options",Toast.LENGTH_LONG).show();
                clickDialog.setVisibility(View.VISIBLE);
            }

        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });

        selectGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(),"see",Toast.LENGTH_LONG).show();
                /*
                btn1Options.setVisibility(View.GONE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);

                 */
                postPhotos(0);


            }

        });

        clickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"see",Toast.LENGTH_LONG).show();
                openCameraIntent();
                //if(!imageFilePath.equals(""))

            }

        });

    }

    //for gallery selection
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                Toast.makeText(getApplicationContext(),"path is "+selectedImagePath,Toast.LENGTH_LONG).show();
                //img.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/

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
        //Toast.makeText(getApplicationContext(),"check1",Toast.LENGTH_LONG).show();
        Log.d("camera_check","check1");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        Log.d("camera_check","check1 "+image);
        //Toast.makeText(getApplicationContext(),"Returned value "+image,Toast.LENGTH_LONG).show();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("camera_check","check3 enter");
        //Toast.makeText(getApplicationContext(),"in onActivity Result with request code "+requestCode,Toast.LENGTH_LONG).show();
        ////if (requestCode == IMAGE_CAPTURE_RC) {
        if (requestCode == 1888) {
            Log.d("camera_check","check3 if");
            //Toast.makeText(getApplicationContext(),"Entered if with result code "+resultCode,Toast.LENGTH_LONG).show();
            if (resultCode == RESULT_OK) {
                //Toast.makeText(getApplicationContext(),"Entered if if ",Toast.LENGTH_LONG).show();
                Log.d("camera_check","check3 if if ");
                File file = new File(imageFilePath);//http://31e1a092b945.ngrok.io/api/testimg
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

    public void uploadingPhotos() {
         Toast.makeText(getApplicationContext(),"Eneterd uploading photos function",Toast.LENGTH_LONG).show();
        reportSubmitLoading = new SpotsDialog.Builder().setContext(getApplicationContext()).setMessage("Uploading Images")
                .setTheme(R.style.CustomDialog)
                .setCancelable(false)
                .build();
        reportSubmitLoading.show();
    }


    public void postPhotos(int value){
        dialog= new ProgressDialog(DetailsPage.this, R.style.AlertDialog);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading....");
        dialog.show();


        ANRequest request = AndroidNetworking.upload(imageUploadUrl)
                //.addHeaders("Authorization", "Token " + token)
                .addMultipartParameter("cloth",id)
                .addMultipartFile("person", mImages.get(0))
                //.addMultipartFile("person", mImages.get(0))
                .setTag("Upload Images")
                //.setPriority(HIGH)
                .build();

        request.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                try{
                    String base64String= response.getString("img");
                    /*
                    byte[] imageAsBytes = Base64.decode(imageString.getBytes(), Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                    Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
                    lproductImage.setImageBitmap(decodedImage);

                     */
                    //String base64String = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAA...";
                    String base64Image = base64String.split(",")[1];
                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    lproductImage.setImageBitmap(decodedByte);
                    Toast.makeText(getApplicationContext(),"done1",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    clickDialog.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"exception",Toast.LENGTH_LONG).show();
                    clickDialog.setVisibility(View.GONE);
                    e.printStackTrace();
                }
                /*
                photosUploadedCount++;
                i++;
                if (photosUploadedCount == 2) {
                    //afterUploading();
                    //reportSubmitLoading.dismiss();
                    Toast.makeText(getApplicationContext(), "Images Uploaded successfully.", Toast.LENGTH_SHORT).show();
                    //Intent i = new Intent(ImageSubmit.this,MainActivity.class);
                    //startActivity(i);

                }
                else {
                     Toast.makeText(getApplicationContext(),"Uploading Image " + (i),Toast.LENGTH_LONG).show();
                    postPhotos(1);
                    //uploadingPhotos();
                }

                 */
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(getApplicationContext(),anError.toString(),Toast.LENGTH_LONG).show();
                Log.d("upload", "onError: " + anError.getErrorBody());
                Toast.makeText(getApplicationContext(), "Photos Upload failed, please try again ", Toast.LENGTH_SHORT).show();
                //reportSubmitLoading.dismiss();
                dialog.dismiss();
            }

        });

    }

    public void getData(String url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                /*
                try {
                    JSONObject abc= response;
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "An exception occurred", Toast.LENGTH_LONG).show();
                    //spinner.setVisibility(View.GONE);
                    Log.d("user_list", "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }

                 */


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //spinner.setVisibility(View.GONE);
                if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(getApplicationContext(), "This error is case2", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(getApplicationContext(), "This error is server error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getApplicationContext(), "This error is case4", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getApplicationContext(), "This error is case5", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

}

