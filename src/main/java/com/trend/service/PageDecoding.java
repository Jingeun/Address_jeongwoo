package com.trend.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class PageDecoding {
	private String url;
	private String id;

	public PageDecoding(String id) {
		this.id = id;
		//this.id = "kjw6613";
		url = "http://storefarm.naver.com/" + this.id + "/products/";
	}

	public String ParsingGoodNumber(String goodsNum) {
		try {
			Document doc = Jsoup.connect(this.url + goodsNum)
					.userAgent(
							"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
					.timeout(10000)
					.get();
			Elements ele = doc.select("div > ._detail_info_area > ._target_content_area > ._content_detail");
			String document = ele.html().replaceAll("<br /><br /><br /> <p><font color=\"white\" size=\"5\">", "");
			String encryption = new String();
			for (int i = 0; i < document.length(); i++) {
				char alpha = document.charAt(i);
				if (alpha == '<')
					break;
				encryption += alpha;
			}
			return encryption;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Asdf";
	}

}
