package com.mastertek.navigator.web.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mastertek.navigator.domain.City;
import com.mastertek.navigator.repository.CityRepository;
import com.mastertek.navigator.service.CbsDataService;
import com.mastertek.navigator.service.IstanbulDataService;
import com.mastertek.navigator.service.KayseriDataServiceService;
import com.mastertek.navigator.web.rest.vm.KeyValueDTO;

/**
 * CbsDataController controller
 */
@RestController
@RequestMapping("/api/cbs-data-controller")
public class CbsDataControllerResource {

	private final KayseriDataServiceService kayseriDataServiceService;
	
	private final IstanbulDataService istanbulDataService;;
	
	private final CityRepository cityRepository;
	
    private final Logger log = LoggerFactory.getLogger(CbsDataControllerResource.class);

    
    
    public CbsDataControllerResource(KayseriDataServiceService kayseriDataServiceService,IstanbulDataService istanbulDataService,CityRepository cityRepository) {
		super();
		this.kayseriDataServiceService = kayseriDataServiceService;
		this.istanbulDataService = istanbulDataService;
		this.cityRepository = cityRepository;
	}

	/**
    * GET defaultAction
    */
    @GetMapping("/default-action")
    public String defaultAction() {
        return "defaultAction";
    }

    @GetMapping("/getIlList")
    public List<KeyValueDTO> getIlList() throws Exception {
    	List<KeyValueDTO> result = new ArrayList<KeyValueDTO>();
    	
//    	List<City> cityList = cityRepository.findAll();
//    	for (Iterator iterator = cityList.iterator(); iterator.hasNext();) {
//			City city = (City) iterator.next();
//			KeyValueDTO dto = new KeyValueDTO(city.getName(), city.getName());
//			result.add(dto);
//		}
    	result.add(new KeyValueDTO("Seçiniz", ""));
    	result.add(new KeyValueDTO("KAYSERİ", "38"));
    	result.add(new KeyValueDTO("İSTANBUL", "34"));
    	return result;
    }
    
    @GetMapping("/getIlceList/{city}")
    public List<KeyValueDTO> getIlceList(@PathVariable String city) throws Exception {
    	CbsDataService proxy = getProxy(city);
    	return proxy.getIlceList();
    }
    
    @GetMapping("/getMahalleList/{param1}/{city}")
    public List<KeyValueDTO> getMahalleList(@PathVariable String param1,@PathVariable String city) throws Exception {
    	CbsDataService proxy = getProxy(city);
    	return proxy.getMahalleList(param1);
    }

    @GetMapping("/getSokakList/{param1}/{city}")
    public List<KeyValueDTO> getSokakList(@PathVariable String param1,@PathVariable String city) throws Exception {
    	CbsDataService proxy = getProxy(city);
    	return proxy.getSokakList(param1);
    }
    
    @GetMapping("/getBinaList/{param1}/{city}")
    public List<KeyValueDTO> getBinaList(@PathVariable String param1,@PathVariable String city) throws Exception {
    	CbsDataService proxy = getProxy(city);
        return proxy.getBinaList(param1, "");
    }
    
    @GetMapping("/getCoordinate/{param1}/{city}")
    public List<KeyValueDTO> getCoordinate(@PathVariable String param1,@PathVariable String city) throws Exception {
    	CbsDataService proxy = getProxy(city);
    	
    	List<KeyValueDTO> result = new ArrayList<KeyValueDTO>();
    	List<String> list =proxy.getKapiNo(param1);
    	result.add(new KeyValueDTO("lat",list.get(0)));
    	result.add(new KeyValueDTO("lng",list.get(1)));
    	
	
    	return result;
    }
    
    @GetMapping("/search/{param1}")
    public List<KeyValueDTO> search(@PathVariable String param1) throws Exception {
        return kayseriDataServiceService.genelArama(param1);
    }
    
    public CbsDataService getProxy(String city) {
    	if(city==null || city.equals(""))
    		return kayseriDataServiceService;
    	if(city.equals("38"))
    		return kayseriDataServiceService;
    	if(city.equals("34"))
    		return istanbulDataService;
    	return null;
    	
    }
}
