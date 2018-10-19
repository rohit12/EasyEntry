package entry.easyentry.models;

public class Visitor {

    private String name;
    private String flatNumber;
    private String timeIn;
    private String date;
    private String phoneNumber;
    private String society;

    public Visitor(String name, String flatNumber, String timeIn, String date, String phoneNumber, String society) {
        this.name = name;
        this.flatNumber = flatNumber;
        this.timeIn = timeIn;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.society = society;
    }

    public Visitor() {
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
