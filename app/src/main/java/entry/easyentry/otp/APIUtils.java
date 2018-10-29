package entry.easyentry.otp;

import entry.easyentry.otp.OTPRetrofit;

public class APIUtils {

    public static final String BASE_URL = "https://2factor.in/API/V1/";
    public static final String SMS_URL = "https://2factor.in/API/R1/";

    public static OTPRetrofit getOTPService(){
        return RetrofitClient.getClient(BASE_URL).create(OTPRetrofit.class);
    }

    public static SMSRetrofit getSMSService(){
        return RetrofitClient.getSMSClient(SMS_URL).create(SMSRetrofit.class);
    }
}
