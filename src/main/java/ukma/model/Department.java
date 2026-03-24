package ukma.model;

import ukma.model.utils.ShortViewable;

public class Department implements ShortViewable {
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
        if (head == null) throw new IllegalArgumentException("input can't be null");
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
        return String.format("| %-4d | %-30s | %-25s |", id, name, extraInfo);
    }
}
