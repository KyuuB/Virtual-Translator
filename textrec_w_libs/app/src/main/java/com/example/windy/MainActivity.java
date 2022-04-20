package com.example.windy;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions;
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    ImageView imageView1;
    String UriString = "";

    //new v

    EditText editText3;

    TextRecognizer recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());




    //new ^

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Button button91 = findViewById(R.id.button91);
//
//
//        button91.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                TextRecognizer recognizer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
//
//            }
//        });





//        findViewById(R.id.button91).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recognizer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
//            }
//        });



        Button button91 = (Button) findViewById(R.id.button91);
        button91.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                recognizer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
            }
        });


        Button button92 = (Button) findViewById(R.id.button92);
        button92.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());
            }
        });


        Button button93 = (Button) findViewById(R.id.button93);
        button93.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                recognizer = TextRecognition.getClient(new JapaneseTextRecognizerOptions.Builder().build());
            }
        });

        Button button94 = (Button) findViewById(R.id.button94);
        button94.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                recognizer = TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());
            }
        });










        //new v

        //new v
        editText3 = findViewById(R.id.edittext3);
        //new ^


        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);

                    currentPhotoPath = imageFile.getAbsolutePath();

                    Uri imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.windy.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1 );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        //new ^


        Button button1 = findViewById(R.id.button1);
        //Button button2 = findViewById(R.id.button2);
        imageView1 = findViewById(R.id.imageView);



        ///////vvvvvvvvvvvvvtesting

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");

            }
        });



    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    imageView1.setImageURI(uri);
                    UriString = uri.toString();

                    try {
                        InputImage image = InputImage.fromFilePath(getApplicationContext(), uri);
                        Task<Text> result =
                                recognizer.process(image)
                                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                                            @Override
                                            public void onSuccess(Text visionText) {
                                                // Task completed successfully
                                                editText3.setText(visionText.getText());
                                            }
                                        })
                                        .addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Task failed with an exception
                                                    }
                                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });




    ///new
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK){

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);

            InputImage image = InputImage.fromBitmap(bitmap, 0);

            Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    // Task completed successfully
                                    // ...
                                    editText3.setText(visionText.getText());




                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                        }
                                    });



        }
    }


    //new






}


