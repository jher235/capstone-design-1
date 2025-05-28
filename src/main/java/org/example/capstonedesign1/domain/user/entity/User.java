package org.example.capstonedesign1.domain.user.entity;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.example.capstonedesign1.domain.user.entity.enums.Role;
import org.example.capstonedesign1.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends BaseEntity {

	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean registerCompleted = false;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private final Role role = Role.ROLE_USER;

	@Embedded
	private Profile profile;

	public User(String email, String password) {
		this.email = email;
		this.password = password;
		this.registerCompleted = false;
	}

	public void signUpComplete(Profile profile) {
		this.profile = profile;
		this.registerCompleted = true;
	}

	public void updatePropensity(Propensity propensity) {
		this.profile.updatePropensity(propensity);
	}

	public String get6DigitBirthDate() {
		LocalDate birthDate = this.profile.getBirthDate();
		return birthDate.format(DateTimeFormatter.ofPattern("yyMMdd"));
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User that)) {
			return false;
		}
		return Objects.equals(that.getId(), this.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}

	public String getInfoString() {
		return "성별: " + this.profile.getGender().getDescription()
			+ ", 월급(원 단위): " + this.profile.getSalary()
			+ ", 자산(원 단위): " + this.profile.getAsset()
			+ ", 나이: " + Period.between(this.profile.getBirthDate(), LocalDate.now()).getYears()
			+ ", 금융 성향: " + this.profile.getPropensityName();
	}
}
