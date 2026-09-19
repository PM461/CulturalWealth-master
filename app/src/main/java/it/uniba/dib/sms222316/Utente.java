package it.uniba.dib.sms222316;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Utente {
    private List<DocumentReference> posseduti;
    private String ProfilePic;
    private String email;
    private String friend_code;
    private String name;
    private Long points;
    private boolean me;




    public Utente(String name,String email, Long points, String ProfilePic ){
        this.name = name;
        this.email = email;
        this.points = points;
        this.ProfilePic = ProfilePic;
        this.me = false;


    }
    public Utente(String name,String email, Long points, String ProfilePic , boolean me){
        this.name = name;
        this.email = email;
        this.points = points;
        this.ProfilePic = ProfilePic;
        this.me = me;


    }

    public String getName(){
        return name;
    }
    public Long getScore(){
        return points;
    }

    public String getMail(){
        return email;
    }
    public String getProfilePic(){
        return ProfilePic;
    }

    public boolean isMe() {
        return me;
    }
}
