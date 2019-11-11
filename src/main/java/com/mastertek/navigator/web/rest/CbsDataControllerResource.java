package com.mastertek.navigator.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mastertek.navigator.service.KayseriDataServiceService;
import com.mastertek.navigator.web.rest.vm.KeyValueDTO;

/**
 * CbsDataController controller
 */
@RestController
@RequestMapping("/api/cbs-data-controller")
public class CbsDataControllerResource {

	private final KayseriDataServiceService kayseriDataServiceService;
	
    private final Logger log = LoggerFactory.getLogger(CbsDataControllerResource.class);

    
    
    public CbsDataControllerResource(KayseriDataServiceService kayseriDataServiceService) {
		super();
		this.kayseriDataServiceService = kayseriDataServiceService;
	}

	/**
    * GET defaultAction
    */
    @GetMapping("/default-action")
    public String defaultAction() {
        return "defaultAction";
    }

    @GetMapping("/getIlceList")
    public List<KeyValueDTO> getIlceList() throws Exception {
        return kayseriDataServiceService.getIlceList();
    }
    
    @GetMapping("/getMahalleList/{param1}")
    public List<KeyValueDTO> getMahalleList(@PathVariable String param1) throws Exception {
        return kayseriDataServiceService.getMahalleList(param1);
    }

    @GetMapping("/getSokakList/{param1}")
    public List<KeyValueDTO> getSokakList(@PathVariable String param1) throws Exception {
        return kayseriDataServiceService.getSokakList(param1);
    }
    
    @GetMapping("/getBinaList/{param1}")
    public List<KeyValueDTO> getBinaList(@PathVariable String param1) throws Exception {
        return kayseriDataServiceService.getBinaList(param1, "");
    }
    
    @GetMapping("/getCoordinate/{param1}")
    public List<KeyValueDTO> getCoordinate(@PathVariable String param1) throws Exception {
    	List<KeyValueDTO> result = new ArrayList<KeyValueDTO>();
    	List<String> list =kayseriDataServiceService.getKapiNo(param1);
    	result.add(new KeyValueDTO("lat",list.get(0)));
    	result.add(new KeyValueDTO("lng",list.get(1)));
    	
	
    	return result;
    }
}
