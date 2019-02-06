package entry.easyentry.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entry.easyentry.R;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.btnCommercial)
    Button btnCommercial;

    @BindView(R.id.btnResidential)
    Button btnResidential;

    @BindView(R.id.btnEnquriy)
    Button btnEnquiry;

    @BindView(R.id.btnLogout)
    Button btnLogout;

    private FirebaseAuth auth;
    private boolean doubleBackToExitPressed = false;

    @OnClick(R.id.btnLogout)
    void logout(){
        auth.signOut();
    }

    @OnClick(R.id.btnResidential)
    void residentialClick(){
        Intent intent = new Intent(this, ResidentialActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnCommercial)
    void commercialClick(){

    }

    @OnClick(R.id.btnEnquriy)
    void enquiryClick(){

    }

    @OnClick(R.id.newVisitor)
    void onNewVisitorClick() {
        Intent intent = new Intent(this, NewVisitorRegister.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressed) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressed = true;

        Toast.makeText(this, "Press BACK again to exit",Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed = false;
            }
        }, 2000);
    }
}
