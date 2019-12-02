package io.khoi.course.service;

import io.khoi.course.Utils;
import io.khoi.course.model.Course;
import io.khoi.course.model.Staff;
import io.khoi.course.model.Unit;
import io.khoi.course.repository.CourseRepository;
import io.khoi.course.repository.StaffRepository;
import io.khoi.course.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for unit-* commands.
 */
@Service
public class UnitService {
    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Get All units
     * @return a list of Unti
     */
    public List<Unit> getUnits() {
        return unitRepository.findAll();
    }

    /**
     * Get an unit using id
     * @param id
     * @return an optional Unit
     */
    public Optional<Unit> getUnit(Long id) {
        return unitRepository.findById(id);
    }

    /**
     * Delete unit using an id
     * @param id
     * @throws Exception
     */
    public void deleteUnit(Long id) throws Exception {
        if (!unitRepository.findById(id).isPresent()) {
            throw new Exception(id + " not exists");
        }
        unitRepository.deleteById(id);
    }

    /**
     * Check whether unit exist using unit code and course id
     * @param code the unit code
     * @param courseId the course id
     * @return a boolean
     */
    public boolean unitExist(String code, Long courseId) {
        return unitRepository.findByCodeAndCourseId(code, courseId) != null;
    }

    /**
     * Assign a staff as Chief examiner to an Unit
     * @param unitId the unit id
     * @param staffId the staff id
     * @throws Exception
     */
    public void assignChiefExaminer(Long unitId, Long staffId) throws Exception {
        Unit u = unitRepository.findById(unitId).orElseThrow(() -> new Exception("unit " + unitId + " doesn't exist"));
        Staff s = staffRepository.findById(staffId).orElseThrow(() -> new Exception("staff " + staffId + " doesn't exist"));

        System.out.println("Assigning " + s + " as chief examier of " + u);
        u.setChiefExaminer(s);
        unitRepository.save(u);
    }

    /**
     * Assign a staff as lecturer for an unit
     * @param unitId the unit id
     * @param staffId the staff id
     * @throws Exception
     */
    public void assignLecturer(Long unitId, Long staffId) throws Exception {
        Unit u = unitRepository.findById(unitId).orElseThrow(() -> new Exception("unit " + unitId + " doesn't exist"));
        Staff s = staffRepository.findById(staffId).orElseThrow(() -> new Exception("staff " + staffId + " doesn't exist"));

        System.out.println("Assigning " + s + " as lecturer of " + u);
        u.setLecturer(s);
        unitRepository.save(u);
    }

    /**
     * Create a new unit
     * @param code the code of the unit
     * @param name the name of the unit
     * @param semester semester 1 > 3
     * @param courseId the course id where the unit belongs to
     * @return created Unit
     * @throws Exception
     */
    public Unit createUnit(String code, String name, int semester, Long courseId) throws Exception {
        if (unitExist(code, courseId)) {
            throw new Exception(code + " already exists for course id " + courseId);
        }

        Course c = courseRepository.findById(courseId).orElseThrow(() -> new Exception("course " + courseId + " doesn't exist"));

        if (!Utils.validUnitCodeForCourseType(code, c.getCourseType())) {
            throw new Exception("Invalid unit year");
        }

        if (semester < 1 || semester > 3) {
            throw new Exception("semester must be in 1-3");
        }

        Unit u = new Unit();
        u.setName(name);
        u.setCourse(c);
        u.setCode(code);
        u.setSemester(semester);
        return unitRepository.save(u);
    }
}
