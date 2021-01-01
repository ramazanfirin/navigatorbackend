package com.mastertek.navigator.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mastertek.navigator.web.rest.vm.KeyValueDTO;


@Service
public class İzmirDataServiceService extends DataService implements CbsDataService {
	private final Logger log = LoggerFactory.getLogger(İzmirDataServiceService.class);

    String cityName = "İZMİR";
    
    @Autowired
    ObjectMapper objectMapper;
    
	@Override
	public List<KeyValueDTO> getIlceList() throws Exception {

		List  <KeyValueDTO> selectItemList = new ArrayList<KeyValueDTO>();
		selectItemList.add(new KeyValueDTO("3", "ALİAĞA"));
		selectItemList.add(new KeyValueDTO("14", "BALÇOVA"));
		selectItemList.add(new KeyValueDTO("7", "BAYINDIR"));
		selectItemList.add(new KeyValueDTO("11", "BAYRAKLI"));
		selectItemList.add(new KeyValueDTO("999", "BERGAMA"));
		selectItemList.add(new KeyValueDTO("993", "BEYDAĞ"));
		
		selectItemList.add(new KeyValueDTO("2", "BORNOVA"));
		selectItemList.add(new KeyValueDTO("8", "BUCA"));
		selectItemList.add(new KeyValueDTO("1001", "ÇEŞME"));
		selectItemList.add(new KeyValueDTO("16", "ÇİĞLİ"));
		selectItemList.add(new KeyValueDTO("995", "DİKİLİ"));
		selectItemList.add(new KeyValueDTO("4", "FOçA"));
		selectItemList.add(new KeyValueDTO("15", "GAZİEMİR"));
		
		selectItemList.add(new KeyValueDTO("1", "GÜZELBAHÇE"));
		selectItemList.add(new KeyValueDTO("19", "KARABAĞLAR"));
		selectItemList.add(new KeyValueDTO("1000", "KARABURUN"));
		selectItemList.add(new KeyValueDTO("20", "KARŞIYAKA"));
		selectItemList.add(new KeyValueDTO("6", "KEMALPAŞA"));
		
		selectItemList.add(new KeyValueDTO("996", "KINIK"));
		selectItemList.add(new KeyValueDTO("994", "KİRAZ"));
		selectItemList.add(new KeyValueDTO("21", "KONAK"));
		selectItemList.add(new KeyValueDTO("13", "MENDERES"));
		selectItemList.add(new KeyValueDTO("17", "MENEMEN"));
		selectItemList.add(new KeyValueDTO("5", "NARLIDERE"));
		
		selectItemList.add(new KeyValueDTO("998", "ÖDEDEMİŞ"));
		selectItemList.add(new KeyValueDTO("12", "SEFERİHİSAR"));
		selectItemList.add(new KeyValueDTO("10", "SELÇUK"));
		selectItemList.add(new KeyValueDTO("997", "TİRE"));
		selectItemList.add(new KeyValueDTO("9", "TORBALI"));
		selectItemList.add(new KeyValueDTO("18", "URLA"));
		
		return selectItemList;
	}

	@Override
	public List<KeyValueDTO> getMahalleList(String... url1) throws Exception {
		String url = "http://cbs.kayseri.bel.tr/KIlce.aspx";
		url ="https://kentrehberi.izmir.bel.tr/izmirkentrehberi/Home/MahalleGetir";
		
		HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addTextBody("ilce_id",url1[0]);
		
		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);
		
		List  <KeyValueDTO> selectItemList = new ArrayList<KeyValueDTO>();
		
