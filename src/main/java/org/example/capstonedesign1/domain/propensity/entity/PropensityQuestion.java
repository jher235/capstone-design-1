package org.example.capstonedesign1.domain.propensity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.capstonedesign1.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class PropensityQuestion extends BaseEntity {
    @Column(nullable = false)
    private String content;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PropensityQuestionOption> options = new ArrayList<>();
}
