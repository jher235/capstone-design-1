package org.example.capstonedesign1.domain.user.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import org.example.capstonedesign1.domain.user.entity.Profile;
import org.example.capstonedesign1.domain.user.entity.User;

import lombok.Builder;

public record UserDetailResponse(UUID id,
								 String email,
								 boolean registerCompleted,
								 String nickname,
								 LocalDate birthDate,
								 Long asset,
								 Integer salary,
								 String gender,
								 String propensity
) {

	@Builder
	public UserDetailResponse(UUID id, String email, boolean registerCompleted, String nickname, LocalDate birthDate,
		Long asset, Integer salary, String gender, String propensity) {
		this.id = id;
		this.email = email;
		this.registerCompleted = registerCompleted;
		this.nickname = nickname;
		this.birthDate = birthDate;
		this.asset = asset;
		this.salary = salary;
		this.gender = gender;
		this.propensity = propensity;
	}

	public static UserDetailResponse from(User user) {
		Profile userProfile = user.getProfile();
		return UserDetailResponse.builder()
			.id(user.getId())
			.email(user.getEmail())
			.registerCompleted(user.isRegisterCompleted())
			.nickname(userProfile.getNickname())
			.birthDate(userProfile.getBirthDate())
			.asset(userProfile.getAsset())
			.salary(userProfile.getSalary())
			.gender(userProfile.getGender().getDescription())
			.propensity(userProfile.getPropensityName())
			.build();
	}
}
