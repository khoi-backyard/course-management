package io.khoi.course.service;

import io.khoi.course.model.*;
import io.khoi.course.repository.CourseRepository;
import io.khoi.course.repository.StaffRepository;
import io.khoi.course.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Business logic for course-* commands.
 */
@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UnitRepository unitRepository;

    /**
     * Get all units belong to a course
     * @param courseId
     * @return units belong to the course
     */
    public List<Unit> findAllUnits(Long courseId) {
        return unitRepository.findByCourseId(courseId);
    }

    /**
     * Get all courses
     * @return list of courses
     */
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    /**
     * Check if the course exist with a given code
     * @param code
     * @return a boolean
     */
    public boolean courseExist(String code) {
        return (courseRepository.findByCode(code) != null);
    }

    /**
     * Get a course using id
     * @param id
     * @return an Optional course
     */
    public Optional<Course> getCourse(Long id) {
        return courseRepository.findById(id);
    }

    /**
     *
     * @param code The code of the course
     * @param name The name of the course
     * @param courseType the type of the course.
     * @param campus The campus enum
     * @return The course created
     * @throws Exception if any error occurs
     */
    public Course createCourse(String code, String name, CourseType courseType, Campus campus) throws Exception {
        if (courseExist(code)) {
            throw new Exception("Err: Code exists. Try different one.");
        }

        Course c = new Course();

        c.setCampus(campus);
        c.setCourseType(courseType);
        c.setCode(code);
        c.setName(name);
        return courseRepository.save(c);
    }

    /**
     * Assign director to a course. It has to be different from deputy if there is one.
     * @param courseId the course id
     * @param staffId the staff id
     * @throws Exception if any
     */
    public void assignDirector(Long courseId, Long staffId) throws Exception {
        Course c = courseRepository.findById(courseId).orElseThrow(() -> new Exception("course " + courseId + " doesn't exist"));
        Staff s = staffRepository.findById(staffId).orElseThrow(() -> new Exception("staff " + staffId + " doesn't exist"));

        System.out.println("Assigning " + s + " as director of " + c);
        if (c.getDeputy() != null && c.getDeputy().getId() == staffId) {
            throw new Exception("director has to be different than deputy.");
        }

        c.setDirector(s);
        courseRepository.save(c);
    }

    /**
     * Assign deputy director to a course. Has to be different from director if any
     * @param courseId the course id
     * @param staffId the staff id
     * @throws Exception if any
     */
    public void assignDeputyDirector(Long courseId, Long staffId) throws Exception {
        Course c = courseRepository.findById(courseId).orElseThrow(() -> new Exception("course " + courseId + " doesn't exist"));
        Staff s = staffRepository.findById(staffId).orElseThrow(() -> new Exception("staff " + staffId + " doesn't exist"));

        System.out.println("Assigning " + s + " as director of " + c);
        if (c.getDirector() != null && c.getDirector().getId() == staffId) {
            throw new Exception("deputy has to be different than director.");
        }

        c.setDeputy(s);
        courseRepository.save(c);
    }

    /**
     * Delete a course using its id
     * @param id
     * @throws Exception if any
     */
    public void deleteCourse(Long id) throws Exception {
        if (!courseRepository.findById(id).isPresent()) {
            throw new Exception(id + " not exists");
        }
        courseRepository.deleteById(id);
    }
}
