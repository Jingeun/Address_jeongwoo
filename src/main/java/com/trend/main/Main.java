package com.trend.main;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.trend.core.Person;
import com.trend.service.ExcelExport;
import com.trend.service.ExcelParse;
import com.trend.service.PageDecoding;
import com.trend.service.TranslateAddress;
import com.trend.service.TranslateName;

public class Main {

	private static List<Person> list;
	private static String Id;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("아이디 : ");
		Id = in.nextLine().trim();
		in.close();

		Main main = new Main();
		System.out.println("엑셀 데이터 읽는 중...");
		if (!main.getExcelList())
			TrandError("Excel Input Error!");
		System.out.println("엑셀데이터 읽기 완료");

		System.out.print("번역 작업 중");
		main.TranslateNameAndAddress(Id);
		System.out.println("\n번역 완료");

		main.setExcelList();
		System.out.println("엑셀 저장 완료");

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void TranslateNameAndAddress(String Id) {
		TranslateName name = TranslateName.newInstance();
		TranslateAddress addr = TranslateAddress.newInstance();
		addr.InputTranslateKorAndEngMap();

		int size = list.size();
		for (int i = 0, per = 0; i < size; i++) {
			if (((double)i / size) * 100 >= per) {
				per += 10;
				System.out.print(".");
			}
			Person tmpPerson = list.get(i);

			//이름번역
			String[] EngName = name.TranslateNameKor(tmpPerson.getNameKor());
			tmpPerson.setNameEng1(EngName[0]);
			tmpPerson.setNameEng2(EngName[1]);

			//주소번역
			String[] AddrAndZipNo = addr.TranslateAddressAndZip(tmpPerson.getBaseAddr(), tmpPerson.getDetailAddr());
			if (AddrAndZipNo != null) {
				tmpPerson.setEngBaseAddr(AddrAndZipNo[0]);
				tmpPerson.setEngDetailAddr(AddrAndZipNo[1]);
				tmpPerson.setZipCode(AddrAndZipNo[2]);
			}

			//복호화
			PageDecoding decoding = PageDecoding.newInstance(Id);
			String decodingNum = decoding.ParsingGoodNumber(tmpPerson.getGoodsNum());
			tmpPerson.setGoodsEncryption(decodingNum);
		}
	}

	private void setExcelList() {
		ExcelExport export = new ExcelExport();
		export.setExcelList(list);
	}

	private boolean getExcelList() {
		ExcelParse excel = new ExcelParse();
		list = excel.getExcelList();
		return list.size() != 0;
	}

	private static void TrandError(String str) {
		System.out.println(str);
		System.exit(1);
	}

}