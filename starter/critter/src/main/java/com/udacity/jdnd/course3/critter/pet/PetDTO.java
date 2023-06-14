package com.udacity.jdnd.course3.critter.pet;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form that pet request and response data takes. Does not map
 * to the database directly.
 */
@Data
@NoArgsConstructor
public class PetDTO {
    private long id;
    private PetType type;
    private String name;
    private long ownerId;
    private LocalDate birthDate;
    private String notes;

    public PetDTO(long id, PetType type, String name, LocalDate birthDate, String notes) {
        this.id = id;
        this.type =type;
        this.name = name;
        this.birthDate = birthDate;
        this.notes = notes;
    }
}
