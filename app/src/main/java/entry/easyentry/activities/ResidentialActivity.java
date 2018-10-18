package entry.easyentry.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entry.easyentry.R;
import entry.easyentry.dao.Dao;
import entry.easyentry.dao.FirebaseVisitorDao;
import entry.easyentry.models.Visitor;
import entry.easyentry.services.OTPService;
import entry.easyentry.services.TwoFactorOTP;
import entry.easyentry.utils.Utils;

public class ResidentialActivity extends AppCompatActivity {

    private Dao visitorDao;
    private OTPService otpService;
    private String sessionID;
    private boolean isNumberVerified;
    private static final String TAG = "ResidentialActivity";

    @BindView(R.id.editTextResidentialName)
    EditText editTextName;

    @BindView(R.id.editTextResidentialFlatNumber)
    EditText editTextFlatNumber;

    @BindView(R.id.editTextResidentialOTP)
    EditText editTextOTP;

    @BindView(R.id.editTextResidentialPhoneNumber)
    EditText editTextPhoneNumber;

    @BindView(R.id.btnResidentialSubmit)
    Button btnSubmit;

    @BindView(R.id.btnResidentialVerifyPhoneNumber)
    Button btnVerifyNumber;

    @OnClick(R.id.btnResidentialSubmit)
    void submit(){
        String phoneNumber = editTextPhoneNumber.getText().toString();
        otpService.sendOTP(phoneNumber);
    }

    @OnClick(R.id.btnResidentialVerifyPhoneNumber)
    void verify(){
        String code = editTextOTP.getText().toString();
        otpService.verifyOTP(code, sessionID);

    }

    void storeVisitor(){


        String name = editTextName.getText().toString();
        String date = Utils.getCurrentDate();
        String flatNumber = editTextFlatNumber.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String timeIn = Utils.getCurrentTime();

        Visitor visitor = new Visitor(name, flatNumber, timeIn, date, phoneNumber);
        visitorDao.write(visitor);
        Toast.makeText(this,"Visitor saved", Toast.LENGTH_LONG).show();
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String method = intent.getStringExtra("method");
            if(method.equals("sendOtp")){
                sessionID = intent.getStringExtra("otp-sessionID");
            }
            else if(method.equals("verifyOtp")){
                isNumberVerified = intent.getBooleanExtra("otp-verified",false);
                if (isNumberVerified){
                    storeVisitor();
                }
                else {
                    Toast.makeText(ResidentialActivity.this, "Number Verification failed",Toast.LENGTH_LONG);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residential);
        visitorDao = new FirebaseVisitorDao();
        otpService = new TwoFactorOTP(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("otp-service"));
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onDestroy();
    }
}
