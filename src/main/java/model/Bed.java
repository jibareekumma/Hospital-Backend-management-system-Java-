package model;

public class Bed {

    private int id;
    private String bedNumber;
    private String ward;
    private String status;

    public Bed(int id, String bedNumber, String ward, String status) {
        this.id = id;
        this.bedNumber = bedNumber;
        this.ward = ward;
        this.status = status;
    }

    public int getId() { return id; }
    public String getBedNumber() { return bedNumber; }
    public String getWard() { return ward; }
    public String getStatus() { return status; }
}