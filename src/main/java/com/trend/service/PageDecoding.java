package com.trend.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class PageDecoding {
	private String url;

	private PageDecoding() {}

	public static PageDecoding newInstance(String id) {
		PageDecoding pageDecoding = new PageDecoding();
		pageDecoding.url = "http://storefarm.naver.com/" + id + "/products/";
		return pageDecoding;
	}

	public String ParsingGoodNumber(String goodsNum) {
		try {
			Document doc = Jsoup.connect(this.url + goodsNum)
					.userAgent(
							"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
					.timeout(10000)
					.get();
			int startIndex = doc.html().indexOf("pcHtml");
			if (startIndex < 0)
				return "";
			String pcHtml = doc.html().substring(startIndex).replaceAll("pcHtml : ", "").trim().replaceAll("<br />", "");

			Elements font = Jsoup.parse(pcHtml).select("font");
			if (font != null && font.size() > 0) {
				String encryption = font.get(0).text();
				return encryption.substring(0,  encryption.indexOf("<\\/font>"));
			}

			Elements span = Jsoup.parse(pcHtml).select("span");
			if (span != null && span.size() > 0) {
				String encryption = span.get(0).text();
				return encryption.substring(0, encryption.indexOf("<\\/span>"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
