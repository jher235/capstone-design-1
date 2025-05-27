package org.example.capstonedesign1.domain.user.entity;

import java.time.LocalDate;

import org.example.capstonedesign1.domain.auth.dto.request.SignUpCompleteRequest;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.example.capstonedesign1.domain.user.entity.enums.Gender;
import org.example.capstonedesign1.domain.user.entity.enums.Status;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {
	private String nickname;
	private LocalDate birthDate;
	private Long asset;
	private Integer salary;

	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Enumerated(EnumType.STRING)
	private Status status;
	@Enumerated(EnumType.STRING)
	private Propensity propensity;

	public static Profile from(SignUpCompleteRequest request) {
		return Profile.builder()
			.nickname(request.getNickname())
			.gender(request.getGender())
			.salary(request.getSalary())
			.birthDate(request.getBirthDate())
			.asset(request.getAsset())
			.status(Status.ACTIVE)
			.build();
	}

	public void updatePropensity(Propensity propensity) {
		this.propensity = propensity;
	}

	public String getPropensityName() {
		if (this.propensity == null) {
			return "알 수 없음";
		}
		return this.propensity.getName();
	}

}
