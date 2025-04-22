package org.example.capstonedesign1.domain.cardproduct.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.capstonedesign1.domain.cardproduct.dto.json.CardProductRecommendationContent;
import org.example.capstonedesign1.domain.cardproduct.dto.response.CardProductRecommendationResponse;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProduct;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProductRecommendation;
import org.example.capstonedesign1.domain.cardproduct.repository.CardProductRecommendationRepository;
import org.example.capstonedesign1.domain.cardproduct.repository.CardProductRepository;
import org.example.capstonedesign1.domain.chat.client.OpenAiApiClient;
import org.example.capstonedesign1.domain.chat.dto.Message;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.service.UserQueryService;
import org.example.capstonedesign1.global.exception.AuthorizedException;
import org.example.capstonedesign1.global.exception.BadRequestException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.example.capstonedesign1.global.template.PromptTemplate;
import org.example.capstonedesign1.global.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;


@Log4j2
@Service
@RequiredArgsConstructor
public class CardProductCommandService {
    private static final int MAX_ROW = 100;
    private static final int START_ROW = 9;
    private static final String DELIMITER = ", ";

    private final UserQueryService userQueryService;

    private final CardProductRepository cardProductRepository;
    private final CardProductRecommendationRepository cardProductRecommendationRepository;

    private final OpenAiApiClient openAiApiClient;

    public CardProductRecommendationResponse recommendCardProduct(User user, MultipartFile file) {
        validXlsxFile(file);
        String paymentRecord = resolvePaymentRecord(user, file); //비동기로 처리하면 더 빠를 것 같음.

        Propensity userPropensity = userQueryService.ensureUserPropensity(user);
        List<CardProduct> cardProducts = cardProductRepository.getRecommendable(userPropensity);

        String prompt = PromptTemplate.CardProductRecommendPrompt(user, paymentRecord, cardProducts);
        String response = openAiApiClient.sendRequest(List.of(new Message(OpenAiApiClient.SYSTEM_ROLE, prompt)));

        CardProductRecommendationContent content = JsonUtil.parseClass(CardProductRecommendationContent.class, response);

        String recommendationJson = JsonUtil.convertToJson(content.recommendations());

        CardProductRecommendation cardProductRecommendation = new CardProductRecommendation(
                user, content.strategy(), recommendationJson);
        cardProductRecommendationRepository.save(cardProductRecommendation);
        return CardProductRecommendationResponse.from(cardProductRecommendation);
    }

    private void validXlsxFile(MultipartFile file) {
        if (file.isEmpty()
                || !file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
            throw new BadRequestException(ErrorCode.INVALID_FILE);
        }
    }


    private String resolvePaymentRecord(User user, MultipartFile file) {
        //try with resource
        try (InputStream inputStream = file.getInputStream()) {
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            EncryptionInfo info = new EncryptionInfo(fs);
            Decryptor decryptor = Decryptor.getInstance(info);

            if (decryptor.verifyPassword(user.get6DigitBirthDate())) {
                try (InputStream decryptedStream = decryptor.getDataStream(fs);
                     Workbook workbook = new XSSFWorkbook(decryptedStream)) {
                    Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 읽기
                    return paymentRecordFormat(sheet);
                }
            }
            throw new AuthorizedException(ErrorCode.PAYMENT_XLSX_UNAUTHORIZED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new AuthorizedException(ErrorCode.PAYMENT_XLSX_UNAUTHORIZED, "결제 내역 파일의 PW가 일치하지 않습니다.");
        }
    }

    private String paymentRecordFormat(Sheet sheet) {
        StringBuilder sb = new StringBuilder();

        int maxRow = Math.min(MAX_ROW, sheet.getLastRowNum());
        for (int i = START_ROW; i < maxRow; i++) { // START_ROW 이전에는 필요없는 정보들이므로 제외
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            for (int j = 0; j < row.getLastCellNum() - 1; j++) { // 거래 후 잔액은 제외하기 위해서 -1을 함
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                } else if (j == 1) {
                    sb.append(parseTFromDTColumn(cell.getStringCellValue()));
                } else {
                    sb.append(getValueFromCell(cell));
                }
                sb.append(DELIMITER);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * DateTime 에서 날짜, 시간만 추출하여 반환. 년도, 초 등은 제외
     *
     * @param cell
     * @return
     */
    private String parseTFromDTColumn(String cell) {
        if (cell.length() > 16) {
            return cell.substring(5, 16);
        }
        return cell;
    }

    private String getValueFromCell(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "N/A";
        }
    }


}
