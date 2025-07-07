package de.kaleidox.luller.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public abstract class BasicEntity {
    @Id UUID id = UUID.randomUUID();
}
