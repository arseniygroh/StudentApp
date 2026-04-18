package ukma.service;

import lombok.extern.slf4j.Slf4j;
import ukma.domain.Department;
import ukma.domain.Faculty;
import ukma.domain.Teacher;
import ukma.domain.exception.TeacherNotFoundException;
import ukma.repository.Repository;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class TeacherService {
    private final Repository<Teacher, Long> teacherRepository;
    private final Repository<Faculty, Integer> facultyRepository;
    private final Repository<Department, Integer> departmentRepository;
    private final EmailRegistry emailRegistry;

    public TeacherService(Repository<Teacher, Long> teacherRepository,
                          Repository<Faculty, Integer> facultyRepository,
                          Repository<Department, Integer> departmentRepository,
                          EmailRegistry emailRegistry) {
        this.teacherRepository = teacherRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.emailRegistry = emailRegistry;
    }

    public void addTeacher(Teacher teacher) {
        teacherRepository.store(teacher);
    }

    public void updateTeacher(Teacher teacher) {
        teacherRepository.store(teacher);
    }

    public Teacher getTeacherById(long id) {
        return teacherRepository.getById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher with id " + id + " was not found"));
    }

    public List<Teacher> getTeachersByNameInfo(String query) {
        String lowerQuery = query.toLowerCase();
        return teacherRepository.search(teacher -> teacher.getFullName().toLowerCase().contains(lowerQuery));
    }

    public void showAllTeachersInFaculty(String facultyShortName) {
        List<Teacher> teachers = teacherRepository.getAll();
        if (teachers.isEmpty()) {
            System.out.println("Teacher repository is empty");
            return;
        }
        teachers.stream()
                .filter(teacher -> teacher.getDepartment() != null
                        && teacher.getDepartment().getFaculty() != null
                        && teacher.getDepartment().getFaculty().getShortName().equalsIgnoreCase(facultyShortName))
                .sorted(Comparator.comparing(Teacher::getFullName))
                .forEach(teacher -> {
                    System.out.println(teacher);
                    System.out.println("================================================");
                });
    }

    public void showDepartmentTeachersAlphabetically(Department department) {
        List<Teacher> teachers = teacherRepository.getAll();
        if (teachers.isEmpty()) {
            System.out.println("Teacher repository is empty");
            return;
        }

        teachers.stream()
                .filter(teacher -> department.equals(teacher.getDepartment()))
                .sorted(Comparator.comparing(Teacher::getFullName))
                .forEach(t -> {
                    System.out.println(t);
                    System.out.println("================================================");
                });
    }


    public void showAllTeachers() {
        List<Teacher> teachers = teacherRepository.getAll();
        if (teachers.isEmpty()) {
            System.out.println("Teachers repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Teacher Name", "Occupation"));
        System.out.println("-".repeat(89));
        teachers.stream()
                .sorted(Comparator.comparing(Teacher::getFullName))
                .forEach(teacher -> System.out.println(teacher.toShortString()));
    }

    public void deleteTeacher(long id) {
        String emailToRemove = getTeacherById(id).getEmail();
        teacherRepository.deleteById(id);
        emailRegistry.removeEmail(emailToRemove);
        log.info("Teacher with ID {} was successfully removed", id);
    }

    public List<Teacher> getAvailableTeachersForDean() {
        List<Long> busyTeacherIds = facultyRepository.getAll().stream()
                .map(faculty -> faculty.getDean())
                .filter(teacher -> teacher != null)
                .map(teacher -> teacher.getId())
                .toList();
        List<Teacher> availableTeachers = teacherRepository.getAll().stream()
                .filter(teacher -> !busyTeacherIds.contains(teacher.getId()))
                .toList();
        return availableTeachers;
    }

    public List<Teacher> getAvailableTeachersForHead() {
        List<Long> busyTeacherIds = departmentRepository.getAll().stream()
                .map(department -> department.getHead())
                .filter(teacher -> teacher != null)
                .map(teacher -> teacher.getId())
                .toList();

        List<Teacher> availableTeachers = teacherRepository.getAll().stream()
                .filter(teacher -> !busyTeacherIds.contains(teacher.getId()))
                .toList();
        return availableTeachers;
    }

    public List<Teacher> getAvailableTeacherForPosition() {
        List<Teacher> teachersForDean = getAvailableTeachersForDean();
        List<Teacher> teachersForHead = getAvailableTeachersForHead();

        List<Teacher> availableTeachers =  teachersForDean.stream()
                .filter(teacher -> teachersForHead.contains(teacher))
                .toList();


        return availableTeachers;
    }
}