package com.mastertek.navigator.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mastertek.navigator.web.rest.vm.KeyValueDTO;


@Service
public class KayseriDataServiceService implements CbsDataService{

    private final Logger log = LoggerFactory.getLogger(KayseriDataServiceService.class);

	@Override
	public List<KeyValueDTO> getIlceList() throws Exception {
		String url = "http://cbs.kayseri.bel.tr/KIlce.aspx";
		url ="http://cbs.kayseri.bel.tr/GenelSayfalar/AdresBilesenleri/KIlce.aspx";
		
		HttpGet httpPost = new HttpGet(url);
		String result = sendRequest(httpPost);
		
		List  <KeyValueDTO> selectItemList = new ArrayList<KeyValueDTO>();
			Document doc = Jsoup.parse(result);
			Elements impress = doc.getElementsByClass("imgcursor");
			
			
			for (Iterator iterator = impress.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();

				String ilce  = new String(element.attr("title"));
				String onclick = element.attr("onclick");
				onclick = onclick.replace("JsMahGoster", "");
				onclick = onclick.replace("(", "");
				onclick = onclick.replace(")", "");
				onclick = onclick.replace("this.id,", "");
				onclick = onclick.replace(",this.title", "");
				onclick = onclick.replace(";", "");
			    
				KeyValueDTO dto = new KeyValueDTO(ilce, "NO="+onclick+"&AD="+ilce);
				selectItemList.add(dto);
			}
	
			return selectItemList;
	}

	@Override
	public List<KeyValueDTO> getMahalleList(String url1) throws Exception {
		String urlEncoded = URLEncoder.encode(url1, "UTF-8");
		String url = "http://cbs.kayseri.bel.tr/KMahalle.aspx?"+url1;
		url="http://cbs.kayseri.bel.tr/GenelSayfalar/AdresBilesenleri/KMahalle.aspx?"+url1;
		List  <KeyValueDTO> selectItemList = new ArrayList<KeyValueDTO>();
	
		HttpGet httpPost = new HttpGet(url);
		
		String result = sendRequest(httpPost);
			
			Document doc = Jsoup.parse(result);
			Elements impress = doc.getElementsByAttributeValue("style", "border-width: 0px");
			for (Iterator iterator = impress.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				String mahalle  = new String(element.attr("title"));
				
				
			
				String onclick = element.attr("onclick");
				onclick = onclick.replace("JScsbmGetir", "");
				onclick = onclick.replace("(", "");
				onclick = onclick.replace(")", "");
				onclick = onclick.replace("this.id,", "");
				onclick = onclick.replace(",this.title", "");
				onclick = onclick.replace(";", "");
				
				KeyValueDTO dto = new KeyValueDTO(mahalle, "NO="+onclick+"&AD="+mahalle);
				selectItemList.add(dto);
			}
			
	
			
		return selectItemList;
	}

	@Override
	public List<KeyValueDTO> getSokakList(String url1) throws Exception {
		String urlEncoded = URLEncoder.encode(url1, "UTF-8");
		String url = "http://cbs.kayseri.bel.tr/Kcsbm.aspx?"+url1;
		url="http://cbs.kayseri.bel.tr/GenelSayfalar/AdresBilesenleri/Kcsbm.aspx?"+url1.replace(" ", "%20");
		List  <KeyValueDTO> selectItemList = new ArrayList<KeyValueDTO>();
	
		HttpGet httpPost = new HttpGet(url);
			
			String result = sendRequest(httpPost);
			
			Document doc = Jsoup.parse(result);
			Elements impress = doc.getElementsByClass("cursorhand");
			for (Iterator iterator = impress.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				String sokak  = new String(element.attr("title"));
				
				
			
				String onclick = element.attr("onclick");
				onclick = onclick.replace("JSKapiGetir", "");
				onclick = onclick.replace("(", "");
				onclick = onclick.replace(")", "");
				onclick = onclick.replace("this.id,", "");
				onclick = onclick.replace(",this.title", "");
				onclick = onclick.replace(";", "");
				
				if(!sokak.equals("") && !(sokak==null)){
					KeyValueDTO dto = new KeyValueDTO(sokak, "NO="+onclick+"&AD="+sokak);
					selectItemList.add(dto);
				}
			}
		
		
		return selectItemList;
	}

