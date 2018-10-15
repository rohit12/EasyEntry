package entry.easyentry.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entry.easyentry.R;
import entry.easyentry.dao.Dao;
import entry.easyentry.dao.FirebaseVisitorDao;
import entry.easyentry.models.Visitor;
import entry.easyentry.services.OTPService;

public class ResidentialActivity extends AppCompatActivity {

    private boolean verifiedNumber = false;
    private Dao visitorDao;
    private OTPService otpService;

    @BindView(R.id.editTextResidentialName)
    EditText editTextName;

    @BindView(R.id.editTextResidentialDate)
    EditText editTextDate;

    @BindView(R.id.editTextResidentialFlatNumber)
    EditText editTextFlatNumber;

    @BindView(R.id.editTextResidentialOTP)
    EditText editTextOTP;

    @BindView(R.id.editTextResidentialPhoneNumber)
    EditText editTextPhoneNumber;

    @BindView(R.id.editTextResidentialTimein)
    EditText editTextTimeIn;

    @BindView(R.id.btnResidentialSubmit)
    Button btnSubmit;

    @BindView(R.id.btnResidentialVerifyPhoneNumber)
    Button btnVerifyNumber;

    @OnClick(R.id.btnResidentialSubmit)
    void submit(){
        String name = editTextName.getText().toString();
        String date = editTextDate.getText().toString();
        String flatNumber = editTextFlatNumber.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String timeIn = editTextTimeIn.getText().toString();

        Visitor visitor = new Visitor(name, flatNumber, timeIn, date, phoneNumber);
    }

    @OnClick(R.id.btnResidentialVerifyPhoneNumber)
    void verify(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residential);
        visitorDao = new FirebaseVisitorDao();
        ButterKnife.bind(this);
    }
}
