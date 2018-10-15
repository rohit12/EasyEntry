package entry.easyentry.services;

public interface OTPService {

    boolean sendOTP(String phoneNumber);

    boolean verifyOTP(String code);
}
