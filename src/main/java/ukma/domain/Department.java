package ukma.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Department implements ShortViewable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int id;
    private static int idCounter = 1;
    public static void setNextId(int id) {
        idCounter = id + 1;
    }

    private String name;
    private Faculty faculty;
    private Teacher head;
    private String location;

    public Department(String name, Faculty faculty, Teacher head, String location) {
        this.id = idCounter++;
        setName(name);
        setFaculty(faculty);
        setHead(head);
        setLocation(location);
    }

    public void setName(String n) {
        if (n == null || n.trim().isEmpty()) throw new IllegalArgumentException("input can't be empty");
        name = n;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setHead(Teacher head) {
        this.head = head;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) throw new IllegalArgumentException("input can't be empty");
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public String getLocation() {
        return location;
    }

    public Teacher getHead() {
        return head;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Department that = (Department) o;
        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "DEPARTMENT INFO\n"
                + "ID: " + id + "\n"
                + "Name: " + name + "\n"
                + "Location: " + location + "\n"
                + "Faculty: " + (faculty != null ? faculty.getName() : "None") + "\n"
                + "Head: " + (head != null ? head.getFullName() : "None");
    }

    @Override
    public String toShortString() {
        String extraInfo = "Head: " + (head != null ? head.getInitials() : "None");
        return String.format("| %-5d | %-50s | %-25s |", id, name, extraInfo);
    }
}
