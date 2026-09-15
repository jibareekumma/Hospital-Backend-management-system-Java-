package model;

public class Staff {

    private int id;
    private String firstName;
    private String lastName;
    private String role;
    private String department;
    private String phone;
    private String email;
    private String username;
    private String status;

    public Staff(int id, String firstName, String lastName, String role, String department,
                 String phone, String email, String username, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.department = department;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.status = status;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRole() { return role; }
    public String getDepartment() { return department; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getStatus() { return status; }

    public String getFullName() { return firstName + " " + lastName; }
}   