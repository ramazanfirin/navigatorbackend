package com.mastertek.navigator.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

public abstract class DataService {

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
	
	public abstract  List<String> getKapiNo(String binaNo) throws Exception;
	
	public abstract List<KeyValueDTO> getSokakList(String... url1) throws Exception;
	
	public abstract List<KeyValueDTO> getMahalleList(String... url1) throws Exception;
	
	public abstract List<KeyValueDTO> getIlceList() throws Exception;
	
	public abstract List<KeyValueDTO> getBinaList(String... url1) throws Exception;
	
	public abstract String getCityName() ;
	
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
	
public void insert() throws Exception {
		
		
		City city = cityRepository.findOneByName(getCityName());
    	if(city==null) {
    		city = new City();
    		city.setName(getCityName());
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
	
	public void migrate() throws Exception {
		insert();

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

}
