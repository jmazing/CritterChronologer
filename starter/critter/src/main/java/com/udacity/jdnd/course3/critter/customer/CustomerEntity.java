package com.udacity.jdnd.course3.critter.customer;

import java.time.LocalDate;
import java.util.List;

import com.udacity.jdnd.course3.critter.pet.PetEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String notes;
    private LocalDate birthDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<PetEntity> pets;

    public CustomerEntity(long id, String name, String phoneNumber, String notes) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
    }
}
