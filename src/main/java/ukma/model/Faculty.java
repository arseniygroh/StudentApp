package ukma.model;

public class Faculty {
    private final int id;
    private static int idCounter = 1;
    private String name;
    private String shortName;
    private Teacher dean;
    private String email;
    private String phone;

    public Faculty(String name, String shortName, Teacher dean, String email, String phone) {
        this.id = idCounter++;
        setName(name);
        setShortName(shortName);
        setDean(dean);
        setEmail(email);
        setPhone(phone);
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.name = name;
    }

    public void setShortName(String shortName) {
        if (shortName == null || shortName.trim().isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.shortName = shortName;
    }

    public void setDean(Teacher dean) {
        this.dean = dean;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.email = email;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
    public String getShortName() {
        return shortName;
    }
    public Teacher getDean() {
        return dean;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", dean=" + (dean != null ? dean.getFirstName() + " " + dean.getLastName() + " " + dean.getFatherName() : "null") +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
