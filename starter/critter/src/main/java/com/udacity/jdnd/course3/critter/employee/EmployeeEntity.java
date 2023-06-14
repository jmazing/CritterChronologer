package com.udacity.jdnd.course3.critter.employee;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udacity.jdnd.course3.critter.schedule.ScheduleEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
public class EmployeeEntity {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private int vacationDaysLeft;
    private int sickDaysLeft;
    private String address;
    private String phoneNumber;
    private Double salary;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Set<EmployeeSkill> skills;

    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysAvailable;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "employees")
    @ToString.Exclude
    @JsonIgnore
    private List<ScheduleEntity> employeeSchedule;

    public EmployeeEntity(long id, String name, Set<EmployeeSkill> skills, Set<DayOfWeek> daysAvailable) {
        this.id = id;
        this.name = name;
        this.skills = skills;
        this.daysAvailable = daysAvailable;
    }
}
