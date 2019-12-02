package io.khoi.course;

import io.khoi.course.model.CourseType;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

public class Utils {
    /**
     * Make TableBuilder for displaying Table in the shell
     * @param tableModel
     * @return TableBuilder
     */
    public static TableBuilder makeTableBuilder(TableModel tableModel) {
        TableBuilder tableBuilder = new TableBuilder(tableModel);
        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        return tableBuilder;
    }

    /**
     * Validate the unit code based on CourseType
     * @param unitCode
     * @param courseType
     * @return
     */
    public static boolean validUnitCodeForCourseType(String unitCode, CourseType courseType) {
        int unitYear = Integer.parseInt(unitCode.substring(0, 1));

        switch (courseType){
            case UG:
                return unitYear > 0 && unitYear <= 3;
            case PG:
                return unitYear > 3 && unitYear <= 5;
            default:
                return false;
        }
    }
}
