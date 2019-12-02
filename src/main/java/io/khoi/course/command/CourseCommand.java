package io.khoi.course.command;

import io.khoi.course.Utils;
import io.khoi.course.model.Campus;
import io.khoi.course.model.Course;
import io.khoi.course.model.CourseType;
import io.khoi.course.model.Unit;
import io.khoi.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.TableModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@ShellComponent
/**
 * Responsible for course-* commands.
 */
public class CourseCommand {
    @Autowired
    private CourseService courseService;

    private LinkedHashMap<String, Object> tableHeaders = new LinkedHashMap<String, Object>(){
        {
            put("id", "Id");
            put("code", "Code");
            put("name", "Name");
            put("courseType", "Type");
            put("campus", "Campus");
            put("director", "Director");
            put("deputy", "Deputy");
        }
    };

    @ShellMethod(value = "List all courses", key = "course-list")
    public String listCourses() {
        List<Course> courses = courseService.findAll();
        TableModel model = new BeanListTableModel<>(courses, tableHeaders);

        return Utils.makeTableBuilder(model).build().render(800);
    }

    @ShellMethod(value = "Get a course by id", key = "course-get")
    public String getCourse(Long id) {
        Optional<Course> c = courseService.getCourse(id);

        if (!c.isPresent()) {
            return id + " not found";
        }

        List<Course> courses = Arrays.asList(new Course[]{c.get()});
        TableModel model = new BeanListTableModel<>(courses, tableHeaders);
        return Utils.makeTableBuilder(model).build().render(800);
    }

    @ShellMethod(value = "Get units using a course id", key = "course-get-units")
    public String getUnits(Long courseId) {
        Optional<Course> c = courseService.getCourse(courseId);

        if (!c.isPresent()) {
            return courseId + " not found";
        }

        Course course = c.get();

        System.out.println("List units for " + course);

        TableModel model = new BeanListTableModel(course.getUnits(), UnitCommand.tableHeaders);
        return Utils.makeTableBuilder(model).build().render(800);
    }

    @ShellMethod(value = "Create a new course", key = "course-create")
    public String createCourse(@Size(min = 4, max = 4) String code,
                               @NotBlank String name,
                               @NotBlank String courseType,
                               @NotBlank String campus) {

        CourseType courseTypeObject;
        try {
            courseTypeObject = CourseType.valueOf(courseType);
        } catch (Exception e) {
            return "Invalid course type value. Correct values are: \n" + Arrays.stream(CourseType.values()).map(cp -> cp.toString() + "\n").collect(Collectors.joining());
        }

        Campus campusObject;
        try {
            campusObject = Campus.valueOf(campus);
        } catch (Exception e) {
            return "Invalid campus value. Correct values are: \n" + Arrays.stream(Campus.values()).map(cp -> cp.toString() + "\n").collect(Collectors.joining());
        }

        Course c;
        try {
            c = courseService.createCourse(code, name, courseTypeObject, campusObject);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }

        return c + " created";
    }

    @ShellMethod(value = "Assign director to a course", key = "course-assign-director")
    public String assignDirector(Long courseId, Long staffId) {
        try {
            courseService.assignDirector(courseId, staffId);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
        return "Done.";
    }

    @ShellMethod(value = "Assign deputy to a course", key = "course-assign-deputy")
    public String assignDeputy(Long courseId, Long staffId) {
        try {
            courseService.assignDeputyDirector(courseId, staffId);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
        return "Done.";
    }

    @ShellMethod(value = "Delete course by id", key = "course-delete")
    public String deleteCourse(Long id) {
        try {
            courseService.deleteCourse(id);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
        return id + " deleted.";
    }
}
