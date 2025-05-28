package org.example.capstonedesign1.global.xlsx;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.exception.BadRequestException;
import org.example.capstonedesign1.global.exception.ForbiddenException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class XlsxHandler {
	private static final int MAX_ROW = 100;
	private static final int PAYMENT_RECORD_START_ROW = 9;
	private static final String DELIMITER = ", ";

	private static final int PAYMENT_RECORD_ORGANIZATION_COLUMN = 4;
	private static final int PAYMENT_RECORD_ACCOUNT_COLUMN = 5;
	private static final int PAYMENT_RECORD_DATE_COLUMN = 1;
	private static final int PAYMENT_RECORD_UNNECESSARY_COLUMN_COUNT = 2;
	private static final int PAYMENT_RECORD_DATE_START = 5;
	private static final int PAYMENT_RECORD_DATE_END = 16;

	public void validXlsxFile(MultipartFile file) {
		if (file.isEmpty()
			|| !file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
			throw new BadRequestException(ErrorCode.INVALID_FILE);
		}
	}

	public String resolveConsumptionRecord(
		User user,
		MultipartFile file,
		Optional<String> filePassword
	) {
		//try with resource
		try (InputStream inputStream = file.getInputStream()) {
			POIFSFileSystem fs = new POIFSFileSystem(inputStream);
			EncryptionInfo info = new EncryptionInfo(fs);
			Decryptor decryptor = Decryptor.getInstance(info);

			String password = filePassword.orElseGet(user::get6DigitBirthDate);

			if (decryptor.verifyPassword(password)) {
				try (InputStream decryptedStream = decryptor.getDataStream(fs);
					 Workbook workbook = new XSSFWorkbook(decryptedStream)) {
					Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 사용
					return paymentRecordFormat(sheet);
				}
			}
			throw new ForbiddenException(ErrorCode.PAYMENT_XLSX_UNAUTHORIZED);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (GeneralSecurityException e) {
			throw new ForbiddenException(ErrorCode.PAYMENT_XLSX_UNAUTHORIZED, "결제 내역 파일의 PW가 일치하지 않습니다.");
		}
	}

	private String paymentRecordFormat(Sheet sheet) {
		StringBuilder sb = new StringBuilder();

		int maxRow = Math.min(MAX_ROW, sheet.getLastRowNum());
		for (int i = PAYMENT_RECORD_START_ROW; i < maxRow; i++) { // START_ROW 이전에는 필요없는 정보들이므로 제외
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			for (int j = 0;
				 j < row.getLastCellNum() - PAYMENT_RECORD_UNNECESSARY_COLUMN_COUNT; j++) { // 거래 후 잔액은 제외하기 위해서 -1을 함
				Cell cell = row.getCell(j);
				if (cell == null || j == PAYMENT_RECORD_ORGANIZATION_COLUMN || j == PAYMENT_RECORD_ACCOUNT_COLUMN) {
					continue;
				} else if (j == PAYMENT_RECORD_DATE_COLUMN) {
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
	 * @param cell
	 * @return
	 */
	private String parseTFromDTColumn(String cell) {
		if (cell.length() > PAYMENT_RECORD_DATE_END) {
			return cell.substring(PAYMENT_RECORD_DATE_START, PAYMENT_RECORD_DATE_END);
		}
		return cell;
	}

	private String getValueFromCell(Cell cell) {
		switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				return String.valueOf((long)cell.getNumericCellValue());
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			default:
				return "N/A";
		}
	}

}
