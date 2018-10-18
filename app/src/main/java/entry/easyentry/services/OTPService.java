package entry.easyentry.services;

public interface OTPService {

    void sendOTP(String phoneNumber);

    void verifyOTP(String code, String sessionID);
}
