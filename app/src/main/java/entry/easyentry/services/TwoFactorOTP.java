package entry.easyentry.services;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class TwoFactorOTP implements OTPService {

    @Override
    public boolean sendOTP(String phoneNumber) {


        return false;
    }

    @Override
    public boolean verifyOTP(String code) {
        return false;
    }


}

