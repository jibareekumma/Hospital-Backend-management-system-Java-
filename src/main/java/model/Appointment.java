package model;

public class Appointment {

    private int id;
    private int patientId;
    private String patientName;
    private int doctorId;
    private String doctorName;
    private String appointmentDate;
    private String appointmentTime;
    private String reason;
    private String status;

    public Appointment(int id, int patientId, String patientName, int doctorId, String doctorName,
                        String appointmentDate, String appointmentTime, String reason, String status) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
        this.status = status;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public int getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getAppointmentTime() { return appointmentTime; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
}