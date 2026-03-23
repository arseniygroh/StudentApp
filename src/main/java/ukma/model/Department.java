package ukma.model;

import java.util.Objects;

public class Department {
    private final int id;
    private static int idCounter = 1;
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
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", faculty=" + (faculty != null ? faculty.getName() : "None") +
                ", head=" + (head != null ? head.getFullName() : "None") +
                ", location='" + location + '\'' +
                '}';
    }
}