	@Override
	public List<KeyValueDTO> getBinaList(String url1, String mahalleId) throws Exception {
		String urlEncoded = URLEncoder.encode(url1, "UTF-8");
		String url = "http://cbs.kayseri.bel.tr/KBina.aspx?"+url1;
		url="http://cbs.kayseri.bel.tr/GenelSayfalar/AdresBilesenleri/KBina.aspx?"+url1.replace(" ", "%20");
		List  <KeyValueDTO> selectItemList = new ArrayList<KeyValueDTO>();
			HttpGet httpPost = new HttpGet(url);
			
			String result = sendRequest(httpPost);
			 result=new String(result.getBytes("UTF-8"), "UTF-8");
			Document doc = Jsoup.parse(result);
			Elements impress = doc.getElementsByClass("trAlternative");
			Elements impress2 = doc.getElementsByClass("trNormal");
			impress.addAll(impress2);
			
			for (Iterator iterator = impress.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				Elements elementList  =element.getElementsByAttributeValue("align", "left");
				
				//System.out.println(elementList.get(0).html());
				
				Elements onclickList =elementList.get(3).getElementsByAttribute("onclick");
				if(onclickList.size()==0)
					continue ;
							
				String onclik=onclickList.get(0).attr("onclick");
				
				onclik = onclik.replace("JSBinaCiz", "");
				onclik = onclik.replace("(", "");
				onclik = onclik.replace(")", "");
				onclik = onclik.replace("\"", "");
				
				String bina = elementList.get(0).html();
				if(bina.equals("96"))
					System.out.println("geldi");
				String acs  = elementList.get(2).html();
				String newString ="";
				
				newString = StringEscapeUtils.unescapeHtml4(acs);
				//newString = StringEscapeUtils.escapeHtml4(newString);
				newString = new String(newString);
				bina=bina+"-"+newString;
				
			

				KeyValueDTO dto = new KeyValueDTO(bina,onclik);
				
				//SelectItem item = new SelectItem(onclik,bina);
				selectItemList.add(dto);
			
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
	public List<String> getKapiNo(String binaNo) throws Exception {
		List<String> returnList= new ArrayList<String>();
		
		String url = "https://cbs.kayseri.bel.tr/Rehber.aspx/GetKapiBina";

		HttpPost httpPost = new HttpPost(url);
		StringEntity input = new StringEntity("{\"binaNO\":"+binaNo+"}");
		httpPost.setEntity(input);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		String result = sendRequest(httpPost);
		
		
		String returnValue="";
		returnValue = result.toString().replace("{\"d\":{\"__type\":\"HaritaCografiArama+sobje\",\"deger\":\"", "");
		returnValue = returnValue.replace("\"}}", "");
		returnValue = returnValue.replace("||", "&");
		returnValue = returnValue.replace("|", "@");
		
		String[] values= returnValue.split("&");
		
		for (int i = 0; i < values.length; i++) {
			String[] temp  =values[i].split("@");
			returnList.add(temp[0]);
			returnList.add(temp[1]);
		}
		
		return returnList;
	}
	
	
	private String sendRequest(HttpUriRequest request) throws IOException  {
		CloseableHttpClient client = HttpClients.createDefault();
	    CloseableHttpResponse response = client.execute(request);
	    if(response.getStatusLine().getStatusCode()!=200 && response.getStatusLine().getStatusCode()!=201) {
	    	String content = EntityUtils.toString(response.getEntity());
	    	log.error("error content:"+content);
	    	throw new RuntimeException("service error");
	    }
	    String content = EntityUtils.toString(response.getEntity());
	    client.close();
	    return content;
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

}
