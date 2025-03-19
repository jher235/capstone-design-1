package org.example.capstonedesign1.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.capstonedesign1.domain.user.entity.enums.Gender;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class SignUpCompleteRequest {

    @NotBlank
    private String nickname;

    @Schema(
            description = "생년월일",
            example = "1990-01-01",
            type = "string"
    )
    @NotNull(message = "생년월일은 필수입니다.")
    @Past
    private LocalDate birthDate;

    @Schema(
            description = "성별",
            allowableValues = {"MALE", "FEMALE"}
    )
    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    @Schema(
            description = "자산. 원 단위",
            minimum = "0",
            maximum = "10000000000000"
    )
    @NotNull(message = "자산값은 필수입니다.")
    @Min(value = 0, message = "자산은 0 이상이어야 합니다.")
    @Max(value = 10_000_000_000_000L, message = "자산은 1경 원을 넘을 수 없습니다.")
    private Long asset;

    @Schema(
            description = "월 수익. 원 단위",
            minimum = "0",
            maximum = "2000000000"
    )
    @Min(value = 0, message = "월 수익은 0 이상이어야 합니다.")
    @Max(value = 2_000_000_000, message = "월 수익은 20억 원을 넘을 수 없습니다.")
    private Integer salary;

}
