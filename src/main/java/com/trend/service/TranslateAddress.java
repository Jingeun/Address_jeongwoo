package com.trend.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;

import com.google.common.collect.Maps;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TranslateAddress {
	private String currentPage;
	private String countPerPage;
	private final String confmKey = "U01TX0FVVEgyMDE2MTAwNjIyMTQzODE1NTg3";
	private String apiUrl;
	private Map<String, String> map;


	public static TranslateAddress newInstance() {
		TranslateAddress translateAddress = new TranslateAddress();
		translateAddress.currentPage = "1";
		translateAddress.countPerPage = "1";
		translateAddress.apiUrl = "http://www.juso.go.kr/addrlink/addrLinkApi.do?currentPage=" + translateAddress.currentPage +
				"&countPerPage=" + translateAddress.countPerPage + "&confmKey=" + translateAddress.confmKey + "&keyword=";
		translateAddress.map = Maps.newLinkedHashMap();
		return translateAddress;
	}

	public void InputTranslateKorAndEngMap() {
		try {
			Scanner in = new Scanner(new File("translate.txt"));
			while (in.hasNextLine()) {
				String[] token = in.nextLine().trim().split("\\s+");
				map.put(token[0].trim(), token[1].trim());
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String[] TranslateAddressAndZip(String baseAddr, String detailAddr) {
		String[] data = null;
		String baseAddrToken[] = baseAddr.split("\\s+");
		String detailAddrToken[] = detailAddr.split("\\s+");

		String address = baseAddr;
		String addressDetail = detailAddr;

		//상세 정보 포함해서 검색
		for (int i = -1; i < detailAddrToken.length; i++) {
			if (i != -1) {
				address += " " + detailAddrToken[i];
				for (int j = i; j < detailAddrToken.length; j++)
					addressDetail += detailAddrToken[j] + " ";
			}
			data = SearchAddress(address, addressDetail.trim());
			if (data != null) {
				return data;
			}
		}
		//상세 정보 검색 실패시 기존 정보 제거하면서 검색
		for (int i = baseAddrToken.length - 1; i > 0; i--) {
			address = "";
			addressDetail = "";
			int j = 0;
			for (j = 0; j <= i; j++)
				address += " " + baseAddrToken[j];
			for (int k = j - 1; k < baseAddrToken.length; k++)
				addressDetail += baseAddrToken[k] + " ";
			addressDetail += detailAddr;
			data = SearchAddress(address, addressDetail);
			if (data != null)
				return data;
		}
		data = new String[3];
		data[0] = TranslateDetailAddress(baseAddr + " " + detailAddr);
		data[1] = "";
		data[2] = null;
		return data;
	}

	private String[] SearchAddress(String address, String addressDetail) {
		String[] data = null; //0: engBaseAddr, 1: engDetailAddr 2: zipNo

		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(apiUrl + URLEncoder.encode(address, "UTF-8"));

			Node common = doc.getElementsByTagName("common").item(0);
			Element itemElement = (Element)common;
			String totalCount = getNodeStr(itemElement, "totalCount");
			//String errorMessage = getNodeStr(itemElement, "errorMessage");

			if (totalCount.equals("1")) {
				data = new String[3];
				Node juso = doc.getElementsByTagName("juso").item(0);
				itemElement = (Element)juso;
				data[0] = getNodeStr(itemElement, "engAddr");
				data[1] = TranslateDetailAddress(addressDetail);
				data[2] = getNodeStr(itemElement, "zipNo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	private String TranslateDetailAddress(String addressDetail) {
		try {
			Scanner in = new Scanner(new File("sentence.txt"));
			while (in.hasNextLine()) {
				String sentence = in.nextLine().trim();
				String setenceToken[] = sentence.split("\\s+");
				String remainSentence = sentence.replaceAll(setenceToken[0].trim() + " ", "");
				addressDetail = addressDetail.replaceAll(setenceToken[0].trim(), remainSentence);
			}
			in.close();
			addressDetail = TranslateDetailLanguage(addressDetail);
		} catch (FileNotFoundException e) {
			System.out.println("sentence.txt 파일 읽기 실패.");
			e.printStackTrace();
		}
		return addressDetail;

	}

	private String TranslateDetailLanguage(String addressDetail) {
		for (int i = 0; i < addressDetail.length(); i++) {
			String character = String.valueOf(addressDetail.charAt(i));
			if (map.containsKey(character)) {
				addressDetail = addressDetail.replaceAll(character, map.get(character));
			}
		}
		return addressDetail;
	}

	private static String getNodeStr(Element itemElement, String nodeName) {
		NodeList titleNodeList = itemElement.getElementsByTagName(nodeName);
		Element titleElement = (Element)titleNodeList.item(0);
		NodeList childTitleNodeList = titleElement.getChildNodes();
		return ((Node)childTitleNodeList.item(0)).getNodeValue();
	}

}