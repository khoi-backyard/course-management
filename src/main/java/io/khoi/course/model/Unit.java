package io.khoi.course.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "units", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code", "course_id"})
})
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Length(min = 4, max = 4)
    private String code;

    @NotBlank
    private String name;

    @OneToOne()
    @JoinColumn(name = "chief_examiner_staff_id", referencedColumnName = "id", nullable = true)
    private Staff chiefExaminer;

    @OneToOne()
    @JoinColumn(name = "lecturer_staff_id", referencedColumnName = "id", nullable = true)
    private Staff lecturer;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Min(1)
    @Max(3)
    @NotNull
    private int semester;

    public Unit() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public String getCodeWithCourseCode() {
        return course.getCode() + code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Staff getChiefExaminer() {
        return chiefExaminer;
    }

    public void setChiefExaminer(Staff chiefExaminer) {
        this.chiefExaminer = chiefExaminer;
    }

    public Staff getLecturer() {
        return lecturer;
    }

    public void setLecturer(Staff lecturer) {
        this.lecturer = lecturer;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return course.getCode() + code + "-" + name;
    }
}
