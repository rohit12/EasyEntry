package entry.easyentry.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entry.easyentry.R;
import entry.easyentry.dao.FirebaseSocietyDao;

public class LoginActivity extends AppCompatActivity {

    // TODO Add Firebase Crashalytics

    @BindView(R.id.editTextLoginEmail)
    EditText editTextEmail;

    @BindView(R.id.editTextLoginPassword)
    EditText editTextPassword;

    @BindView(R.id.btnLoginSignin)
    Button btnLogin;

    private FirebaseAuth auth;
    private static final String TAG = "LoginActivity";
    private FirebaseSocietyDao societyDao;

    @OnClick(R.id.btnLoginSignin)
    void login(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: User successfully logged in");
                            Toast.makeText(LoginActivity.this,"User logged in successfully",Toast.LENGTH_LONG).show();
                            // Start Main Activity on successful login
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            FirebaseUser user = auth.getCurrentUser();
                            societyDao.getSocietyInformation(user.getEmail());
                        }
                        else{
                            Log.d(TAG, "onComplete: User not logged in.");
                            Toast.makeText(LoginActivity.this,"Check credentials again.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        societyDao = new FirebaseSocietyDao(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser!=null){
            // Start MainActivity if the user is currently logged in.
            Log.d(TAG, "onStart: User logged in. Starting MainActivity");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
