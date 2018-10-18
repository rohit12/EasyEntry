package entry.easyentry.otp;

import entry.easyentry.otp.OTPRetrofit;

public class APIUtils {

    public static final String BASE_URL = "https://2factor.in/API/V1/";

    public static OTPRetrofit getOTPService(){
        return RetrofitClient.getClient(BASE_URL).create(OTPRetrofit.class);
    }

}
