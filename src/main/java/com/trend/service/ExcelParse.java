package com.trend.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.trend.core.Person;

public class ExcelParse {

	public List<Person> getExcelList() {
		List<Person> list = Lists.newArrayList();

		FileInputStream fis = null;
		HSSFWorkbook workbook = null;
		try {
			fis = new FileInputStream("address.xls");
			workbook = new HSSFWorkbook(fis);

			HSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);

				if (row != null) {
					Person person = new Person();
					person.setNameKor(getCell(row, 9));
					person.setGoodsNum(getCell(row, 14));
					person.setGoodsName(getCell(row, 15));
					person.setGoodsOption(getCell(row, 17));
					person.setGoodsCount(getCell(row, 19));
					person.setGoodsMsg(getCell(row, 44));
					person.setGoodsPrice(getCell(row, 24));
					person.setBuyerPhone(getCell(row, 63));
					person.setAddressee1phone(getCell(row, 58));
					person.setAddressee2phone(getCell(row, 59));
					person.setAddresseeZipCode(getCell(row, 60));
					person.setBaseAddr(getCell(row, 61));
					person.setDetailAddr(getCell(row, 62));
					list.add(person);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	private String getCell(HSSFRow row, int i) {
		Cell cell = row.getCell(i);
		if (cell == null)
			return null;

		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_NUMERIC:
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_BLANK:
				return "";
			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf((cell.getBooleanCellValue()));
			default:
				return null;
		}
	}

}
