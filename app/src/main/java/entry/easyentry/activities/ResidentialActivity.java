package entry.easyentry.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import entry.easyentry.dao.FirebaseVisitorDao;
import entry.easyentry.models.Visitor;
import entry.easyentry.services.OTPService;
import entry.easyentry.services.TwoFactorOTP;
import entry.easyentry.utils.Utils;

public class ResidentialActivity extends AppCompatActivity {

    private FirebaseVisitorDao visitorDao;
    private OTPService otpService;
    private String sessionID;
    private FirebaseUser currentUser;
    private static final String TAG = "ResidentialActivity";
    private String society; // TODO fetch society string from current user's records
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    private StorageReference storageReference;
    private Uri photoURI;

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

    @BindView(R.id.imageViewResidential)
    ImageView imageView;

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
        visitorDao.checkIfVisitorExists(phoneNumber);
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
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            imageView.setImageBitmap(imageBitmap);
//            storeImageToFirebaseCloud();
//            galleryAddPic();
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

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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
                boolean isNumberVerified = intent.getBooleanExtra("otp-verified", false);
                if (isNumberVerified){
                    storeVisitor();
                }
                else {
                    Toast.makeText(ResidentialActivity.this, "Number Verification failed",Toast.LENGTH_LONG).show();
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
        storageReference = FirebaseStorage.getInstance().getReference();
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
