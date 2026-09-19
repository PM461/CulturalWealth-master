package it.uniba.dib.sms222316.Gameplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextThemeWrapper;
import android.view.Display;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import it.uniba.dib.sms222316.Caselle;
import it.uniba.dib.sms222316.Home;
import it.uniba.dib.sms222316.PopupField;
import it.uniba.dib.sms222316.R;
import it.uniba.dib.sms222316.endgame_popup;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import java.util.stream.Collectors;

public class GameActivity extends AppCompatActivity {

    int terzo = 0;
    int secondo = 0;
    int primo = 0;
    Caselle caselle = new Caselle();
    ImageView[] casella;
    boolean isViewCreated = false;
    private TextView playerScoreTextView;
    private TextView playerScoreTextView2;
    private TextView playerScoreTextView3;
    String GameId;
    Game game;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        int money = intent.getIntExtra("money", 1500);
        int icon = intent.getIntExtra("puddle", 0);
        GameId = intent.getStringExtra("GameId");
        TextView playerNameTextView = findViewById(R.id.name1);
        playerScoreTextView = findViewById(R.id.money1);
        playerScoreTextView2 = findViewById(R.id.money2);
        playerScoreTextView3 = findViewById(R.id.money3);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Log.d("Display" ," w: "+displayMetrics.widthPixels+" h: " +displayMetrics.heightPixels+ "t: "+displayMetrics.widthPixels * displayMetrics.heightPixels );
        int Thisthisplay = displayMetrics.widthPixels * displayMetrics.heightPixels;
        int basedisplay = 2255040;
        float mScale = Thisthisplay/(basedisplay);
        if (Thisthisplay> basedisplay+500000)mScale = 0.6f;
        else mScale = 1f;
        HorizontalScrollView layout = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewZoom);
        int[] location = new int[2];
        layout.getLocationOnScreen(location);

        int x = location[0]; // coordinata x rispetto allo schermo
        int y = location[1];
        // Calcola il centro dello schermo
        int screenWidth = size.x;
        int screenHeight = size.y;
        //int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f , 1f / mScale, 1f, 1f / mScale, x, y+centerY);
        ConstraintLayout layoutm = (ConstraintLayout) findViewById(R.id.Icons);
        scaleAnimation.setDuration(0);
        scaleAnimation.setFillAfter(true);
        layoutm.startAnimation(scaleAnimation);

        casella = caselle.setCasella(this);

        //Creazione lista Giocatori
        List<Player> players = new ArrayList<>();


        String  currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        playerNameTextView = findViewById(R.id.name1);findusername(!Home.Guest?currentId:"Guest" , playerNameTextView);
        TextView playerNameTextView2 = findViewById(R.id.name2);
        playerNameTextView2.setText("Guest 2");
        TextView playerNameTextView3 = findViewById(R.id.name33);
        playerNameTextView3.setText("Guest 3");
        players.add(new Player("Giocatore 1",0, money));
        players.add(new Player("Giocatore 2", 1, money));
        players.add(new Player("Giocatore 3", 2, money));

        ImageView pedina1 = findViewById(R.id.pedina1);
        switch (icon)
        {
            case 1 :pedina1.setImageResource(R.drawable.culture_japan_japanese_svgrepo_com);break;
            case 2 :pedina1.setImageResource(R.drawable.abstract_painting_ic_national_culture_paris_svgrepo_com);break;
            case 3 :pedina1.setImageResource(R.drawable.moai_svgrepo_com);break;
            case 4 :pedina1.setImageResource(R.drawable.icon_rank_149);break;
            case 5 :pedina1.setImageResource(R.drawable.japan_monument_svgrepo_com);break;
            default:pedina1.setImageResource(R.drawable.japan_monument_svgrepo_com);break;
        }

        //Lettura file JSON delle proprieta
        List<Property> properties = new ArrayList<>();
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("property", "raw", getPackageName());
        InputStream inputStream = resources.openRawResource(resourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonString = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject0 = new JSONObject(String.valueOf(jsonString));
            JSONArray jsonArray = jsonObject0.getJSONArray("properties");

            //Creazione oggetti Proprietà
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("type").equals("monument")){
                String name = jsonObject.getString("name");
                String type = jsonObject.getString("type");
                String group = jsonObject.getString("group");
                String description = jsonObject.getString("description_it");
                int price = jsonObject.getInt("price");
                JSONArray rentarray = jsonObject.getJSONArray("rent");
                int[] rent = new int[rentarray.length()];
                for (int j = 0; j < rentarray.length(); j++) {
                    rent[j] = rentarray.getInt(j);
                }
                int paintCost = jsonObject.getInt("paintCost");
                int sell_price = jsonObject.getInt("sell_price");
                int posizione = jsonObject.getInt("position");
                String foto = jsonObject.getString("foto_path");
                Property property = new Property(name,type,group,description,price,Arrays.stream(rent).boxed().collect(Collectors.toList()),paintCost,sell_price,posizione,foto);
                properties.add(property);
                }
                if(jsonObject.getString("type").equals("museum") || jsonObject.getString("type").equals("utility")){
                    String name = jsonObject.getString("name");
                    String type = jsonObject.getString("type");
                    String group = "";
                    String description = jsonObject.getString("description_it");
                    int price = jsonObject.getInt("price");
                    JSONArray rentarray = jsonObject.getJSONArray("rent");
                    int[] rent = new int[rentarray.length()];
                    for (int j = 0; j < rentarray.length(); j++) {
                        rent[j] = rentarray.getInt(j);
                    }
                    int paintCost = 0;
                    int sell_price = jsonObject.getInt("sell_price");
                    int posizione = jsonObject.getInt("position");
                    String foto = "";

                    Property property = new Property(name,type,group,description,price,Arrays.stream(rent).boxed().collect(Collectors.toList()),paintCost,sell_price,posizione,foto);
                    properties.add(property);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Instanza oggetto partita
        if(GameId!=null){

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            //controllo guest, guest non dovrebbe neanche arrivare a questo punto. Possibilità di carica partita gia bloccate dal bottone
            db.collection("Users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("Games").document(GameId)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        // Elabora gli oggetti recuperati

                            game = querySnapshot.toObject(Game.class);
                        for (Property property: game.getProperties())
                        {

                            if (property.getGiocatore() != null) {
                               property.setGiocatore(game.getPlayers().get(property.getGiocatore().getIcon()));

                            }

                        }
                            game.iniziaPartita();
                            gioco();


                    })
                    .addOnFailureListener(e -> {
                        // Errore: Impossibile recuperare gli oggetti
                    });


        }else{
            game = new Game(players , properties);
            game.iniziaPartita();
            gioco();
        }

    }

    public void gioco(){

        Button endturn = findViewById(R.id.endTurn);
        Button info = findViewById(R.id.Info);
        Button buy = findViewById(R.id.buy);
        ImageButton exit = findViewById(R.id.btnBack);

        info.setVisibility(View.INVISIBLE);
        buy.setVisibility(View.INVISIBLE);


        List<Player> players = game.getPlayers();
        List<Property> properties = game.getProperties();

        updateUI();
        updateUI(players);


        //Calcolo giocatori
        ImageView[] pedina = new ImageView[game.getNumberOfPlayers()];
        int[] position = new int[game.getNumberOfPlayers()];
        pedina[0] = findViewById(R.id.pedina1);
        pedina[1] = findViewById(R.id.pedina2);
        pedina[2] = findViewById(R.id.pedina3);


        for (int i = 0; i < players.size(); i++) position[i] = players.get(i).getPosition();


        PopupField field = new PopupField(GameActivity.this, GameActivity.this);
        Button rollDice = findViewById(R.id.dado);


        //Creazione animazione Dadi
        View view = findViewById(R.id.relativeLayout);
        GifImageView Dice1 = findViewById(R.id.dice1);
        GifImageView Dice2 = findViewById(R.id.dice2);
        int random[] = game.dadi();
        int lastFrameIndex1 = rollDice(random[0]).getNumberOfFrames() - 1;
        Dice1.setImageBitmap(rollDice(random[0]).seekToFrameAndGet(lastFrameIndex1));
        int lastFrameIndex2 = rollDice(random[1]).getNumberOfFrames() - 1;
        Dice2.setImageBitmap(rollDice(random[1]).seekToFrameAndGet(lastFrameIndex2));


        // Registra un OnGlobalLayoutListener sulla vista
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Verifica se la vista è stata già creata
                if (!isViewCreated) {

                    for (int i = 0; i < position.length; i++) {
                        //casella[position[i]].getLocationInWindow(coordinate);

                        pedina[i].setX(casella[position[i]].getX());
                        pedina[i].setY( casella[position[i]].getY());
                    }

                    isViewCreated = true;
                }

                // Rimuovi il listener per evitare di essere chiamato nuovamente
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        exit.setOnClickListener(v1 -> Exit());
        endturn.setVisibility(View.INVISIBLE);
        rollDice.setOnClickListener(v -> {

            //Lancio dei dadi
            if (game.getCurrentPlayer().getMoney() <= 0) {
                players.get(game.getCurrentPlayerIndex()).setBankrupt();
                updateUI(players);
                if (terzo != 0) terzo = game.getCurrentPlayerIndex();
                else secondo = game.getCurrentPlayerIndex();

            }
            int[] numeri = game.dadi();
            if (!game.isGameStarted()) {
                primo = game.getCurrentPlayerIndex();

                //popup classifica
                int punteggioprimo = players.get(primo).getScore();
                int punteggiosecondo = players.get(secondo).getScore();
                int punteggioterzo = players.get(terzo).getScore();
                String nomeprimo = players.get(primo).getName();
                String nomesecondo = players.get(secondo).getName();
                String nometerzo = players.get(terzo).getName();

                Intent i = new Intent(GameActivity.this, endgame_popup.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("punteggioprimo", punteggioprimo);
                i.putExtra("punteggiosecondo", punteggiosecondo);
                i.putExtra("punteggioterzo", punteggioterzo);
                i.putExtra("nomeprimo", nomeprimo);
                i.putExtra("nomesecondo", nomesecondo);
                i.putExtra("nometerzo", nometerzo);
                startActivity(i);
            }
            if (game.getCurrentPlayer().isPrison()) {
                int turno = game.getCurrentPlayer().getTurnPrison();

                rollDice.setEnabled(false);

                GifDrawable Gif1 = rollDice(numeri[0]);
                GifDrawable Gif2 = rollDice(numeri[1]);
                Gif1.reset(); // Resetta la GIF all'inizio
                Gif1.start(); // Inizia animazione GIF
                Dice1.setImageDrawable(Gif1);
                new Handler().postDelayed(() -> {
                    Gif2.reset(); // Resetta la GIF all'inizio
                    Gif2.start(); // Inizia animazione GIF
                    Dice2.setImageDrawable(Gif2);
                }, 200);

                if (numeri[0] == numeri[1]) {
                    game.getCurrentPlayer().setPrison(false);
                    game.getCurrentPlayer().setTurnPrison(0);
                    new Handler().postDelayed(() -> rollDice.callOnClick(), 4500);
                } else {
                    if (turno > 2) {
                        game.getCurrentPlayer().setPrison(false);
                        game.getCurrentPlayer().setTurnPrison(0);
                        endturn.setVisibility(View.VISIBLE);
                    } else {
                        game.getCurrentPlayer().setTurnPrison(game.getCurrentPlayer().getTurnPrison() + 1);
                        endturn.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                //players.get(1).setBankrupt();
                //players.get(2).setBankrupt();
                rollDice.setEnabled(false);
                GifDrawable Gif1 = rollDice(numeri[0]);
                GifDrawable Gif2 = rollDice(numeri[1]);
                Gif1.reset(); // Resetta la GIF all'inizio
                Gif1.start(); // Inizia animazione GIF
                Dice1.setImageDrawable(Gif1);
                new Handler().postDelayed(() -> {
                    Gif2.reset(); // Resetta la GIF all'inizio
                    Gif2.start(); // Inizia animazione GIF
                    Dice2.setImageDrawable(Gif2);
                }, 200);


                int currentPlayer = game.getCurrentPlayerIndex();

                Path path = new Path();
                path.moveTo(casella[position[currentPlayer]].getX(), casella[position[currentPlayer]].getY());
                for (int i = 1; i <= (numeri[0] + numeri[1]); i++) {
                    position[currentPlayer]++;
                    if (position[currentPlayer] == 40) {
                        position[currentPlayer] = 0;
                        players.get(currentPlayer).addMoney(100);
                        /*To do alert that money has been added*/
                    }
                    path.lineTo(casella[position[currentPlayer]].getX(), casella[position[currentPlayer]].getY());
                }
                if (position[currentPlayer] == 30) {
                    position[currentPlayer] = 10;
                    game.getCurrentPlayer().setPrison(true);
                    path.lineTo(casella[position[currentPlayer]].getX(), casella[position[currentPlayer]].getY());
                }
                if (position[currentPlayer] == 2 || position[currentPlayer] == 20 || position[currentPlayer] == 33 || position[currentPlayer] == 38 ) {
                    game.getCurrentPlayer().removeMoney(100);
                }
                if (position[currentPlayer] == 4 || position[currentPlayer] == 17 || position[currentPlayer] == 22 || position[currentPlayer] == 36  ) {
                    game.getCurrentPlayer().addMoney(100);
                }
                new Handler().postDelayed(() -> {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(pedina[currentPlayer], pedina[currentPlayer].X, pedina[currentPlayer].Y, path);
                    animator.setDuration(2000);
                    animator.start();
                    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(@NonNull Animator animation) {

                        }
                        @Override
                        public void onAnimationEnd(@NonNull Animator animation) {

                            endturn.setVisibility(View.VISIBLE);
                            Optional<Property> currentProperty = properties.stream().filter(l -> l.getPosizione() == position[currentPlayer]).findFirst();
                            if (currentProperty.isPresent()) {
                                info.setVisibility(View.VISIBLE);
                                if (currentProperty.get().isAvaible())
                                    buy.setVisibility(View.VISIBLE);
                                else {
                                    game.gestisciPagamentoAffitto(currentProperty.get());
                                }
                            }
//                            for (Property prop : properties) {
//                                if (prop.getPosizione() == position[currentPlayer] && prop.getGiocatore() == null)
//                                    buy.setVisibility(View.VISIBLE);
//                            }
                        }

                        @Override
                        public void onAnimationCancel(@NonNull Animator animation) {

                        }
                        @Override
                        public void onAnimationRepeat(@NonNull Animator animation) {
                        }
                    };
                    animator.addListener(animatorListener);
                }, 3500);
            }
            buy.setOnClickListener(new View.OnClickListener() {
                /**
                 * When you have no money should still be visible
                 */
                @Override
                public void onClick(View v) {
                    int currentPlayer = game.getCurrentPlayerIndex();

                    for (Property prop : properties) {
                        //if(prop.getTipo().equals("monument")) { Puoi comprare anche i musei e le centrali, if inutile
                        String name = prop.getNome();

                        if (name.equals(casella[position[currentPlayer]].getContentDescription())) {
                            game.gestisciAcquisto(players.get(currentPlayer), prop);
                            buy.setVisibility(View.INVISIBLE);
                        }
                        //}
                        if (!prop.isAvaible()) {
                            updateUI(prop.getPosizione(), prop.getGiocatore().getIcon());
                        }
                    }
                }
            });
            updateUI(players);
            int currentPlayer = game.getCurrentPlayerIndex();
            info.setOnClickListener(v1 -> {
                field.InfoField(position[currentPlayer], properties);
                field.show();
            });

            ConstraintLayout Player = findViewById(R.id.players);
            for (int i = 0; i < players.size(); i++) {
                ConstraintLayout rl = (ConstraintLayout) Player.getChildAt(i);
                if (i != currentPlayer) {

                    int j = i;
                    rl.setOnClickListener(v2 -> {
                        TradeProposal(currentPlayer, j);
                        rl.setOnClickListener(null);
                    });
                } else {
                    rl.setBackgroundResource(R.drawable.currentplayer_focus);
                }
            }
            players.get(currentPlayer).setPosition(position[currentPlayer]);
        });
        endturn.setOnClickListener(v -> {
            game.endTurn(players);
            ConstraintLayout Player = findViewById(R.id.players);
            for (int i = 0; i < players.size(); i++) {
                Player.getChildAt(i).setOnClickListener(null);
                Player.getChildAt(i).setBackgroundResource(R.drawable.rounded);
            }
            endturn.setVisibility(View.INVISIBLE);
            info.setVisibility(View.INVISIBLE);
            buy.setVisibility(View.INVISIBLE);
            rollDice.setEnabled(true);
            updateUI(players);

        });
    }
    public void TradeProposal(int offerer, int recipient){

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomAlertDialogStyle));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.trade_popup, null);
        builder.setView(dialogView);

        ListView listView1 = dialogView.findViewById(R.id.ListOfferer);
        ListView listView2 = dialogView.findViewById(R.id.ListRecipient);


        List<Player> players = game.getPlayers();
        List<Property> properties = game.getProperties();

        List<Property> items1 = properties.stream().filter(l -> {
            if (!l.isAvaible()) return l.getGiocatore().equals(players.get(offerer));
            return false;
        }).collect(Collectors.toList());
        List<Property> items2 = properties.stream().filter(l -> {
            if (!l.isAvaible()) return l.getGiocatore().equals(players.get(recipient));
            return false;
        }).collect(Collectors.toList());


        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, items1.stream().map(Property::getNome).collect(Collectors.toList()));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, items2.stream().map(Property::getNome).collect(Collectors.toList()));
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);

        listView1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        builder.setPositiveButton("OK", (dialog, which) -> {
            SparseBooleanArray checked1 = listView1.getCheckedItemPositions();
            SparseBooleanArray checked2 = listView2.getCheckedItemPositions();
            List<Property> selectedItems1 = new ArrayList<>();
            List<Property> selectedItems2 = new ArrayList<>();
            for (int i = 0; i < checked1.size(); i++) {
                if (checked1.valueAt(i)) {
                    selectedItems1.add(items1.get(checked1.keyAt(i)));
                }
            }
            for (int i = 0; i < checked2.size(); i++) {
                if (checked2.valueAt(i)) {
                    selectedItems2.add(items2.get(checked2.keyAt(i)));
                }
            }
            if(selectedItems1.size()>0 && selectedItems2.size()>0)  TradeResponse(selectedItems1, selectedItems2);
            else Toast.makeText(this, "Serve almeno una proprietà per giocatore",Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }
    public void TradeResponse(List<Property> OfferedProperty, List<Property> RequestedProperty){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomAlertDialogStyle));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.trade_popup, null);
        builder.setView(dialogView);
        Player Offerer = OfferedProperty.get(0).getGiocatore();
        Player Recipient = RequestedProperty.get(0).getGiocatore();
        ListView listView1 = dialogView.findViewById(R.id.ListOfferer);
        ListView listView2 = dialogView.findViewById(R.id.ListRecipient);



        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, OfferedProperty.stream().map(Property::getNome).collect(Collectors.toList()));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, RequestedProperty.stream().map(Property::getNome).collect(Collectors.toList()));


        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);

        builder.setPositiveButton("Accept", (dialog, which) -> {
            for (int i = 0; i < OfferedProperty.size(); i++) {
                OfferedProperty.get(i).setGiocatore(Recipient);
            }
            for (int i = 0; i < RequestedProperty.size(); i++) {
                RequestedProperty.get(i).setGiocatore(Offerer);
            }
            updateUI();
        });

        builder.setNegativeButton("Refuse", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    public void Exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomAlertDialogStyle));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.save, null);

        builder.setView(dialogView);


        builder.setPositiveButton(R.string.save, (dialog, which) -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Funzione Per salvare la partita
            //Game GameToSave = game.clone(); Ideato un modo per clonare l'oggetto per non avere conflitti, da pensare se serve integrarlo
            if(!Home.Guest) {
                if (GameId!=null)
                {
                    db.collection("Users")
                            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .collection("Games").document(GameId)
                            .set(game)
                            .addOnSuccessListener(documentReference -> {

                                Intent i = new Intent(GameActivity.this, Home.class );
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                //Errore: L'aggiunta dell'oggetto non è riuscita
                            });
                }
                else
                {
                    db.collection("Users")
                            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .collection("Games")
                            .add(game)
                            .addOnSuccessListener(documentReference -> {

                                Intent i = new Intent(GameActivity.this, Home.class );
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                //Errore: L'aggiunta dell'oggetto non è riuscita
                            });
                }
            }else{
                Toast.makeText(this, "Guest cant save a game",Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton(R.string.exit, (dialog, which) -> finish());

        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    private void updateUI(){
        ConstraintLayout BloccoIcone = findViewById(R.id.Icons);
        List<Property> properties = game.getProperties();

        for (Property prop:properties) {
            int ImagePlayer;
            ImageView icona = (ImageView) BloccoIcone.getChildAt(prop.getPosizione()+40);
            if(!prop.isAvaible())     ImagePlayer = getResources().getIdentifier("_"+prop.getGiocatore().getIcon(), "drawable", getPackageName());
            else    ImagePlayer = getResources().getIdentifier("_-1", "drawable", getPackageName());
            icona.setImageResource(ImagePlayer);
        }
    }
    private void updateUI(int Position, int playerIcon){
        ConstraintLayout BloccoIcone = findViewById(R.id.Icons);
        ImageView icona = (ImageView) BloccoIcone.getChildAt(Position+40);
        int ImagePlayer = getResources().getIdentifier("_"+playerIcon, "drawable", getPackageName());
        icona.setImageResource(ImagePlayer);
    }

    private Player updateUI(List<Player> players) {
        Player currentPlayer = game.getCurrentPlayer();

        int money1 = players.get(0).getMoney();
        int money2 = players.get(1).getMoney();
        int money3 = players.get(2).getMoney();

        if (players.get(0).isBankrupt())playerScoreTextView.setText("BANCAROTTA");else playerScoreTextView.setText(money1+ "$");
        if (players.get(1).isBankrupt())playerScoreTextView2.setText("BANCAROTTA");else playerScoreTextView2.setText(money2+ "$");
        if (players.get(2).isBankrupt())playerScoreTextView3.setText("BANCAROTTA");else playerScoreTextView3.setText(money3+ "$");
    return currentPlayer;
    }
    private void findusername(String DocId , TextView v)
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(DocId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> v.setText(queryDocumentSnapshots.getString("nome")));


    }

    private GifDrawable rollDice(int number){
        int gifResourceId = 0;
        switch (number){
            case 1: gifResourceId = R.drawable.dado1;break;
            case 2: gifResourceId = R.drawable.dado2;break;
            case 3: gifResourceId = R.drawable.dado3;break;
            case 4: gifResourceId = R.drawable.dado4;break;
            case 5: gifResourceId = R.drawable.dado5;break;
            case 6: gifResourceId = R.drawable.dado6;break;
        }
        GifDrawable Gif;
        try {
            Gif = new GifDrawable(getResources(), gifResourceId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Gif.setLoopCount(1);

        return Gif;
    }


}