package model;

public class Admission {

    private int id;
    private int patientId;
    private String patientName;
    private int bedId;
    private String bedNumber;
    private String ward;
    private String admissionDate;
    private String dischargeDate;
    private String status;

    public Admission(int id, int patientId, String patientName, int bedId, String bedNumber, String ward,
                      String admissionDate, String dischargeDate, String status) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.bedId = bedId;
        this.bedNumber = bedNumber;
        this.ward = ward;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.status = status;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public int getBedId() { return bedId; }
    public String getBedNumber() { return bedNumber; }
    public String getWard() { return ward; }
    public String getAdmissionDate() { return admissionDate; }
    public String getDischargeDate() { return dischargeDate; }
    public String getStatus() { return status; }
}