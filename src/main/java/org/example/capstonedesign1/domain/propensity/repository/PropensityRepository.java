package org.example.capstonedesign1.domain.propensity.repository;

import org.example.capstonedesign1.domain.propensity.entity.Propensity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropensityRepository extends JpaRepository<Propensity, UUID> {
    Optional<Propensity> findByName(String name);

}
