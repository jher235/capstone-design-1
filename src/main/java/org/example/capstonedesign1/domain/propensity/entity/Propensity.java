package org.example.capstonedesign1.domain.propensity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.example.capstonedesign1.global.common.BaseEntity;

@Entity
public class Propensity extends BaseEntity {
    @Column(nullable = false)
    private String name;
}
