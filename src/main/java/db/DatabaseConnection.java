package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:hospital.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        String users = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL" +
                ")";

        String patients = "CREATE TABLE IF NOT EXISTS patients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "date_of_birth TEXT," +
                "gender TEXT," +
                "phone TEXT," +
                "email TEXT," +
                "address TEXT," +
                "blood_group TEXT" +
                ")";

        String appointments = "CREATE TABLE IF NOT EXISTS appointments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patient_id INTEGER NOT NULL," +
                "doctor_name TEXT," +
                "appointment_date TEXT," +
                "status TEXT," +
                "FOREIGN KEY (patient_id) REFERENCES patients(id)" +
                ")";

        String prescriptions = "CREATE TABLE IF NOT EXISTS prescriptions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patient_id INTEGER NOT NULL," +
                "medication TEXT," +
                "dosage TEXT," +
                "date_prescribed TEXT," +
                "FOREIGN KEY (patient_id) REFERENCES patients(id)" +
                ")";

        String medicalHistory = "CREATE TABLE IF NOT EXISTS medical_history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patient_id INTEGER NOT NULL," +
                "diagnosis TEXT," +
                "treatment TEXT," +
                "record_date TEXT," +
                "FOREIGN KEY (patient_id) REFERENCES patients(id)" +
                ")";


        String staff = "CREATE TABLE IF NOT EXISTS staff (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "first_name TEXT NOT NULL," +
        "last_name TEXT NOT NULL," +
        "role TEXT NOT NULL," +
        "department TEXT," +
        "phone TEXT," +
        "email TEXT," +
        "username TEXT UNIQUE," +
        "password TEXT," +
        "status TEXT DEFAULT 'Active'" +
        ")";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(users);
            stmt.execute(patients);
            stmt.execute(appointments);
            stmt.execute(prescriptions);
            stmt.execute(medicalHistory);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}