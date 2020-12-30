package com.mastertek.navigator.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mastertek.navigator.service.IstanbulDataService;
import com.mastertek.navigator.service.KayseriDataServiceService;

/**
 * Import controller
 */
@RestController
@RequestMapping("/api/import")
public class ImportResource {

    private final Logger log = LoggerFactory.getLogger(ImportResource.class);

    
    private final KayseriDataServiceService kayseriDataServiceService;
    
    private final IstanbulDataService istanbulDataService;
    
    
    
    public ImportResource(KayseriDataServiceService kayseriDataServiceService,IstanbulDataService istanbulDataService) {
		super();
		this.kayseriDataServiceService = kayseriDataServiceService;
		this.istanbulDataService = istanbulDataService;
	}



	/**
    * GET importKayseri
	 * @throws Exception 
    */
    @GetMapping("/import-kayseri")
    public String importKayseri() throws Exception {
        //kayseriDataServiceService.migrate();
    	//istanbulDataService.migrate();
    	return "importKayseri";
    }

}
