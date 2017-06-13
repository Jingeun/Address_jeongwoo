package com.trend.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TranslateName {
	private String apiClientId;
	private String apiSecret;
	private String url;
	private String[] name;

	private TranslateName() {}

	public static TranslateName newInstance() {
		TranslateName translateName = new TranslateName();
		try {
			Scanner in = new Scanner(new File("naverAPI.txt"));
			translateName.apiClientId = in.nextLine().trim();
			translateName.apiSecret = in.nextLine().trim();
			in.close();
			translateName.url = "https://openapi.naver.com/v1/krdict/romanization?query=";
			translateName.name = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return translateName;
	}

	public String[] TranslateNameKor(String nameKor) {
		name = new String[2];
		name[0] = name[1] = nameKor;
		try {
			nameKor = nameKor.replaceAll(" ", "");
			String nameUrl = this.url + nameKor;
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet get = new HttpGet(nameUrl);

			//Set headers for API key authroization
			get.addHeader("X-Naver-Client-Id", apiClientId);
			get.addHeader("X-Naver-Client-secret", apiSecret);

			HttpResponse response = client.execute(get);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null)
				buffer.append(line);
			rd.close();

			String result = buffer.toString();
			Charset.forName("UTF-8").encode(result);
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject)parser.parse(result);
			JSONArray aResultArr = (JSONArray)obj.get("aResult");

			if (aResultArr.size() == 0)
				return name;

			JSONObject aResult = (JSONObject)aResultArr.get(0);
			JSONArray aItems = (JSONArray)aResult.get("aItems");

			if (aItems.size() == 0)
				name[0] = name[1] = nameKor;
			else if (aItems.size() == 1) {
				JSONObject tmpObj = (JSONObject)aItems.get(0);
				name[0] = name[1] = tmpObj.get("name").toString();
			} else {
				JSONObject tmpObj1 = (JSONObject)aItems.get(0);
				JSONObject tmpObj2 = (JSONObject)aItems.get(1);
				name[0] = tmpObj1.get("name").toString();
				name[1] = tmpObj2.get("name").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return name;
	}

}