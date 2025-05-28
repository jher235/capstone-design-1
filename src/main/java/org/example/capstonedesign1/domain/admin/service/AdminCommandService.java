package org.example.capstonedesign1.domain.admin.service;

import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.entity.enums.Role;
import org.example.capstonedesign1.global.exception.ForbiddenException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.example.capstonedesign1.global.weaviate.WeaviateHandler;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminCommandService {

	private final WeaviateHandler weaviateHandler;

	public void createSchema(User user) {
		if (!user.getRole().equals(Role.ROLE_ADMIN)) {
			throw new ForbiddenException(ErrorCode.UN_AUTHORIZED);
		}
		weaviateHandler.createBankProductSchema();
		weaviateHandler.createCardProductSchema();
	}

	public void setData(User user) {
		if (!user.getRole().equals(Role.ROLE_ADMIN)) {
			throw new ForbiddenException(ErrorCode.UN_AUTHORIZED);
		}
		weaviateHandler.setCardProductDataFromDB();
		weaviateHandler.setBankProductDataFromDB();
	}

}
