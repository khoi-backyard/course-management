package io.khoi.course.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @Length(min = 4, max = 4)
    private String code;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Campus campus;

    @Enumerated(EnumType.STRING)
    @NotNull
    private CourseType courseType;

    @NotBlank
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "director_staff_id", referencedColumnName = "id", nullable = true)
    private Staff director;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deputy_director_staff_id", referencedColumnName = "id", nullable = true)
    private Staff deputy;

    @OneToMany(mappedBy = "course", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Unit> units = new HashSet<>();

    public Course() {

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

    public void setCode(String code) {
        this.code = code;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Staff getDirector() {
        return director;
    }

    public void setDirector(Staff director) {
        this.director = director;
    }

    public Staff getDeputy() {
        return deputy;
    }

    public void setDeputy(Staff deputy) {
        this.deputy = deputy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void removeUnit(Unit u) {
        units.remove(u);
    }

    public void addUnit(Unit u) {
        units.add(u);
    }

    public Set<Unit> getUnits() {
        return units;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    @Override
    public String toString() {
        return id + "-" + name;
    }
}
