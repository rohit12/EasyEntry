package entry.easyentry.otp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SMSRetrofit {

    @GET(".")
    Call<APIResponse> sendSMS(@Query("module") String module, @Query("apikey") String key, @Query("to") String to,
                              @Query("from") String from, @Query("templatename") String template, @Query("var1") String var1,
                              @Query("var2") String var2);

}
