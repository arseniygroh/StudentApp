package ukma.service;

import lombok.extern.slf4j.Slf4j;
import ukma.domain.Department;
import ukma.domain.exception.DepartmentNotFoundException;
import ukma.repository.Repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DepartmentService {
    private final Repository<Department, Integer> departmentRepository;

    public DepartmentService(Repository<Department, Integer> departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public void addDepartment(Department department) {
        departmentRepository.store(department);
    }

    public void updateDepartment(Department department) {
        departmentRepository.store(department);
    }

    public Department getDepartmentById(int id) {
        return departmentRepository.getById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with id " + id + " was not found"));
    }

    public Map<Integer, Department> getDepartments() {
        List<Department> departmentList = departmentRepository.getAll();
        Map<Integer, Department> departmentMap = new HashMap<>();
        for (Department d : departmentList) {
            departmentMap.put(d.getId(), d);
        }
        return departmentMap;
    }


    public void showAllDepartments() {
        List<Department> departments = departmentRepository.getAll();
        if (departments.isEmpty()) {
            System.out.println("Department repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Department Name", "Head"));
        System.out.println("-".repeat(89));
        departments.stream()
                .sorted(Comparator.comparing(Department::getName))
                .forEach(dept -> System.out.println(dept.toShortString()));
    }

    public void deleteDepartment(int id) {
        departmentRepository.deleteById(id);
        log.info("Department with ID {} was successfully removed", id);
    }
}