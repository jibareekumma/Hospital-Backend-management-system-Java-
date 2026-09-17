package model;

public class LabTest {

    private int id;
    private int patientId;
    private String patientName;
    private String doctorName;
    private String testName;
    private String status;
    private String result;
    private String dateOrdered;
    private String dateCompleted;

    public LabTest(int id, int patientId, String patientName, String doctorName, String testName,
                   String status, String result, String dateOrdered, String dateCompleted) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.testName = testName;
        this.status = status;
        this.result = result;
        this.dateOrdered = dateOrdered;
        this.dateCompleted = dateCompleted;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getTestName() { return testName; }
    public String getStatus() { return status; }
    public String getResult() { return result; }
    public String getDateOrdered() { return dateOrdered; }
    public String getDateCompleted() { return dateCompleted; }
}
