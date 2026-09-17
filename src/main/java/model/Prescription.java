package model;

public class Prescription {

    private int id;
    private int patientId;
    private String patientName;
    private String doctorName;
    private String medication;
    private String dosage;
    private String status;
    private String datePrescribed;

    public Prescription(int id, int patientId, String patientName, String doctorName,
                         String medication, String dosage, String status, String datePrescribed) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.medication = medication;
        this.dosage = dosage;
        this.status = status;
        this.datePrescribed = datePrescribed;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getMedication() { return medication; }
    public String getDosage() { return dosage; }
    public String getStatus() { return status; }
    public String getDatePrescribed() { return datePrescribed; }
}
