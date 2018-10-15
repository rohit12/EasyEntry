package entry.easyentry.otp;

import entry.easyentry.otp.APIResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OTPRetrofit {

    @GET("{api_key}/SMS/{phone_number}/AUTOGEN")
    Call<APIResponse> sendOTP(@Path("api_key") String key, @Path("phone_number") String phoneNumber);

    @GET("{api_key}/SMS/VERIFY/{session_id}/{otp_input}")
    Call<APIResponse> verifyOTP(@Path("api_key") String key, @Path("session_id") String session_id, @Path("otp_input") String otp);
}
