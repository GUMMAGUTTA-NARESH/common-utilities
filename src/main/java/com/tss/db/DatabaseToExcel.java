/**
 * 
 */
package com.tss.db;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mongodb.DB;


/**
 * @author NARESH GUMMAGUTTA
 * @since 4 Oct, 2021
 */
public class DatabaseToExcel {
	static final String PATH = "G:\\InputFiles\\Resultset";

	
	public static String databaseToExcel(ResultSet resultSet, String fileName, Connection con)
			throws SQLException, FileNotFoundException, IOException {

		ResultSetMetaData metaData = resultSet.getMetaData();
//		List<Map<String, Object>> test = DbUtils.getMapList(con, sql);
		List<String> columns = new ArrayList<String>();
//		for(Map<String, Object> m : test) {
//			m.forEach((key, value) -> columns.add(value.toString()));
////			columns.add(m.values());
//		}
		System.out.println(columns);
		List<String> columns1 = new ArrayList<String>();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			columns.add(metaData.getColumnLabel(i));
			//TODO need to call mapLIst and iterate store to list
		}
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Research areas");
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 10);
//		headerFont.setColor(IndexedColors.BLACK.index);
		headerFont.setFontName("Arial");
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(headerFont);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		Row header = sheet.createRow(0);
		for (int i = 0; i < columns.size(); i++) {
			Cell cell = header.createCell(i);
//			header.createCell(i).setCellStyle(cellStyle);
//			header.createCell(i).setCellValue(columns.get(i));
			cell.setCellValue(columns.get(i));
			cell.setCellStyle(cellStyle);
			sheet.autoSizeColumn(i);
		}
		int rowIndex = 0;
		while (resultSet.next()) {
			Row row = sheet.createRow(++rowIndex);
			for (int i = 0; i < columns.size(); i++) {
				row.createCell(i).setCellValue(Objects.toString(resultSet.getObject(columns.get(i)), ""));
			}
		}
		FileOutputStream fileOutputStream = new FileOutputStream(PATH + "\\" + fileName);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		workbook.close();
		return "Success";
	}
	
	public static String databaseToExcelForNaarm(ResultSet resultSet, String fileName, Connection con,String s)
			throws SQLException, FileNotFoundException, IOException {

		ResultSetMetaData metaData = resultSet.getMetaData();
		ResultSet set = DbUtils.getExecuteQueryForNaarms(con, s);
		List<Map<String, Object>> test = DbUtils.getMapList(con, s);
		List<String> columns = new ArrayList<String>();
		columns.add("NAME OF THE INSTITUTE");
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			columns.add(metaData.getColumnLabel(i));
		}
		
//		for(Map<String, Object> m : test) {
//			m.forEach((key, value) -> columns.add(value.toString()));
//		}
		System.out.println(columns);
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Research areas");
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 10);
//		headerFont.setColor(IndexedColors.BLACK.index);
		headerFont.setFontName("Arial");
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(headerFont);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		Row header = sheet.createRow(0);
		for (int i = 0; i < columns.size(); i++) {
			Cell cell = header.createCell(i);
//			header.createCell(i).setCellStyle(cellStyle);
//			header.createCell(i).setCellValue(columns.get(i));
			cell.setCellValue(columns.get(i));
			cell.setCellStyle(cellStyle);
			sheet.autoSizeColumn(i);
		}
		int rowIndex = 0;
		while (resultSet.next()) {
			Row row = sheet.createRow(++rowIndex);
//			System.out.println(resultSet.getObject(columns.get(2)));
			for (int i = 0; i < columns.size(); i++) {
//				row.createCell(i).setCellValue(Objects.toString(resultSet.getObject(columns.get(i))));
				System.out.println(resultSet.getObject("temp_research_areas"));
			}
			
//			for (int i = 0; i < columns.size(); i++) {
//				row.createCell(i).setCellValue(Objects.toString(resultSet.getObject(columns.get(i)), ""));
//			}
		}
		FileOutputStream fileOutputStream = new FileOutputStream(PATH + "\\" + fileName);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		workbook.close();
		return "Success";
	}
}
