package entry.easyentry.services;

import android.content.Context;
import android.widget.Toast;

import entry.easyentry.otp.APIResponse;
import entry.easyentry.otp.APIUtils;
import entry.easyentry.otp.SMSRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwoFactorSMS implements SMSService {

    private String API_KEY = "14d0b659-cbc7-11e8-a895-0200cd936042";
    private String module = "TRANS_SMS";
    private String from = "EZNTRY";
    private String templateName = "VisitorEntry";
    private SMSRetrofit smsRetrofit;
    private Context context;

    public TwoFactorSMS(Context context){
        smsRetrofit = APIUtils.getSMSService();
        this.context = context;
    }

    @Override
    public void sendSMS(String phoneNumber, String var1, String var2) {
        smsRetrofit.sendSMS(module, API_KEY, phoneNumber, from, templateName, var1, var2).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"SMS not sent. Response unsucessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(context, "SMS not sent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
