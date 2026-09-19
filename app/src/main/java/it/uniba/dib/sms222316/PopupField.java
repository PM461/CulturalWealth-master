package it.uniba.dib.sms222316;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;

import it.uniba.dib.sms222316.Gameplay.GameActivity;
import it.uniba.dib.sms222316.Gameplay.Property;

public class PopupField extends Dialog {
    Caselle caselle = new Caselle();
    ImageView[] casella;

    public PopupField(@NonNull Context context,final GameActivity activity) {
        super(context);
        context.setTheme(android.R.style.Theme_DeviceDefault_NoActionBar);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.info_field);
        casella = caselle.setCasella(context);
        Button front = findViewById(R.id.frontbutton);
        Button back = findViewById(R.id.backbutton);
        EasyFlipView flip = findViewById(R.id.flip);


        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flip.flipTheView();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flip.flipTheView();
            }
        });
    }

    public void InfoField(int field, List<Property> properties){

        TextView Name = findViewById(R.id.name);
        TextView Rent = findViewById(R.id.rent);
        TextView Sell_Price = findViewById(R.id.mortage);
        TextView Price = findViewById(R.id.price);
        TextView PaintCost = findViewById(R.id.paintingprice);
        TextView Rent1P = findViewById(R.id.rentonepaint);
        TextView Rent2P = findViewById(R.id.renttwopaint);
        TextView Rent3P = findViewById(R.id.rentthreepaint);
        TextView Rent4P = findViewById(R.id.rentfourpaint);
        TextView Description = findViewById(R.id.smalldescription);
        ImageView photo = findViewById(R.id.imagecard);
        Name.setText("");
        Price.setText("");
        Description.setText("");
        Sell_Price.setText("");
        Rent.setText("");
        Rent1P.setText("");
        Rent2P.setText("");
        Rent3P.setText("");
        Rent4P.setText("");
        PaintCost.setText("");
        photo.setImageBitmap(null);
        for (int i = 0; i < properties.size(); i++) {

                if(properties.get(i).getTipo().equals("monument")) {
                    String name = properties.get(i).getNome();
                    if (name.equals(casella[field].getContentDescription())) {

                        String imagePath = properties.get(i).getphoto(); // Sostituisci con il tuo percorso effettivo
                        Log.d("imgpath"  , "aaaa:"+ imagePath);
                        // Carica l'immagine dal percorso specifico
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

                        // Imposta l'immagine Bitmap nell'ImageView
                        photo.setImageBitmap(bitmap);

                        Name.setText(name);
                        int price = properties.get(i).getCosto();
                        Price.setText("Prezzo:" + price);
                        int paintCost = properties.get(i).getCostoQuadro();
                        PaintCost.setText("Costo Quadro:" + paintCost);
                        String description = properties.get(i).getDescrizione();
                        Description.setText(description);
                        int sell_price = properties.get(i).getCostoVendita();
                        Sell_Price.setText("Vendita:" + sell_price);
                        int[] rentarray = properties.get(i).getAffitto().stream().mapToInt(l->l).toArray();
                        int[] rent = new int[rentarray.length];
                        for (int j = 0; j < rentarray.length; j++) {
                            rent[j] = rentarray[j];
                        }
                        Rent.setText("Rendita:" + rent[0]);
                        Rent1P.setText("rendita con 1 quadro:" + rent[1]);
                        Rent2P.setText("rendita con 2 quadro:" + rent[2]);
                        Rent3P.setText("rendita con 3 quadro:" + rent[3]);
                        Rent4P.setText("rendita con 4 quadro:" + rent[4]);
                    }
                }
                else if(properties.get(i).getTipo().equals("museum")) {

                    String name = properties.get(i).getNome();
                    if (name.equals(casella[field].getContentDescription())) {
                        Name.setText(name);
                        photo.setImageResource(R.drawable.museo);
                        int price = properties.get(i).getCosto();
                        Price.setText("Prezzo:" + price);
                        String description = properties.get(i).getDescrizione();
                        Description.setText(description);
                        int sell_price = properties.get(i).getCostoVendita();
                        Sell_Price.setText("Vendita:" + sell_price);
                        int[] rentarray = properties.get(i).getAffitto().stream().mapToInt(l->l).toArray();
                        int[] rent = new int[rentarray.length];
                        for (int j = 0; j < rentarray.length; j++) {
                            rent[j] = rentarray[j];
                        }
                        Rent.setText("Rendita:" + rent[0]);
                        Rent1P.setText("rendita con 1 museo:" + rent[1]);
                        Rent2P.setText("rendita con 2 musei:" + rent[2]);
                        Rent3P.setText("rendita con 3 musei:" + rent[3]);
                    }
                }
                else if(properties.get(i).getTipo().equals("utility")) {

                    String name = properties.get(i).getNome();
                    if (name.equals(casella[field].getContentDescription())) {
                        Name.setText(name);
                        int price = properties.get(i).getCosto();
                        Price.setText("Prezzo:" + price);
                        String description = properties.get(i).getDescrizione();
                        Description.setText(description);
                        int sell_price = properties.get(i).getCostoVendita();
                        Sell_Price.setText("Vendita:" + sell_price);
                        int[] rentarray = properties.get(i).getAffitto().stream().mapToInt(l->l).toArray();
                        int[] rent = new int[rentarray.length];
                        for (int j = 0; j < rentarray.length; j++) {
                            rent[j] = rentarray[j];
                        }
                        Rent.setText("Rendita:" + rent[0]);
                    }
                }


            }



        ImageView Image = findViewById(R.id.imagecard);
    }
}
