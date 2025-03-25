package org.example.capstonedesign1.domain.propensity.dto.response.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface UserPropensityPreview {
    UUID getId();
    String getPropensity();
    LocalDateTime getCreatedAt();
}
