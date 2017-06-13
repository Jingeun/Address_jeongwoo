package com.trend.service;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.trend.core.Person;

public class ExcelExport {
	String[] menu = {
			"상품번호",
			"알리상품번호",
			"상품명",
			"옵션",
			"배송메시지",
			"수량",
			"가격",
			"수취인(E)",
			"수취인(K)",
			"수취인주소(K)",
			"수취인주소(E)",
			"우편번호(입력)",
			"우편번호(검색)",
			"수취인연락처1",
			"수취인연락처2",
			"구매자연락처",
	};

	public ExcelExport() {}

	@SuppressWarnings("deprecation")
	public boolean setExcelList(List<Person> list) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("발주발송관리");
		HSSFRow row = null;

		HSSFCellStyle center = workbook.createCellStyle();
		center.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCellStyle styleAddressEng = workbook.createCellStyle();
		styleAddressEng.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		styleAddressEng.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFCellStyle styleCount = workbook.createCellStyle();
		styleCount.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleCount.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		styleCount.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		row = sheet.createRow(0);

		for (int i = 0; i < menu.length; i++) {
			row.createCell(i).setCellValue(menu[i]);
			row.getCell(i).setCellStyle(center);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 1);
			Person person = list.get(i);
			row.createCell(0).setCellValue(person.getGoodsNum());
			row.createCell(1).setCellValue(person.getGoodsEncryption());
			row.createCell(2).setCellValue(person.getGoodsName());
			row.createCell(3).setCellValue(person.getGoodsOption());
			row.createCell(4).setCellValue(person.getGoodsMsg());
			row.createCell(5).setCellValue(person.getGoodsCount());

			//수량이 1이 아니라면 셀 색 변경
			if (!person.getGoodsCount().equals("1"))
				row.getCell(5).setCellStyle(styleCount);

			row.createCell(6).setCellValue(person.getGoodsPrice());
			row.createCell(7).setCellValue(person.getNameEng1());
			row.createCell(8).setCellValue(person.getNameKor());
			row.createCell(9).setCellValue(person.getBaseAddr() + " " + person.getDetailAddr());
			row.createCell(10).setCellValue(person.getEngBaseAddr() + " " + person.getEngDetailAddr());

			//주소영문번역이 안되면 글자 하나하나씩 번역 후 셀 색 변경
			if (person.getZipCode() == null)
				row.getCell(10).setCellStyle(styleAddressEng);

			row.createCell(11).setCellValue(person.getAddresseeZipCode());
			row.createCell(12).setCellValue(person.getZipCode());
			row.createCell(13).setCellValue(person.getAddressee1phone());
			row.createCell(14).setCellValue(person.getAddressee2phone());
			row.createCell(15).setCellValue(person.getBuyerPhone());
		}

		for (int i = 0; i < menu.length; i++)
			sheet.autoSizeColumn(i);

		try {
			FileOutputStream fos = new FileOutputStream("result.xls");
			workbook.write(fos);
			workbook.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}