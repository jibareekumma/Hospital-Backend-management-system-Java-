package model;

public class Patient {

    private int id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String bloodGroup;

    public Patient(int id, String firstName, String lastName, String dateOfBirth, String gender,
                   String phone, String email, String address, String bloodGroup) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.bloodGroup = bloodGroup;
    }

    public Patient(String firstName, String lastName, String dateOfBirth, String gender,
                   String phone, String email, String address, String bloodGroup) {
        this(0, firstName, lastName, dateOfBirth, gender, phone, email, address, bloodGroup);
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getBloodGroup() { return bloodGroup; }

    public String getFullName() { return firstName + " " + lastName; }
}