package entry.easyentry.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
