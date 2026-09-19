package it.uniba.dib.sms222316;

import static it.uniba.dib.sms222316.Utility.showToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private long backPressed;
    private static final int TIME_INTERVALL = 2000;
    Button Register, Login , loginWithGoogle, anonymousLoginButton;
    EditText emailEditText, passwordEditText;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    FirebaseUser user, mUser;
    FirebaseAuth firebaseAuth;
    GoogleSignInAccount account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_login);
        anonymousLoginButton = findViewById(R.id.Guest);
        anonymousLoginButton.setOnClickListener(v -> signInAnonymously());



        //Instanza oggetti visivi
        emailEditText = findViewById(R.id.emaillogintext);
        passwordEditText = findViewById(R.id.passwordlogintext);
        Login = findViewById(R.id.loginButton);
        loginWithGoogle = findViewById(R.id.Google);
        //Instanza variabili di login
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);
        firebaseAuth = FirebaseAuth.getInstance();
        mUser=firebaseAuth.getCurrentUser();

        account = GoogleSignIn.getLastSignedInAccount(this);


        //Creazione trigger  onclick
        Login.setOnClickListener((v)-> loginUser());
        loginWithGoogle.setOnClickListener(view -> SignIn());





        //Rimando alla schermata di registrazione
        Register = findViewById(R.id.SignIn);
        Register.setOnClickListener(v -> startActivity(new Intent(Login.this, Registration.class)));
    }


    //Avviso uscita dall'app
    @Override
    public void onBackPressed() {
        if(backPressed + TIME_INTERVALL > System.currentTimeMillis()){
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit app", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    //funzionew che gestire il Guest user
    private void signInAnonymously() {

        account = null;
        FirebaseAuth.getInstance().signOut();


        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("AnonymousLogin", "signInAnonymously:success");

                        Intent intent = new Intent(Login.this,Home.class);
                        intent.putExtra("Guest" , true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Log.w("AnonymousLogin", "signInAnonymously:failure", task.getException());
                    }
                });
    }

    //Funzione che gestisce il login classico
    void loginUser()
    {
        String mail = emailEditText.getText().toString();
        String pass = passwordEditText.getText().toString();
        boolean isValid = validateData(pass);
        if(!isValid)return;
        loginAccountInFirebase(mail, pass);
    }

    //
    boolean validateData(String Password_local){

        if(Password_local.length()<6)
        {
            passwordEditText.setError("Lunghezza della password troppo corta");
            return false;
        }
        return true;
    }
    //Login con firebase
    void loginAccountInFirebase(String mail,String pass)
    {
        firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(task -> {
            changeInProgress(false);
            if (task.isSuccessful())
            {
               HomeActivity();
            }
            else
            {
                showToast(Login.this, Objects.requireNonNull(task.getException()).getLocalizedMessage());//controllo dell'errore
            }

        });

    }
    void changeInProgress(boolean inProgress)
    {
        if(inProgress){
            Login.setVisibility(View.GONE);
        }else{
            Login.setVisibility(View.VISIBLE);
        }
    }
    //login con Google API
    private void SignIn()
    {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent,101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try { //Login Work
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());


            } catch (ApiException e) {// Login error
                showToast(this, "error");
                finish();
            }
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                         HomeActivity();
                         user = firebaseAuth.getCurrentUser();
                         updateUI();

                    } else {
                        // If sign in fails, display a message to the user.


                        finish();
                    }
                });
    }

    private void HomeActivity()
    {
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        finish();
    }

    private void updateUI()
    {
        Intent intent = new Intent(Login.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


