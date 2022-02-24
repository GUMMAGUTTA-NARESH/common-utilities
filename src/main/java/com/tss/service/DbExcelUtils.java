package com.tss.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tss.db.DbUtils;
import com.tss.util.GnUtil;

public class DbExcelUtils {
	public static void createDb() {
		
	}
	public static List<String> getSheets(String path) throws EncryptedDocumentException, IOException {
		File myFile = new File(path);
		Workbook wb = WorkbookFactory.create(myFile);
		List<String> sheetNames = new ArrayList<String>();
		for (int i=0; i<wb.getNumberOfSheets(); i++) {
		    sheetNames.add( wb.getSheetName(i));
		}
		return sheetNames;
	}
	@SuppressWarnings("resource")
	public static Map<Integer, Object> getColumnsMap(String path) throws IOException {
		FileInputStream inpStream = new FileInputStream(new File(path));
		XSSFWorkbook workbook = new XSSFWorkbook(inpStream);
		 XSSFSheet spreadsheet = workbook.getSheetAt(0);
		 Row row = spreadsheet.getRow(0);
//		 String[] sheets = new String[spreadsheet.getLastRowNum()+1];
		 String[] sheets = new String[spreadsheet.getRow(0).getLastCellNum()+1];
		 Map<Integer, Object> cls = new LinkedHashMap<Integer, Object>();
		 for(int col=0; col<= spreadsheet.getRow(0).getLastCellNum()-2; col++){
	           Cell cell = row.getCell(col);
//	           String columnName = cell.getStringCellValue();
	           sheets[col] = cell.toString();
	           cls.put(col, cell.toString());
	       }
		 return cls;
	}
	public static List<String> getColumnsList(Map<Integer,Object> map, boolean flag){
		List<String> list = new ArrayList<String>();
		for (Integer key: map.keySet()) { 
			if(flag) list.add(GnUtil.dbColumn(map.get(key).toString()));
			else list.add(map.get(key).toString());
		} 
		return list;
	}
	@SuppressWarnings({ "unused", "resource" })
	public static void insert(String file, Connection connection, String table, boolean flag) throws Exception {
		if(GnUtil.isBlank(file) || GnUtil.isBlank(connection)) throw new Exception("file or connection Failed");
		try {
			FileInputStream inpStream = new FileInputStream(new File(file));
			XSSFWorkbook workbook = new XSSFWorkbook(inpStream);
			String[] sheets = getSheets(file).toArray(new String[getSheets(file).size()]);
			Map<Integer, Object> getCols = getColumnsMap(file);
			List<String> colsList = getColumnsList(getCols, true);
			String[] columns = colsList.toArray(new String[colsList.size()]);
			for (String sName : sheets) {
				XSSFSheet sheet = workbook.getSheet(sName);
				Iterator<Row> iterator = sheet.iterator();
				Row row = (Row) iterator.next();
				System.out.println(row.getPhysicalNumberOfCells());
				int id = 0;
				int index = 0;
				while (iterator.hasNext()) {
					row = (Row) iterator.next();
					// if flag is true check the record before insert else insert duplicates 
					if(flag) {
						if(DbUtils.get(connection, DbUtils.generateSelectQuery(table,getCols.get(1).toString()), row.getCell(1).toString()) == null){
							id = DbUtils.getGeneratedKey(connection,DbUtils.generateInsertQuery(null, table, columns),
									row.getCell(0).toString(),row.getCell(1).toString(),row.getCell(2).toString(),row.getCell(3).toString(),
									row.getCell(4).toString(),row.getCell(5).toString(),row.getCell(6).toString(),row.getCell(7).toString(),
							row.getCell(8).toString());
						}
						} else {
							id = DbUtils.getGeneratedKey(connection,DbUtils.generateInsertQuery(null, table, columns),
									row.getCell(0).toString(),row.getCell(1).toString(),row.getCell(2).toString(),row.getCell(3).toString(),
									row.getCell(4).toString(),row.getCell(5).toString(),row.getCell(6).toString(),row.getCell(7).toString(),
									row.getCell(8).toString());
						}
					System.out.println(id);
				}
				}
				
			inpStream.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "resource", "rawtypes" })
	public static Map<Integer, Object> getDatatypesMap(String file) throws IOException {
		Map<Integer, Object> dataTypes = new LinkedHashMap<Integer, Object>();
		try (FileInputStream fis = new FileInputStream(file)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
//            sheet.getRow(0).forEach(cell ->{
//            	colms.put(cell.getColumnIndex(),cell.getStringCellValue());
//            });
            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()) {
                XSSFRow row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                while (cells.hasNext()) {
                    XSSFCell cell = (XSSFCell) cells.next();
                    switch (cell.getCellType()) {
                    case STRING:
                    	cell.getRichStringCellValue().toString();
                    	 dataTypes.put(cell.getColumnIndex(), "VARCHAR(255) NULL,");
                    	 break;
                    case NUMERIC:
                    	if(DateUtil.isCellDateFormatted(cell)) {
                    		dataTypes.put(cell.getColumnIndex(), "TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,");
                    	}else if(cell.getNumericCellValue() > 999999999) {
                    		dataTypes.put(cell.getColumnIndex(), "VARCHAR(255) NULL,");
                    	}else
                    	 dataTypes.put(cell.getColumnIndex(), "INT NOT NULL,");
                    	 break;
                    case BOOLEAN:
                    	dataTypes.put(cell.getColumnIndex(), "TINYINT(1) NULL DEFAULT '0',");
                    	break;
                    case BLANK:
                    	dataTypes.put(cell.getColumnIndex(), null);
                    	break;
                    	default:
                    		dataTypes.put(cell.getColumnIndex(), "VARCHAR(255) NULL,");
                    		break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		return dataTypes;
	}
	
	public static String replaceWildcards(String replace, String with) {
		return null;
	}

	public static String queryBuilder(String file, String scheme, boolean flag) throws Exception {
		if(!GnUtil.isValidPath(file)) return "Given File is not Valid";
		if(scheme == null || scheme == "") {
			scheme = "default";
		}
		List<String> dataTypes = getColumnsList(getDatatypesMap(file), false);
		List<String> cols = getColumnsList(getColumnsMap(file), true);
		StringBuilder builder = new StringBuilder();
		List<String> query = new ArrayList<String>(cols.size());
		for(int i = 0; i < cols.size(); i++) {
			query.add(GnUtil.encloseGrave(cols.get(i))+" "+dataTypes.get(i));
		}
		query.forEach(builder::append);
		String s = builder.substring(0, builder.length()-1);
		String qry =  (flag) ? Constants.CREATE_TABLE_DEFAULTS.replace("{{query}}", s).replace("{{table}}", GnUtil.encloseGrave(GnUtil.getFileName(file.toLowerCase()))).replace("{{scheme}}", GnUtil.encloseGrave(scheme)) 
				: Constants.CREATE_TABLE.replace("{{query}}", s).replace("{{table}}", GnUtil.encloseGrave(GnUtil.getFileName(file.toLowerCase()))).replace("{{scheme}}", GnUtil.encloseGrave(scheme));
	return ("default".equalsIgnoreCase(scheme)) ? qry.replace("`default`.", ""): qry;
	}
	
	public static void imageFrmExcelToFolder(String file, String dir) throws Exception {
		FileInputStream inpStream = new FileInputStream(new File(file));
		XSSFWorkbook workbook = new XSSFWorkbook(inpStream);
		XSSFSheet sheet = workbook.getSheetAt(0);
        int pictureColumn = 4;

        for (XSSFShape shape : sheet.getDrawingPatriarch()) {
            if (shape instanceof XSSFPicture) {
            	XSSFPicture picture = (XSSFPicture) shape;
            	XSSFClientAnchor anchor = (XSSFClientAnchor) picture.getAnchor();

                // Ensure to use only relevant pictures
                if (anchor.getCol1() == pictureColumn) {

                    // Use the row from the anchor
                	XSSFRow pictureRow = sheet.getRow(anchor.getRow1());
                    if (pictureRow != null) {
                    	XSSFPictureData data = picture.getPictureData();
						byte data1[] = data.getData();
//						FileOutputStream out = new FileOutputStream(
//								"F:\\images-naarm\\logos\\logo_" + (pictureRow.getRowNum()+1) + ".png");
						FileOutputStream out = new FileOutputStream(dir+"\\logo_" + (pictureRow.getRowNum()+1) + ".png");
						out.write(data1);
						out.close();
                       }
                }
            }
        }
        inpStream.close();
		workbook.close();
	}
}
