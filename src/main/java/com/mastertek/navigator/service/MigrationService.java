package com.mastertek.navigator.service;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mastertek.navigator.domain.City;
import com.mastertek.navigator.domain.District;
import com.mastertek.navigator.repository.CityRepository;
import com.mastertek.navigator.repository.DistrictRepository;
import com.mastertek.navigator.web.rest.vm.KeyValueDTO;

@Service
@Transactional
public class MigrationService {

	@Autowired
	KayseriDataServiceService kayseriDataService;
	
	@Autowired
	DistrictRepository districtRepository;
	
	@Autowired
	CityRepository cityRepository;
	
    private final Logger log = LoggerFactory.getLogger(MigrationService.class);

    
    public void migrate() throws Exception {
    	kayseriDataService.migrate();
    }
}
