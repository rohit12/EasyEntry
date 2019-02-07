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


    private FirebaseAuth auth;
    private boolean doubleBackToExitPressed = false;


    @OnClick(R.id.newVisitor)
    void onNewVisitorClick() {
        Intent intent = new Intent(this, ResidentialActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
    }

}
