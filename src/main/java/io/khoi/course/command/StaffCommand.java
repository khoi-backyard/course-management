package io.khoi.course.command;

import io.khoi.course.Utils;
import io.khoi.course.model.Staff;
import io.khoi.course.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.TableModel;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@ShellComponent
/**
 * Responsible for staff-* commands.
 */
public class StaffCommand {
    @Autowired
    private StaffService staffService;

    private LinkedHashMap<String, Object> staffTableHeaders = new LinkedHashMap<String, Object>(){
        {
            put("id", "Id");
            put("staffId", "Staff Id");
            put("fullName", "Full Name");
            put("address", "Address");
        }
    };

    @ShellMethod(value = "List all staffs", key = "staff-list")
    public String listStaffs() {
        List<Staff> staffs = staffService.getStaffs();
        TableModel model = new BeanListTableModel<>(staffs, staffTableHeaders);
        return Utils.makeTableBuilder(model).build().render(800);
    }

    @ShellMethod(value = "Get a staff by id", key = "staff-get")
    public String getStaff(Long staffId) {
        Optional<Staff> s = staffService.getStaff(staffId);

        if (!s.isPresent()) {
            return staffId + " not found";
        }

        List<Staff> staffs = Arrays.asList(new Staff[]{s.get()});
        TableModel model = new BeanListTableModel<>(staffs, staffTableHeaders);
        return Utils.makeTableBuilder(model).build().render(800);
    }
}
