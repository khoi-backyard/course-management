package io.khoi.course.command;

import io.khoi.course.Utils;
import io.khoi.course.model.Campus;
import io.khoi.course.model.Course;
import io.khoi.course.model.Unit;
import io.khoi.course.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.TableModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
/**
 * Responsible for unit-* commands.
 */
public class UnitCommand {
    @Autowired
    private UnitService unitService;

    public static LinkedHashMap<String, Object> tableHeaders = new LinkedHashMap<String, Object>(){
        {
            put("id", "Id");
            put("codeWithCourseCode", "Code");
            put("name", "Name");
            put("course", "Course");
            put("semester", "Semester");
            put("chiefExaminer", "Chief Examiner");
            put("lecturer", "Lecturer");
        }
    };

    @ShellMethod(value = "List all units", key = "unit-list")
    public String listUnits() {
        List<Unit> units = unitService.getUnits();
        TableModel model = new BeanListTableModel(units, tableHeaders);
        return Utils.makeTableBuilder(model).build().render(800);
    }

    @ShellMethod(value = "Get an unit by id", key = "unit-get")
    public String getUnit(Long id) {
        Optional<Unit> u = unitService.getUnit(id);

        if (!u.isPresent()) {
            return id + " not found";
        }

        List<Unit> units = Arrays.asList(new Unit[]{u.get()});
        TableModel model = new BeanListTableModel<>(units, tableHeaders);
        return Utils.makeTableBuilder(model).build().render(800);
    }

    @ShellMethod(value = "Create a new unit", key = "unit-create")
    public String createUnit(@ShellOption(help = "Examples: 1001") @Size(min = 4, max=4) String code,
                             @NotBlank String name,
                             @Min(1) @Max(3) int semester,
                             Long courseId) {
        Unit u;

        try {
            u = unitService.createUnit(code, name, semester, courseId);
        } catch (Exception e){
            return e.getLocalizedMessage();
        }

        return u + " created";
    }

    @ShellMethod(value = "Assign examiner to an unit", key = "unit-assign-examiner")
    public String assignExaminer(Long unitId, Long staffId) {
        try {
            unitService.assignChiefExaminer(unitId, staffId);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
        return "Done.";
    }

    @ShellMethod(value = "Assign examiner to an unit", key = "unit-assign-lecturer")
    public String assignLecturer(Long unitId, Long staffId) {
        try {
            unitService.assignLecturer(unitId, staffId);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
        return "Done.";
    }

    @ShellMethod(value = "Delete unit by id", key = "unit-delete")
    public String deleteUnit(Long id) {
        try {
            unitService.deleteUnit(id);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
        return id + " deleted.";
    }
}
