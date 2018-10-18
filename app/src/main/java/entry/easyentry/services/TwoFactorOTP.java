package entry.easyentry.services;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import entry.easyentry.otp.APIResponse;
import entry.easyentry.otp.APIUtils;
import entry.easyentry.otp.OTPRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;


public class TwoFactorOTP implements OTPService {

    private String API_KEY = "14d0b659-cbc7-11e8-a895-0200cd936042";
    private OTPRetrofit otpRetrofit;
    private String sessionID;
    private static final String TAG = "TwoFactorOTP";
    private Context context;
    private Intent intent;

    public TwoFactorOTP(Context context){
        otpRetrofit = APIUtils.getOTPService();
        this.context = context;
        intent = new Intent("otp-service");
    }

    @Override
    public void sendOTP(String phoneNumber) {
        Log.d(TAG, "sendOTP: Sending OTP");
        otpRetrofit.sendOTP(API_KEY, phoneNumber).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Successful API call. OTP sent");
                    sessionID = response.body().getDetails();
                    Log.d(TAG, "onResponse: sessionID: "+sessionID);

                    intent.putExtra("method","sendOtp");
                    intent.putExtra("otp-sessionID",sessionID);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
                else {
                    intent.putExtra("method","sendOtp");
                    intent.putExtra("otp-sessionID","none");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    Log.d(TAG, "onResponse: Successful API call. OTP not sent");
                }

            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: API Response not received. Service might be down");
            }
        });
    }

    @Override
    public void verifyOTP(String code, String sessionID) {
        Log.d(TAG, "verifyOTP: In verify OTP");
        otpRetrofit.verifyOTP(API_KEY, sessionID, code).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.isSuccessful()){
                    intent.putExtra("method","verifyOtp");
                    intent.putExtra("otp-verified",true);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    Log.d(TAG, "onResponse: Successful API call. Code verified");
                }
                else {
                    intent.putExtra("method","verifyOtp");
                    intent.putExtra("otp-verified", false);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    Log.d(TAG, "onResponse: Successful API call. Code could not be verified");
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: API call failed");
            }
        });
    }


}

