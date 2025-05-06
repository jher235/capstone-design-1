package org.example.capstonedesign1.domain.admin.service;

import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.schema.model.Property;
import io.weaviate.client.v1.schema.model.WeaviateClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.entity.enums.Role;
import org.example.capstonedesign1.global.exception.ForbiddenException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminCommandService {

    private final WeaviateClient weaviateClient;

    public void createSchema(User user) {

        if (!user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new ForbiddenException(ErrorCode.UN_AUTHORIZED);
        }
        createBankProductSchema();
        createCardProductSchema();
    }

    public void setData(User user) {
        if (!user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new ForbiddenException(ErrorCode.UN_AUTHORIZED);
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("bankProductId", "a9aa350e-65e6-4d44-8342-4d93da4027dd");
        properties.put("name", "국민수퍼정기예금(개인)");
        properties.put("description", "국내에 거주하는 국민인 개인 또는 외국인 거주자(재외국민, 외국국적동포 포함)로서 연령에 관계없이 누구든지 가입가능\n" +
                "※ 전 금융기관을 통하여 주택청약종합저축, 청년 주택드림 청약통장, 청약저축, 청약예금, 청약부금 중 1인 1계좌만 보유 가능");
        properties.put("benefit", " 입금하려는 금액과 납입누계액의 합이 1,500만원 이하인 경우 50만원을 초과하여 입금 가능,매월 약정납입일(신규가입일 해당일)에 월저축금을 납입하는 적금식 상품으로 순위가 발생하고 소정의 청약자격을 갖추면 국민주택 및 민영주택에 모두 청약할 수 있는 입주자저축 ");
        Float[] vector1 = new Float[1536];
        Arrays.fill(vector1, 0.12345f);

        Result<WeaviateObject> result = weaviateClient.data().creator()
                .withClassName("BankProduct")
                .withProperties(properties)
                .withVector(vector1)
                .run();

        Map<String, Object> properties2 = new HashMap<>();
        properties.put("bankProductId", "1c2a6878-77c7-4849-931f-7b3fca7e7b79");
        properties.put("name", "주택청약종합저축");
        properties.put("description", "제한없음(단, 무기명으로는 가입하실 수 없습니다.)");
        properties.put("benefit", "가입자가 이율, 이자지급, 만기일 등을 직접 설계하여 저축할 수 있는 다기능 맞춤식 정기예금입니다.");
        Float[] vector2 = new Float[1536];
        Arrays.fill(vector2, 0.12345f);

        Result<WeaviateObject> result2 = weaviateClient.data().creator()
                .withClassName("BankProduct")
                .withProperties(properties)
                .withVector(vector1)
                .run();

        if (result.hasErrors()) {
            log.error(result.getError().getMessages());
            throw new IllegalArgumentException("스키마 생성 오류");
        }

    }

    public void createBankProductSchema() {
        WeaviateClass bankProduct = WeaviateClass.builder()
                .className("BankProduct")
                .description("예적금 상품")
                .properties(Arrays.asList(
                        Property.builder()
                                .name("bankProductId")
                                .dataType(Collections.singletonList("text"))
                                .description("예적금 상품 id")
                                .build(),
                        Property.builder()
                                .name("name")
                                .dataType(Collections.singletonList("text"))
                                .description("예적금 상품명")
                                .build(),
                        Property.builder()
                                .name("description")
                                .dataType(Collections.singletonList("text"))
                                .description("예적금 상품 설명")
                                .build(),
                        Property.builder()
                                .name("benefit")
                                .dataType(Collections.singletonList("text"))
                                .description("예적금 상품 benefit")
                                .build()))
//                .vectorizer("text2vec-contextionary")
                .build();

        Result<Boolean> result = weaviateClient.schema()
                .classCreator()
                .withClass(bankProduct)
                .run();

        if (result.hasErrors()) {
            log.error(result.getError().getMessages());
            throw new IllegalArgumentException("스키마 생성 오류");
        }
    }

    public void createCardProductSchema() {
        WeaviateClass bankProduct = WeaviateClass.builder()
                .className("CardProduct")
                .description("카드 상품")
                .properties(Arrays.asList(
                        Property.builder()
                                .name("cardProductId")
                                .dataType(Collections.singletonList("text"))
                                .description("카드 상품 id")
                                .build(),
                        Property.builder()
                                .name("name")
                                .dataType(Collections.singletonList("text"))
                                .description("카드 상품명")
                                .build(),
                        Property.builder()
                                .name("description")
                                .dataType(Collections.singletonList("text"))
                                .description("카드 상품 설명")
                                .build(),
                        Property.builder()
                                .name("benefit")
                                .dataType(Collections.singletonList("text"))
                                .description("카드 상품 benefit")
                                .build()))
//                .vectorizer("text2vec-contextionary")
                .build();

        Result<Boolean> result = weaviateClient.schema()
                .classCreator()
                .withClass(bankProduct)
                .run();

        if (result.hasErrors()) {
            log.error(result.getError().getMessages());
            throw new IllegalArgumentException("스키마 생성 오류");
        }
    }

}
