package org.example.capstonedesign1.domain.propensity.repository;

import org.example.capstonedesign1.domain.propensity.entity.PropensityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface PropensityQuestionRepository extends JpaRepository<PropensityQuestion, UUID> {

    /**
     * @return fetch join 으로  PropensityQuestion 과 PropensityQuestionOptions 를 가져와서 반환
     */
    @Query("select q " +
            "from PropensityQuestion as q " +
            "join fetch q.options")
    List<PropensityQuestion> findAllWithOptions();
}
