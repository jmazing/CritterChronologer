package com.udacity.jdnd.course3.critter.schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.udacity.jdnd.course3.critter.employee.EmployeeEntity;
import com.udacity.jdnd.course3.critter.employee.EmployeeSkill;
import com.udacity.jdnd.course3.critter.pet.PetEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ScheduleEntity {
    @Id
    @GeneratedValue
    private long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "schedule_employees", 
        joinColumns = @JoinColumn(name = "schedule_id"), 
        inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<EmployeeEntity> employees;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "schedule_pets", 
        joinColumns = @JoinColumn(name = "schedule_id"), 
        inverseJoinColumns = @JoinColumn(name = "pet_id"))
    private List<PetEntity> pets;

    private LocalDate date;
    private long schedule_uuid;
    private int priority;

    @Enumerated(EnumType.STRING)
    private Set<EmployeeSkill> skillsNeeded;

    public ScheduleEntity(long id, LocalDate date, Set<EmployeeSkill> skillsNeeded) {
        this.id = id;
        this.date = date;
        this.skillsNeeded = skillsNeeded;
    }
}
