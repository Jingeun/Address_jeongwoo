package com.trend.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class PageDecoding {
	private String url;
	private String id;

	private PageDecoding() {}

	public static PageDecoding newInstance(String id) {
		PageDecoding pageDecoding = new PageDecoding();
		pageDecoding.id = id;
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
			String pcHtml = doc.html().substring(startIndex).replaceAll("pcHtml : ", "").trim();
			int endIndex = pcHtml.indexOf("<\\/font>");
			return pcHtml.substring("\"<br /><br /><br /> <p><font color=\\\"white\\\" size=\\\"5\\\">".length(), endIndex);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
