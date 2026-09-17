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
            addColumnIfMissing(stmt, "appointments", "doctor_id", "INTEGER");
            addColumnIfMissing(stmt, "appointments", "appointment_time", "TEXT");
            addColumnIfMissing(stmt, "appointments", "reason", "TEXT");
            addColumnIfMissing(stmt, "medical_history", "doctor_name", "TEXT");
            addColumnIfMissing(stmt, "medical_history", "notes", "TEXT");
            addColumnIfMissing(stmt, "prescriptions", "doctor_name", "TEXT");
            addColumnIfMissing(stmt, "prescriptions", "status", "TEXT DEFAULT 'Pending'");
            stmt.execute(prescriptions);
            stmt.execute(medicalHistory);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addColumnIfMissing(Statement stmt, String table, String column, String definition) {
    try {
        stmt.execute("ALTER TABLE " + table + " ADD COLUMN " + column + " " + definition);
    } catch (SQLException ignored) {
    }
}


    String beds = "CREATE TABLE IF NOT EXISTS beds (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "bed_number TEXT NOT NULL," +
        "ward TEXT NOT NULL," +
        "status TEXT DEFAULT 'Available'" +
        ")";

String admissions = "CREATE TABLE IF NOT EXISTS admissions (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "patient_id INTEGER NOT NULL," +
        "bed_id INTEGER NOT NULL," +
        "admission_date TEXT," +
        "discharge_date TEXT," +
        "status TEXT DEFAULT 'Admitted'," +
        "FOREIGN KEY (patient_id) REFERENCES patients(id)," +
        "FOREIGN KEY (bed_id) REFERENCES beds(id)" +
        ")";


        String labTests = "CREATE TABLE IF NOT EXISTS lab_tests (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patient_id INTEGER NOT NULL," +
                "doctor_name TEXT," +
                "test_name TEXT NOT NULL," +
                "status TEXT DEFAULT 'Pending'," +
                "result TEXT," +
                "date_ordered TEXT," +
                "date_completed TEXT," +
                "FOREIGN KEY (patient_id) REFERENCES patients(id)" +
                ")";

        String inventory = "CREATE TABLE IF NOT EXISTS inventory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "drug_name TEXT UNIQUE NOT NULL," +
                "quantity INTEGER DEFAULT 0," +
                "reorder_level INTEGER DEFAULT 10" +
                ")";
}
