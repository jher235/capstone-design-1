package org.example.capstonedesign1.domain.propensity.repository;

import org.example.capstonedesign1.domain.propensity.entity.UserPropensity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserPropensityRepository extends JpaRepository<UserPropensity, UUID> {
}