		String result = sendRequest(httpPost);
		ArrayNode faces = (ArrayNode) objectMapper.readTree(result);
		for (int i = 0; i < faces.size(); i++) {
			JsonNode rectangle = faces.get(i);
			String key = rectangle.get("Value").asText();
			String value= rectangle.get("Text").asText();
			KeyValueDTO keyValueDTO = new KeyValueDTO(key, value);
			selectItemList.add(keyValueDTO);
			
		}
		return selectItemList;


	}

	@Override
	public List<KeyValueDTO> getSokakList(String... url1) throws Exception {
		String url = "http://cbs.kayseri.bel.tr/KIlce.aspx";
		url ="https://kentrehberi.izmir.bel.tr/izmirkentrehberi/Home/SokakGetir";
		
		HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addTextBody("mahalle_id",url1[0]);
		
		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);
		
		List  <KeyValueDTO> selectItemList = new ArrayList<KeyValueDTO>();
		
		String result = sendRequest(httpPost);
		ArrayNode faces = (ArrayNode) objectMapper.readTree(result);
		for (int i = 0; i < faces.size(); i++) {
			JsonNode rectangle = faces.get(i);
			String key = rectangle.get("Value").asText();
			String value= rectangle.get("Text").asText();
			KeyValueDTO keyValueDTO = new KeyValueDTO(key, value);
			selectItemList.add(keyValueDTO);
			
		}
		return selectItemList;

	}

	@Override
	public List<KeyValueDTO> getBinaList(String... url1) throws Exception {
		String url = "http://cbs.kayseri.bel.tr/KIlce.aspx";
		url ="https://kentrehberi.izmir.bel.tr/izmirkentrehberi/Home/KapiNoGetir";
		
		HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addTextBody("mahalle_id",url1[0]);
		builder.addTextBody("yolKod",url1[1]);
		
		
		
		HttpEntity entity = builder.build();
		httpPost.setEntity(entity);
		
		List  <KeyValueDTO> selectItemList = new ArrayList<KeyValueDTO>();
		
		String result = sendRequest(httpPost);
		ArrayNode faces = (ArrayNode) objectMapper.readTree(result);
		for (int i = 0; i < faces.size(); i++) {
			JsonNode rectangle = faces.get(i);
			String key = rectangle.get("Value").asText();
			String value= rectangle.get("Text").asText();
			KeyValueDTO keyValueDTO = new KeyValueDTO(key, value);
			selectItemList.add(keyValueDTO);
			
		}
		return selectItemList;
	}		

	@Override
	public List<KeyValueDTO> genelAramaByNumber(String string) throws Exception {
List<KeyValueDTO> returnList= new ArrayList<KeyValueDTO>();
		
		String url = "https://cbs.kayseri.bel.tr/Rehber.aspx/JSGenelAramaByNumber";
		//url = "http://localhost/Rehber.aspx/JSGenelArama";
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		
//		String query = "{\"number\":"+string+"}";
//		StringEntity input = new StringEntity(query,"UTF-8");
//		input.setContentType("application/json; charset=UTF-8");
//		post.setEntity(input);
//		
//		post.setHeader("Accept", "*/*");
//		post.setHeader("Accept-Encoding", "gzip,deflate");
//		post.setHeader("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.6,en;q=0.4");
//		post.setHeader("Connection", "keep-alive");
//		post.setHeader("Content-Type", "application/json; charset=UTF-8");
//		post.setHeader("Host", "cbs.kayseri.bel.tr");
//		post.setHeader("X-Requested-With", "XMLHttpRequest");
//		post.setHeader("Referer", "http://cbs.kayseri.bel.tr/Rehber.aspx");
		
		
		HttpPost httpPost = new HttpPost(url);
		String query = "{\"number\":"+string+",tip:0}";
		StringEntity input = new StringEntity(query,"UTF-8");
		httpPost.setEntity(input);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		String result = sendRequest(httpPost);
		
		
		
		String returnValue=result.toString();
		
		returnValue = returnValue.replace("|", "&");
		returnValue = returnValue.replace("\"}", "");
		String[] values= returnValue.split("&");
		String a  =values[0];
		a=a.replace("{\"d\":\"", "");
		String b = values[1];
		
		returnList.add(new KeyValueDTO("lat", a));
		returnList.add(new KeyValueDTO("lng", b));	
		
		for (int i = 2; i < values.length; i++) {
			returnList.add(new KeyValueDTO("", values[i]));
			
			
			
			//returnList.add(new KeyValueDTO(a[1], a[2]));
			
		}
		
		return returnList;
	}

	@Override
	public List<KeyValueDTO> genelArama(String string) throws Exception {
		List<KeyValueDTO> returnList= new ArrayList<KeyValueDTO>();
		
		String url = "https://cbs.kayseri.bel.tr/Rehber.aspx/JSGenelArama";

		HttpPost httpPost = new HttpPost(url);
		String query = "{\"val\":\""+string+"\"}";
		StringEntity input = new StringEntity(query,"UTF-8");
		httpPost.setEntity(input);
//		httpPost.setHeader("Accept", "application/json");
//		httpPost.setHeader("Content-type", "application/json");
		
		httpPost.setHeader("Accept", "*/*");
		httpPost.setHeader("Accept-Encoding", "gzip,deflate");
		httpPost.setHeader("Accept-Language", "tr-TR,tr;q=0.8,en-US;q=0.6,en;q=0.4");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
		httpPost.setHeader("Host", "cbs.kayseri.bel.tr");
		httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
		httpPost.setHeader("Referer", "http://cbs.kayseri.bel.tr/Rehber.aspx");
		
		String resultTemp = sendRequest(httpPost);
		
	
//		byte[] utfString = resultTemp.toString().getBytes("US-ASCII") ;
//		String stringaaa = new String(utfString, "UTF-8");
		
		StringBuffer result = new StringBuffer();
		result = removeUTFCharacters(resultTemp.toString());
		
		String returnValue="";
		returnValue = result.toString().replaceAll("<img src='/assets/global/plugins/leaflet-0.7.2/images/marker-icon.png' style='border-width:0; width:15px;' />", "");
		returnValue = returnValue.replaceAll("class='linknone'", "");
		returnValue = returnValue.replaceAll("a href='javascript:JSAramaGetirByNumber","");
		returnValue = returnValue.replaceAll("></a>|<", "");
		
		returnValue = returnValue.replaceAll("/a>", "");
		returnValue = returnValue.replace("{\"d\":\"", "").replace("\"}","");
		returnValue = returnValue.replace("££", "&");
		
		String[] values= returnValue.split("&");
		
		for (int i = 0; i < values.length; i++) {
			String[] temp  =values[i].split("\'");
			
			String a = temp[0].replace("(", "").replace(")","");
			String b = temp[2].replace(">", "");
			
			returnList.add(new KeyValueDTO(a, b));
			
		}
		
		return returnList;
	}

	@Override
	public List<String> getKapiNo(String binaNo,String lat,String lng) throws Exception {
		String url = "https://cbs.izmir.bel.tr/arcgis/rest/services/CbsRehber/MapServer/9/query?f=json&where=CBSID%3D"+binaNo;
		
		HttpGet httpPost = new HttpGet(url);
				
		
		String result = sendRequest(httpPost);
		JsonNode faces = objectMapper.readTree(result);
		JsonNode features = faces.get("features");
		JsonNode geometry = features.get(0).get("geometry");
		JsonNode latValue = geometry.get("x");
		JsonNode lngValue = geometry.get("y");
		
		System.out.println("s");
		
		List<String> returnList= new ArrayList<String>();
		returnList.add(latValue.asText());
		returnList.add(lngValue.asText());
		return returnList;
	}
	
	
	
	
	public static StringBuffer removeUTFCharacters(String data){
		Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
		Matcher m = p.matcher(data);
		StringBuffer buf = new StringBuffer(data.length());
		while (m.find()) {
		String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
		m.appendReplacement(buf, Matcher.quoteReplacement(ch));
		}
		m.appendTail(buf);
		return buf;
		}
	
	private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

	
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


}
