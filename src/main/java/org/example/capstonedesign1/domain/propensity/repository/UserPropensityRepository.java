package org.example.capstonedesign1.domain.propensity.repository;

import org.example.capstonedesign1.domain.propensity.dto.response.projection.UserPropensityPreview;
import org.example.capstonedesign1.domain.propensity.entity.UserPropensity;
import org.example.capstonedesign1.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserPropensityRepository extends JpaRepository<UserPropensity, UUID> {

    @Query("select up.id as id, up.propensity as propensity, up.createdAt as createdAt " +
            "from UserPropensity up " +
            "where up.user = :user ")
    Page<UserPropensityPreview> findUserPropensityPreviews(@Param("user") User user, Pageable pageable);
}
