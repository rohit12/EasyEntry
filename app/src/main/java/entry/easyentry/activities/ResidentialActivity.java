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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    //Flow for the activity:
    //1. Visitor enters his details on the screen.
    //2. Check if number exist and if it needs to be verified.
    //3. if number exists, no need to verify. Store details and redirect to home page.
    //4. if number does not exist, verify number. Store details and redirect to home page.

    private FirebaseVisitorDao visitorDao;
    private OTPService otpService;
    private String sessionID;
    private boolean isNumberVerified;
    private FirebaseUser currentUser;
    private static final String TAG = "ResidentialActivity";
    String society; // TODO fetch society string from current user's records

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
        visitorDao.checkIfVisitorExists(phoneNumber);
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

        Visitor visitor = new Visitor(name, flatNumber, timeIn, date, phoneNumber, society);
        visitorDao.writeRecord(visitor);
        Toast.makeText(this,"Visitor saved", Toast.LENGTH_LONG).show();
    }

    private BroadcastReceiver phoneNumberVerifier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: phone number verifier intent received");
            boolean doesNumberExist = intent.getBooleanExtra("phoneNumber", false);
            if (doesNumberExist){
                Log.d(TAG, "onReceive: Number exists");
                Toast.makeText(ResidentialActivity.this,"This number exists in database. Not verifying",Toast.LENGTH_LONG).show();
                storeVisitor();
            }
            else {
                otpService.sendOTP(editTextPhoneNumber.getText().toString());
                Toast.makeText(ResidentialActivity.this, "Sending OTP. Number not found in database.",Toast.LENGTH_LONG).show();
            }
        }
    };

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
        visitorDao = new FirebaseVisitorDao(this);
        otpService = new TwoFactorOTP(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        society = "None";
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("otp-service"));
        LocalBroadcastManager.getInstance(this).registerReceiver(phoneNumberVerifier, new IntentFilter("visitor-exists"));
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(phoneNumberVerifier);
        super.onDestroy();
    }
}
