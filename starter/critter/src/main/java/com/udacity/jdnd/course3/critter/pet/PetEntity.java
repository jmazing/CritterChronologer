package com.udacity.jdnd.course3.critter.pet;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udacity.jdnd.course3.critter.customer.CustomerEntity;
import com.udacity.jdnd.course3.critter.schedule.ScheduleEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
public class PetEntity {
    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private PetType petType;

    private String name;
    private LocalDate birthDate;
    private String date;
    private String notes;


    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private CustomerEntity owner;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "pets")
    @ToString.Exclude
    @JsonIgnore
    private List<ScheduleEntity> petSchedule;


    public PetEntity(long id, PetType petType, String name, LocalDate birthDate, String notes) {
        this.id = id;
        this.petType = petType;
        this.name = name;
        this.birthDate = birthDate;
        this.notes = notes;
    }
}
