package org.example.capstonedesign1.global.template;

import org.example.capstonedesign1.domain.propensity.dto.request.SurveyRequest;


import java.util.List;


public class PromptTemplate {
    private static final String BASE_TEMPLATE = """
            ### 요청하고 싶은 것
            {{request}}
                    
            ### 응답 값 형식
            {{responseFormat}}
            """;

    private static final String SURVEY_ENTRY_TEMPLATE = """
            ### 질문: %s
            ### 응답: %s
            
            """;

    public static String fillTemplate(String request, String responseFormat) {
        return BASE_TEMPLATE.replace("{{request}}", request)
                .replace("{{responseFormat}}", responseFormat);
    }

    //이걸 스트링 빌더로 묶어서 처리
    public static String entry(String question, String answer){
        return String.format(SURVEY_ENTRY_TEMPLATE, question, answer);
    }

    public static String propensityAnalysisPrompt(String gender, List<SurveyRequest.SurveyItemEntry> surveyEntries) {
        StringBuilder stringBuilder = new StringBuilder();
        surveyEntries.stream()
                .forEach(e -> stringBuilder.append(entry(e.getQuestion(), e.getSelectedAnswer())));

        return fillTemplate(
                """
                ## 명령
                성별과 사용자의 답변을 기반으로 금융 성향 타입
                (보수형(저축과 안정 추구), 소비형(즉각적 소비 선호), 투자형(위험 감수 및 수익 추구), 균형형(저축과 소비의 조화)**, 융합형(상황에 따라 유동적으로 반응)**) 을 분석하고, 
                타입에 대한 설명, 장단점, 주의점을 기계적이지 않고 자세하게 답변해줘.
                결과는 JSON 형식으로 반환해줘.

                ## 사용자 정보
                성별: %s

                ## 답변
                %s
                """.formatted(gender, stringBuilder),
                """
                {
                    "type": "<금융 성향 유형>",
                    "description": "<금융 성향 설명>",
                    "prosAndCons": "<금융 성향 장단점 설명>",
                    "precaution": "<해당 금융 성향인들 주의점>"
                }
                """
        );
    }


}
