package model;

public class ClinicalRecord {

    private int id;
    private int patientId;
    private String patientName;
    private String doctorName;
    private String diagnosis;
    private String treatment;
    private String notes;
    private String recordDate;

    public ClinicalRecord(int id, int patientId, String patientName, String doctorName,
                           String diagnosis, String treatment, String notes, String recordDate) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.recordDate = recordDate;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }
    public String getNotes() { return notes; }
    public String getRecordDate() { return recordDate; }
}