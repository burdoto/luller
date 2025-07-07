package de.kaleidox.luller.repo;

import de.kaleidox.luller.trait.PersonalityTrait;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PersonalityTraitRepository extends CrudRepository<PersonalityTrait, UUID> {}
