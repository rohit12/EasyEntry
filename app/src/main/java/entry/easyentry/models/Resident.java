package entry.easyentry.models;

public class Resident {

    private String name;
    private String phoneNumber;
    private String flatNumber;

    public Resident(String name, String phoneNumber, String flatNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.flatNumber = flatNumber;
    }

    public Resident(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }
}
