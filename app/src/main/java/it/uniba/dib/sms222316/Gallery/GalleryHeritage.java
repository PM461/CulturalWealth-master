package it.uniba.dib.sms222316.Gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

import it.uniba.dib.sms222316.R;

public class GalleryHeritage extends AppCompatActivity {

    ImageButton btnBack;
    ImageView img;
    TextView title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_gallery_heritage);

        btnBack = findViewById(R.id.btnBack);


        //Button back
        btnBack.setOnClickListener(v -> {
            finish();
        });




        img = findViewById(R.id.imgHeritage);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        String tl = getIntent().getExtras().getString("Title");
        String desc = getIntent().getExtras().getString("Description");

        switch (getIntent().getExtras().getString("Type")){
            case "Monuments":
                File Monuments = new File(GalleryHeritage.this.getFilesDir() ,"CulturalWealth/Monuments/" + getIntent().getExtras().getString("Image"));
                Bitmap bitmapMonuments = BitmapFactory.decodeFile(Monuments.getAbsolutePath());
                img.setImageBitmap(bitmapMonuments);
                break;
            case "Paintings":
                File Paintings = new File(GalleryHeritage.this.getFilesDir() ,"CulturalWealth/Paintings/" + getIntent().getExtras().getString("Image"));
                Bitmap bitmapPaintings = BitmapFactory.decodeFile(Paintings.getAbsolutePath());
                img.setImageBitmap(bitmapPaintings);
                break;
            case "Characters":
                File Characters  = new File(GalleryHeritage.this.getFilesDir() ,"CulturalWealth/ProfilesPictures/" + getIntent().getExtras().getString("Image"));
                Bitmap bitmapCharacters = BitmapFactory.decodeFile(Characters.getAbsolutePath());
                img.setImageBitmap(bitmapCharacters);
                break;
        }


        title.setText(tl);
        description.setMovementMethod(new ScrollingMovementMethod());
        description.setText(desc);






    }

}