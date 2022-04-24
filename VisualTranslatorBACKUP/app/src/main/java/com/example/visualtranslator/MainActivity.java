package com.example.visualtranslator;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    int preTranslate = 0;
    ImageView imageView1;
    String UriString = "";
    EditText editText3;
    EditText editText4;
    TextRecognizer recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());
    private String currentPhotoPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // CHINESE
        Button button91 = (Button) findViewById(R.id.button91);
        button91.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                recognizer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
                preTranslate = FirebaseTranslateLanguage.ZH;
                Toast.makeText(MainActivity.this, "Chinese & Latin Recognition", Toast.LENGTH_SHORT).show();
            }
        });

        //KOREAN
        Button button92 = (Button) findViewById(R.id.button92);
        button92.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());
                preTranslate = FirebaseTranslateLanguage.KO;
                Toast.makeText(MainActivity.this, "Korean & Latin Recognition", Toast.LENGTH_SHORT).show();
            }
        });

        //JAPANESE
        Button button93 = (Button) findViewById(R.id.button93);
        button93.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                recognizer = TextRecognition.getClient(new JapaneseTextRecognizerOptions.Builder().build());
                preTranslate = FirebaseTranslateLanguage.JA;
                Toast.makeText(MainActivity.this, "Japanese & Latin Recognition", Toast.LENGTH_SHORT).show();
            }
        });

        //HINDI
        Button button94 = (Button) findViewById(R.id.button94);
        button94.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                recognizer = TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());
                preTranslate = FirebaseTranslateLanguage.HI;
                Toast.makeText(MainActivity.this, "Hindi & Latin Recognition", Toast.LENGTH_SHORT).show();
            }
        });



        editText3 = findViewById(R.id.edittext3);
        editText4 = findViewById(R.id.edittext4);



        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);

                    currentPhotoPath = imageFile.getAbsolutePath();

                    Uri imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.visualtranslator.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1 );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        Button button1 = findViewById(R.id.button1);
        //Button button2 = findViewById(R.id.button2);
        imageView1 = findViewById(R.id.imageView);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");

            }
        });




    }//end of create


    private void translateText(int fromLanguageCode, int toLanguageCode, String source){
        //translatedTV.setText("Downloading Model...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();

        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //translatedTV.setText("Translating...");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        editText4.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to translate: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to download language model: "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                                translateText(preTranslate, FirebaseTranslateLanguage.EN, visionText.getText());

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
                                    translateText(preTranslate, FirebaseTranslateLanguage.EN, visionText.getText());




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





}