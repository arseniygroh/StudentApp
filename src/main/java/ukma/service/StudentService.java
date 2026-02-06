package ukma.service;

import ukma.model.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    // Пошук за ПІБ
    public List<Student> findByName(String query) {
        List<Student> result = new ArrayList<>();

        // Нормалізуємо запит (маленькі літери)
        String lowerQuery = query.toLowerCase().trim();

        // Проходимо по кожному студенту в базі
        for (Student student : students) {
            if (student.getFullName().toLowerCase().contains(lowerQuery)) {
                result.add(student);
            }
        }
        return result;
    }

    // Пошук за курсом
    public List<Student> findByCourse(int year) {
        List<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (student.getStudyYear() == year) {
                result.add(student);
            }
        }
        return result;
    }

    // Пошук за групою
    public List<Student> findByGroup(String groupCode) {
        List<Student> result = new ArrayList<>();

        for (Student student : students) {
            if (student.getCourseCode().equalsIgnoreCase(groupCode.trim())) {
                result.add(student);
            }
        }
        return result;
    }

    public List<Student> getAll() {
        return new ArrayList<>(students);
    }
}