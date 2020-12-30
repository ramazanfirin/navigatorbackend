package com.mastertek.navigator.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastertek.navigator.domain.Building;
import com.mastertek.navigator.domain.City;
import com.mastertek.navigator.domain.District;
import com.mastertek.navigator.domain.Street;
import com.mastertek.navigator.domain.Town;
import com.mastertek.navigator.web.rest.vm.KeyValueDTO;


@Service
@Transactional
public class IstanbulDataService  implements CbsDataService {

    private final Logger log = LoggerFactory.getLogger(IstanbulDataService.class);

    String cityName = "İstanbul";
    
	@Override
	public List<KeyValueDTO> genelAramaByNumber(String dto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KeyValueDTO> genelArama(String string) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getKapiNo(String binaNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KeyValueDTO> getSokakList(String... url1) throws Exception {
		List<KeyValueDTO> resultList = new ArrayList<KeyValueDTO>();
		String url = "http://cbsproxy.ibb.gov.tr/?SehirHaritasiYolListele151&&ilceID="+url1[0]+"&mahalleID="+url1[1]+"&yolAdi=";		
		HttpGet httpPost = new HttpGet(url);
		String result = sendRequest(httpPost);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(result);
		
		JsonNode addressList = (JsonNode) jsonNode.get("AdresList");
		JsonNode addresslet = (JsonNode) addressList.get("Adresler");
		
		JsonNode array = (JsonNode) addresslet.get("Adres");
		for (int i = 0; i < array.size(); i++) {
			JsonNode item = (JsonNode) array.get(i);
			resultList.add(new KeyValueDTO(item.get("ADI").asText(), item.get("ID").asText()));
		}
		
		return resultList;

	}

	@Override
	public List<KeyValueDTO> getMahalleList(String... url1) throws Exception {
		List<KeyValueDTO> resultList = new ArrayList<KeyValueDTO>();
		String url = "http://cbsproxy.ibb.gov.tr/?SehirHaritasiMahalleListele151&&ID="+url1[0];		
		HttpGet httpPost = new HttpGet(url);
		String result = sendRequest(httpPost);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(result);
		
		JsonNode addressList = (JsonNode) jsonNode.get("AdresList");
		JsonNode addresslet = (JsonNode) addressList.get("Adresler");
		
		JsonNode array = (JsonNode) addresslet.get("Adres");
		for (int i = 0; i < array.size(); i++) {
			JsonNode item = (JsonNode) array.get(i);
			resultList.add(new KeyValueDTO(item.get("ADI").asText(), item.get("ID").asText()));
		}
		
		return resultList;
	}

	@Override
	public List<KeyValueDTO> getIlceList() throws Exception {
		List<KeyValueDTO> resultList = new ArrayList<KeyValueDTO>();
		String url = "http://cbsproxy.ibb.gov.tr/?SehirHaritasiIlceListele151&";
		HttpGet httpPost = new HttpGet(url);
		String result = sendRequest(httpPost);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(result);

		JsonNode addressList = (JsonNode) jsonNode.get("AdresList");
		JsonNode addresslet = (JsonNode) addressList.get("Adresler");
		
		JsonNode array = (JsonNode) addresslet.get("Adres");
		for (int i = 0; i < array.size(); i++) {
			JsonNode item = (JsonNode) array.get(i);
			resultList.add(new KeyValueDTO(item.get("ADI").asText(), item.get("ID").asText()));
		}

		return resultList;
	}

	@Override
	public List<KeyValueDTO> getBinaList(String... url1) throws Exception {
		List<KeyValueDTO> resultList = new ArrayList<KeyValueDTO>();
		String url = "http://cbsproxy.ibb.gov.tr/?SehirHaritasiKapiListele151&kapiNo=&yolAdi=&&ilceID="+url1[0]+"&mahalleID="+url1[1]+"&yolid="+URLEncoder.encode(url1[2], "UTF-8");		
		HttpGet httpPost = new HttpGet(url);
		String result = sendRequest(httpPost);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(result);

		JsonNode addressList = (JsonNode) jsonNode.get("AdresList");
		JsonNode addresslet = (JsonNode) addressList.get("Adresler");
		
		if(addresslet==null)
			return resultList;
		JsonNode array = (JsonNode) addresslet.get("Adres");
		
		for (int i = 0; i < array.size(); i++) {
			JsonNode item = (JsonNode) array.get(i);
			KeyValueDTO dto = new KeyValueDTO();
			dto.setLng(item.get("LON").asText());
			dto.setLat(item.get("LAT").asText());
			String name=item.get("ADI").asText();
			String number=item.get("KAPI").asText();
			if(name!=null && !name.equals("null"))
				number= number+"-"+name;
			dto.setKey(number);
			resultList.add(dto);
		}
		return resultList;
	}

	@Override
	public String getCityName() {
		// TODO Auto-generated method stub
		return cityName;
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
				
				building.setLat(dto.getLat());
				building.setLng(dto.getLng());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//buildingRepository.save(building);
		}
		
		return building;
	}

	public String sendRequest(HttpUriRequest request) throws IOException  {
		CloseableHttpClient client = HttpClients.createDefault();
	    CloseableHttpResponse response = client.execute(request);
	    if(response.getStatusLine().getStatusCode()!=200 && response.getStatusLine().getStatusCode()!=201) {
	    	String content = EntityUtils.toString(response.getEntity());
	    	throw new RuntimeException("service error");
	    }
	    String content = EntityUtils.toString(response.getEntity());
	    client.close();
	    return content;
	}

	
//public void insert() throws Exception {
//		
//		
//		City city = cityRepository.findOneByName(getCityName());
//    	if(city==null) {
//    		city = new City();
//    		city.setName(getCityName());
//    		city.setCompleted(false);
//    		cityRepository.save(city);
//    	}
//		
//		List<KeyValueDTO> ilceList = getIlceList();
//		for (Iterator iterator = ilceList.iterator(); iterator.hasNext();) {
//			KeyValueDTO ilce = (KeyValueDTO) iterator.next();
//			List<District> districtList = districtRepository.findByDistrictCityId(city.getId());
//			District district = getDistrict(ilce.getKey(), city,districtList);
//			if(district.isCompleted())
//				continue;
//			
//			List<KeyValueDTO> mahalleList = getMahalleList(ilce.getValue());
//			for (Iterator iterator2 = mahalleList.iterator(); iterator2.hasNext();) {
//				KeyValueDTO mahalle = (KeyValueDTO) iterator2.next();
//				List<Town> townList = townRepository.findTownByDistrictId(district.getId());
//				Town town = getTown(mahalle.getKey(), district,townList);
//				if(town.isCompleted())
//					continue;
//				
//				List<KeyValueDTO> sokakList = getSokakList(ilce.getValue(),mahalle.getValue());
//				for (Iterator iterator3 = sokakList.iterator(); iterator3.hasNext();) {
//					KeyValueDTO sokak = (KeyValueDTO) iterator3.next();
//					List<Street> streetList = streetRepository.findStreetByTownId(town.getId());
//					Street street = getStreet(sokak.getKey(), town,streetList);
//					if(street.isCompleted())
//						continue;
//					
//					List<KeyValueDTO> binaList;
//					try {
//						binaList = getBinaList(ilce.getValue(),mahalle.getValue(),sokak.getValue(),"");
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						continue;
//					}
//					List<Building> insertList = new ArrayList<Building>();
//					List<Building> buildingList = buildingRepository.findBuildingByStreetId(town.getId());
//					
//					for (Iterator iterator4 = binaList.iterator(); iterator4.hasNext();) {
//						KeyValueDTO keyValueDTO = (KeyValueDTO) iterator4.next();
//						Building building = getBuilding(keyValueDTO, street,buildingList);
//						insertList.add(building);
//					}
//					buildingRepository.save(insertList);
//					street.setCompleted(true);
//					streetRepository.save(street);
//				}
//				town.setCompleted(true);
//				townRepository.save(town);
//			}
//			district.setCompleted(true);
//			districtRepository.save(district);
//		}
//		
//		city.setCompleted(true);
//    	cityRepository.save(city);
//	}
}
