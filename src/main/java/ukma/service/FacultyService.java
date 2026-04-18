package ukma.service;

import lombok.extern.slf4j.Slf4j;
import ukma.domain.Faculty;
import ukma.domain.exception.FacultyNotFoundException;
import ukma.repository.Repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FacultyService {
    private final Repository<Faculty, Integer> facultyRepository;
    private final EmailRegistry emailRegistry;

    public FacultyService(Repository<Faculty, Integer> facultyRepository, EmailRegistry emailRegistry) {
        this.facultyRepository = facultyRepository;
        this.emailRegistry = emailRegistry;
    }

    public void addFaculty(Faculty faculty) {
        facultyRepository.store(faculty);
    }

    public void updateFaculty(Faculty faculty) {
        facultyRepository.store(faculty);
    }

    public void deleteFaculty(int id) {
        String emailToRemove = getFacultyById(id).getEmail();
        facultyRepository.deleteById(id);
        emailRegistry.removeEmail(emailToRemove);
        log.info("Faculty with ID {} was successfully removed", id);
    }

    public Faculty getFacultyById(int id) {
        return facultyRepository.getById(id)
                .orElseThrow(() -> new FacultyNotFoundException("Faculty with id " + id + " was not found"));
    }

    public void showFacultiesAlphabeticallySorted() {
        List<Faculty> faculties = facultyRepository.getAll();
        if (faculties.isEmpty()) {
            System.out.println("Repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Faculty Name", "Dean"));
        System.out.println("-".repeat(89));
        faculties.stream()
                .sorted(Comparator.comparing(Faculty::getName))
                .forEach(f -> System.out.println(f.toShortString()));
    }

    public Map<Integer, Faculty> getFaculties() {
        List<Faculty> facultyList = facultyRepository.getAll();
        Map<Integer, Faculty> facultyMap = new HashMap<>();
        for (Faculty f : facultyList) {
            facultyMap.put(f.getId(), f);
        }
        return facultyMap;
    }
}