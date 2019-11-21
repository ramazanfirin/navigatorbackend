package com.mastertek.navigator.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mastertek.navigator.domain.Building;
import com.mastertek.navigator.domain.City;
import com.mastertek.navigator.domain.District;
import com.mastertek.navigator.domain.Street;
import com.mastertek.navigator.domain.Town;
import com.mastertek.navigator.repository.BuildingRepository;
import com.mastertek.navigator.repository.CityRepository;
import com.mastertek.navigator.repository.DistrictRepository;
import com.mastertek.navigator.repository.StreetRepository;
import com.mastertek.navigator.repository.TownRepository;
import com.mastertek.navigator.web.rest.vm.KeyValueDTO;


@Service
public class KayseriDataServiceService implements CbsDataService{

	
	@Autowired
	CityRepository cityRepository;
	
	@Autowired
	DistrictRepository districtRepository;
	
	@Autowired
	TownRepository townRepository;
	
	@Autowired
	StreetRepository streetRepository;
	
	@Autowired
	BuildingRepository buildingRepository;
	
    private final Logger log = LoggerFactory.getLogger(KayseriDataServiceService.class);

    String cityName = "KAYSERİ";
    
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
				onclik = onclik.replace("JSKapiNumarasiCiz", "");
				
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

	
	
	public void insert() throws Exception {
		
		
		City city = cityRepository.findOneByName(cityName);
    	if(city==null) {
    		city = new City();
    		city.setName(cityName);
    		city.setCompleted(false);
    		cityRepository.save(city);
    	}
		
		List<KeyValueDTO> ilceList = getIlceList();
		for (Iterator iterator = ilceList.iterator(); iterator.hasNext();) {
			KeyValueDTO ilce = (KeyValueDTO) iterator.next();
			List<District> districtList = districtRepository.findByDistrictCityId(city.getId());
			District district = getDistrict(ilce.getKey(), city,districtList);
			if(district.isCompleted())
				continue;
			
			List<KeyValueDTO> mahalleList = getMahalleList(ilce.getValue());
			for (Iterator iterator2 = mahalleList.iterator(); iterator2.hasNext();) {
				KeyValueDTO mahalle = (KeyValueDTO) iterator2.next();
				List<Town> townList = townRepository.findTownByDistrictId(district.getId());
				Town town = getTown(mahalle.getKey(), district,townList);
				if(town.isCompleted())
					continue;
				
				List<KeyValueDTO> sokakList = getSokakList(mahalle.getValue());
				for (Iterator iterator3 = sokakList.iterator(); iterator3.hasNext();) {
					KeyValueDTO sokak = (KeyValueDTO) iterator3.next();
					List<Street> streetList = streetRepository.findStreetByTownId(town.getId());
					Street street = getStreet(sokak.getKey(), town,streetList);
					if(street.isCompleted())
						continue;
					
					List<KeyValueDTO> binaList;
					try {
						binaList = getBinaList(sokak.getValue(),"");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
					for (Iterator iterator4 = binaList.iterator(); iterator4.hasNext();) {
						KeyValueDTO keyValueDTO = (KeyValueDTO) iterator4.next();
						List<Building> buildingList = buildingRepository.findBuildingByStreetId(town.getId());
						Building building = getBuilding(keyValueDTO, street,buildingList);
					}
					street.setCompleted(true);
					streetRepository.save(street);
				}
				town.setCompleted(true);
				townRepository.save(town);
			}
			district.setCompleted(true);
			districtRepository.save(district);
		}
		
		city.setCompleted(true);
    	cityRepository.save(city);
	}
	
	public void insertBina(List<KeyValueDTO> binaList,KeyValueDTO sokak) {
		
	}
	
	public District getDistrict(String name,City city,List<District> list) {
		District district = null;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			District districtTemp = (District) iterator.next();
			if(districtTemp.getName().equals(name)) {
				district = districtTemp;
				break;
			}
				
		}
		
		if(district == null) {
			district = new District();
			district.setName(name);
			district.setCity(city);
			district.setCompleted(false);
			districtRepository.save(district);
		}
		
		return district;
	}
	
	public Town getTown(String name,District ilce,List<Town> list) {
		Town town = null;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Town townTemp = (Town) iterator.next();
			if(townTemp.getName().equals(name)) {
				town = townTemp;
				break;
			}
		}
	
		if(town == null) {
			town = new Town();
			town.setName(name);
			town.setDistrict(ilce);;
			town.setCompleted(false);
			townRepository.save(town);
		}
		
		return town;
	}
	
	public Street getStreet(String name,Town mahalle,List<Street> list) {
		Street street = null;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Street streetTemp = (Street) iterator.next();
			if(streetTemp.getName().equals(name)) {
				street = streetTemp;
				break;
			}
		}
		if(street == null) {
			street = new Street();
			street.setName(name);
			street.setTown(mahalle);
			street.setCompleted(false);
			streetRepository.save(street);
		}
		
		return street;
	}
	
	public Building getBuilding(KeyValueDTO dto,Street street,List<Building> list) throws Exception {
		String key = dto.getKey();
		String[] values = key.split("-");
		
		String number="";
		String name = "";
		if (values.length>0) {
			number = key.split("-")[0];
		}
		if (values.length>1) {
			name = key.split("-")[1];
		}
		
		Building building = null;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Building buildingTemp = (Building) iterator.next();
			if(buildingTemp.getName().equals(name) && buildingTemp.getNumber().equals(number)) {
				building = buildingTemp;
				break;
			}
		}
		
		if(building == null) {
			building = new Building();
			building.setName(name);
			building.setNumber(number);
			building.setStreet(street);	
			try {
				List<String> coords = getKapiNo(dto.getValue());
				building.setLat(coords.get(0));
				building.setLng(coords.get(1));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			buildingRepository.save(building);
		}
		
		return building;
	}
	
	public void migrate() throws Exception {
		insert();
	
	}
	
	
	//insert code
	/*
	City city = cityRepository.findOneByName(cityName);
		
		List<KeyValueDTO> ilceList = getIlceList();
		for (Iterator iterator = ilceList.iterator(); iterator.hasNext();) {
			KeyValueDTO ilce = (KeyValueDTO) iterator.next();
			
			List<KeyValueDTO> mahalleList = getMahalleList(ilce.getValue());
			for (Iterator iterator2 = mahalleList.iterator(); iterator2.hasNext();) {
				KeyValueDTO mahalle = (KeyValueDTO) iterator2.next();
				
				List<KeyValueDTO> sokakList = getSokakList(mahalle.getValue());
				for (Iterator iterator3 = sokakList.iterator(); iterator3.hasNext();) {
					KeyValueDTO sokak = (KeyValueDTO) iterator3.next();
					List<KeyValueDTO> binaList = getBinaList(sokak.getValue(),"");
					
				}
			}
		}
		*/ 
	
}
