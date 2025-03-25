package org.example.capstonedesign1.domain.propensity.entity;


import jakarta.persistence.*;
import lombok.Getter;
import org.example.capstonedesign1.global.common.BaseEntity;

@Entity
@Getter
public class PropensityQuestionOption extends BaseEntity {
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propensity_question_id")
    private PropensityQuestion question;
}
