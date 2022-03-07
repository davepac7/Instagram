package com.ableto.utilities;

import com.ableto.base.BaseTest;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ExcelFunctions extends BaseTest {

	// todo: should we leverage a base logger class with static logger?
	static LogBuilder logger = new LogBuilder();

	// Generic method to return the number of rows in the sheet.
	public static int getRowCount(String xlPath, String sheet) {
		int rowCount = 0;

		try (FileInputStream fis = new FileInputStream(xlPath); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
			XSSFSheet worksheet = workbook.getSheet(sheet);
			rowCount = worksheet.getLastRowNum() + 1;
		} catch (Exception e) {
			logger.error("" + e.getStackTrace());
		}

		return rowCount;
	}

	public static int getColumnCount(String xlPath, String sheet, int row) throws IOException {
		int columnCount = 0;

		try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlPath))) {
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			XSSFRow myExcelRow = myExcelSheet.getRow(row);
			columnCount = myExcelRow.getLastCellNum();
		}

		return columnCount;
	}

	// Generic method to return the column values in the sheet.
	public static String getCellValue(String xlPath, String sheet, int row, int col) {
		String data = "";
		try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlPath))) {
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			data = myExcelSheet.getRow(row).getCell(col).toString();
		} catch (Exception e) {
			logger.error("" + e.getStackTrace());
		}
		return data;
	}

	public static void writeData(String xlPath, String sheet, int row, int col, String data) {
		try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlPath));
				FileOutputStream output = new FileOutputStream(xlPath)) {
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			myExcelSheet.createRow(row).createCell(col).setCellValue(data);
			myExcelBook.write(output);
		} catch (Exception e) {
			logger.error("" + e.getMessage());
		}
	}
}
