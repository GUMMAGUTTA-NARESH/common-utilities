package com.gn.db;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gn.service.Constants;
import com.gn.service.GnMap;
import com.gn.util.GnUtil;


public class ExcelToDatabase {
	
	private static Logger log = Logger.getLogger(ExcelToDatabase.class.toString());
	
	private static String getCellValue(int index, Row row) {
		index = index <= 0 ? 1 :index;
		return row.getCell(index).toString();
	}
	/**
	 * This method is used to insert the pincodes for apollo rider
	 * @param file
	 * @param connection
	 * @throws Exception
	 */
	public static void insertData(String file, Connection connection) throws Exception {
		int stateId = 0;
		int districtId = 0;
		int cityId = 0;
		int pincodeId = 0;
		FileInputStream inputStream = new FileInputStream(file);
		XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workBook.getSheetAt(0);
		Iterator<Row> iterator = sheet.iterator();
		Row row = iterator.next();
		while (iterator.hasNext()) {
			row = iterator.next();
			GnMap state = DbUtils.get(connection, Constants.GET_STATE, getCellValue(1, row));
				stateId = GnUtil.hasData(state) ? state.getI("pk_id") : DbUtils.getGeneratedKey(connection, Constants.INSERT_STATE, row.getCell(0).toString(), row.getCell(1).toString(), "Active");
			GnMap district = DbUtils.get(connection, Constants.GET_DISTRICT, row.getCell(3).toString());
				districtId = GnUtil.hasData(district) ? district.getI("pk_id"): DbUtils.getGeneratedKey(connection, Constants.INSERT_DISTRICT, row.getCell(2).toString(), row.getCell(3).toString(), stateId, "Active");
			GnMap city = DbUtils.get(connection, Constants.GET_CITY, row.getCell(5).toString());
				cityId = GnUtil.hasData(city) ? city.getI("pk_id") : DbUtils.getGeneratedKey(connection, Constants.INSERT_CITY, row.getCell(4).toString(), row.getCell(5).toString(), districtId, "Active");
			GnMap postalCode = DbUtils.get(connection, Constants.GET_PINCODE, row.getCell(6).toString());
				pincodeId = GnUtil.hasData(postalCode) ? postalCode.getI("pk_id") : DbUtils.getGeneratedKey(connection, Constants.INSERT_PINCODE, row.getCell(6).toString(), row.getCell(6).toString(), cityId, "Active");
			log.info(stateId + " >> " + districtId + " >> "+ cityId +" >> " +pincodeId);
		}
		inputStream.close();
		workBook.close();
	}

}
