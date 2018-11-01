package entry.easyentry.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entry.easyentry.R;
import entry.easyentry.dao.FirebaseResidentDao;
import entry.easyentry.dao.FirebaseVisitorDao;
import entry.easyentry.models.Visitor;
import entry.easyentry.services.OTPService;
import entry.easyentry.services.SMSService;
import entry.easyentry.services.TwoFactorOTP;
import entry.easyentry.services.TwoFactorSMS;
import entry.easyentry.utils.Utils;

public class ResidentialActivity extends AppCompatActivity {

    private FirebaseVisitorDao visitorDao;
    private FirebaseResidentDao residentDao;
    private OTPService otpService;
    private SMSService smsService;
    private String sessionID;
    private FirebaseUser currentUser;
    private static final String TAG = "ResidentialActivity";
    private String society; // TODO fetch society string from current user's records. Store in shared preferences
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    private StorageReference storageReference;
    private Uri photoURI;
    private boolean isNumberVerified;

    private String phoneNumber;
    private String name;
    private SharedPreferences settings;

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

    @BindView(R.id.btnResidentialTakePhoto)
    Button btnTakePhoto;

    @BindView(R.id.btnResidentialEntry)
    Button btnEntry;

    @BindView(R.id.imageViewResidential)
    ImageView imageView;

    @OnClick(R.id.btnResidentialEntry)
    void easyEntry(){
        if (isNumberVerified){
            if (!(Utils.isEditTextEmpty(editTextName) || Utils.isEditTextEmpty(editTextFlatNumber))){
                if (imageView.getDrawable()!=null) {
                    residentDao.getResidentPhoneNumber(editTextFlatNumber.getText().toString(),society);
                    storeVisitor();
//                    smsService.sendSMS(phoneNumber, "Abhishek", editTextName.getText().toString());
                }
                else{
                    Toast.makeText(this,"Photo not taken. Storing data without photo",Toast.LENGTH_SHORT).show();
                    storeVisitor();

                }

            }

            else{
                Toast.makeText(this, "One of the fields has not been populated", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Number isn't verified. Verify number, try again.", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.btnResidentialTakePhoto)
    void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            // Create the File where the photo should go
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }
            catch (IOException ex){
                Log.d(TAG, "dispatchTakePictureIntent: IO exception occurred");
                Toast.makeText(this, "IOException occurred", Toast.LENGTH_LONG).show();
            }

            // Continue only if the File was successfully created
            if(photoFile!=null){
                photoURI = FileProvider.getUriForFile(this, "entry.easyentry",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }

    @OnClick(R.id.btnResidentialSubmit)
    void submit(){
        String phoneNumber = editTextPhoneNumber.getText().toString();
        if (phoneNumber.length()!=10) {
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
        }
        else {
            visitorDao.checkIfVisitorExists(phoneNumber);
        }
    }

    @OnClick(R.id.btnResidentialVerifyPhoneNumber)
    void verify(){
        String code = editTextOTP.getText().toString();
        otpService.verifyOTP(code, sessionID);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()){
                Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(bmp);
            }
        }
    }

    private String storeImageToFirebaseCloud(){
        String imageName = "visitors/" + society +"/" + currentPhotoPath.substring(currentPhotoPath.lastIndexOf("/")+1);
        StorageReference imageRef = storageReference.child(imageName);
        imageRef.putFile(photoURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ResidentialActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getMessage());
                        Toast.makeText(ResidentialActivity.this, "File not uploaded. Failure to upload", Toast.LENGTH_SHORT).show();
                    }
                });
        return imageName;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void storeVisitor(){

        String name = editTextName.getText().toString();
        String date = Utils.getCurrentDate();
        String flatNumber = editTextFlatNumber.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String timeIn = Utils.getCurrentTime();
        String photoLocation = storeImageToFirebaseCloud();

        Visitor visitor = new Visitor(name, flatNumber, timeIn, date, phoneNumber, society, photoLocation);
        visitorDao.writeRecord(visitor);
        Toast.makeText(this,"Visitor saved", Toast.LENGTH_LONG).show();
    }


    /*
    The submit button asks the database if a visitor with a given phone number exists. If it does, then
    it sends out a LocalBroadcastMessage that this receiver picks up. This receiver is intended to
    inform this activity whether a given number exists in the database. If the number exists, the visitor
    is stored otherwise it calls the function to send out the OTP.
     */
    private BroadcastReceiver userInformationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: phone number verifier intent received");
            boolean doesNumberExist = intent.getBooleanExtra("phoneNumber", false);
            Visitor v = (Visitor) intent.getExtras().getSerializable("visitor");
            if (doesNumberExist){
                Log.d(TAG, "onReceive: Number exists");
                editTextName.setText(v.getName());
                isNumberVerified = true;
            }
            else {
                editTextOTP.setVisibility(View.VISIBLE);
                btnVerifyNumber.setVisibility(View.VISIBLE);
                otpService.sendOTP(editTextPhoneNumber.getText().toString());
                Toast.makeText(ResidentialActivity.this, "Sending OTP. Number not found in database.",Toast.LENGTH_SHORT).show();
            }
        }
    };

    private BroadcastReceiver otpMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String method = intent.getStringExtra("method");
            if(method.equals("sendOtp")){
                sessionID = intent.getStringExtra("otp-sessionID");
            }
            else if(method.equals("verifyOtp")){
                isNumberVerified = intent.getBooleanExtra("otp-verified", false);
                if(!isNumberVerified)
                    Toast.makeText(ResidentialActivity.this, "Number Verification failed",Toast.LENGTH_SHORT).show();

            }
        }
    };

    private BroadcastReceiver residentInformationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("name") && intent.hasExtra("phoneNumber")){
                phoneNumber = intent.getStringExtra("phoneNumber");
                name = intent.getStringExtra("name");
                smsService.sendSMS(phoneNumber, name, editTextName.getText().toString());
            }
            else{
                Toast.makeText(ResidentialActivity.this, "No information about flat found in database. SMS not sent", Toast.LENGTH_SHORT).show();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residential);
        visitorDao = new FirebaseVisitorDao(this);
        residentDao = new FirebaseResidentDao(this);
        otpService = new TwoFactorOTP(this);
        smsService = new TwoFactorSMS(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        LocalBroadcastManager.getInstance(this).registerReceiver(otpMessageReceiver, new IntentFilter("otp-service"));
        LocalBroadcastManager.getInstance(this).registerReceiver(userInformationReceiver, new IntentFilter("visitor-exists"));
        LocalBroadcastManager.getInstance(this).registerReceiver(residentInformationReceiver, new IntentFilter("send-resident-details"));
        ButterKnife.bind(this);
        settings = this.getSharedPreferences("entry.easyentry.preferences",Context.MODE_PRIVATE);
        society = settings.getString("SocietyName","missing");
        editTextPhoneNumber.requestFocus(); //puts focus on the phone number
        editTextOTP.setVisibility(View.GONE);
        btnVerifyNumber.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(otpMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userInformationReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(residentInformationReceiver);
        super.onDestroy();
    }
}
