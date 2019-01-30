package entry.easyentry.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entry.easyentry.R;

public class NewVisitorRegister extends AppCompatActivity {

    private static final String TAG = "NewVisitorRegister";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @BindView(R.id.btnVerifyPhoneNumber)
    Button verifyPhone;

    @BindView(R.id.verificationOTP)
    EditText confirmationOTP;

    @BindView(R.id.imagePreview)
    ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visitor_register);
        ButterKnife.bind(this);
        Log.d(TAG, "New Visitor Register flow started...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nextButton) {
            onNextMenuButtonClick();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnVerifyPhoneNumber)
    void onVerifyPhoneClick() {
        Log.d(TAG, "Verify Phone Clicked, showing confirmation OTP.");
        this.confirmationOTP.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.btnResidentialTakePhoto)
    void onTakePicture() {
        Log.d(TAG, "Take picture clicked. Sending control to OS");
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Result received from requested intent.");

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //TODO Crop and set the image correctly such that it take the complete horizontal space available
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagePreview.setImageBitmap(imageBitmap);
        }
    }

    void onNextMenuButtonClick() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
