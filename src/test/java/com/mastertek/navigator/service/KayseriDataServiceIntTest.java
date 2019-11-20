package com.mastertek.navigator.service;

import com.mastertek.navigator.NavigatorbackendApp;
import com.mastertek.navigator.config.Constants;
import com.mastertek.navigator.domain.City;
import com.mastertek.navigator.domain.District;
import com.mastertek.navigator.domain.User;
import com.mastertek.navigator.repository.CityRepository;
import com.mastertek.navigator.repository.DistrictRepository;
import com.mastertek.navigator.repository.UserRepository;
import com.mastertek.navigator.service.dto.UserDTO;
import com.mastertek.navigator.service.util.RandomUtil;
import com.mastertek.navigator.web.rest.vm.KeyValueDTO;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavigatorbackendApp.class)
@Transactional
public class KayseriDataServiceIntTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KayseriDataServiceService dataServiceService;
    
    @Autowired
    private CityRepository cityRepository;


    @Autowired
    private DistrictRepository districtRepository;

    private User user;


    @Test
    @Transactional
    public void getIlceList() throws Exception {
    	List<KeyValueDTO> list =  dataServiceService.getIlceList();
    	
    	assertThat(list.size()).isEqualTo(16);
    }
    
    @Test
    @Transactional
    public void getMahalleList() throws Exception {
    	List<KeyValueDTO> list =  dataServiceService.getMahalleList("NO=4&AD=MELİKGAZİ");
    	
    	assertThat(list.size()).isEqualTo(58);
    }
    
    @Test
    @Transactional
    public void getSokakList() throws Exception {
    	List<KeyValueDTO> list =  dataServiceService.getSokakList("NO=42098&AD=KIRANARDI%20MAH.");
    	
    	assertThat(list.size()).isEqualTo(79);
    }
    
    @Test
    @Transactional
    public void getBinaList() throws Exception {
    	List<KeyValueDTO> list =  dataServiceService.getBinaList("NO=257690&AD=SEYHAN%20SK.","");
    	
    	assertThat(list.size()).isEqualTo(8);
    }
    
    @Test
    @Transactional
    public void getKapiNo() throws Exception {
    	List<String> result =  dataServiceService.getKapiNo("225962");
    	
    	assertThat(result).isNotNull();
    }
    
    @Test
    @Transactional
    public void genelArama() throws Exception {
    	List<KeyValueDTO> result =  dataServiceService.genelArama("arzu");
    	
    	assertThat(result).isNotNull();
    }

    @Test
    @Transactional
    public void genelAramaByNumber() throws Exception {
    	List<KeyValueDTO> result =  dataServiceService.genelAramaByNumber("13840");
    	
    	assertThat(result).isNotNull();
    }
 
    @Test
    @Transactional
    public void testAll() throws Exception {
    	List<KeyValueDTO> list =  dataServiceService.getIlceList();
    	List<KeyValueDTO> mahalleList =  dataServiceService.getMahalleList(list.get(7).getValue());
    	List<KeyValueDTO> sokakList =  dataServiceService.getSokakList(mahalleList.get(35).getValue());
    	List<KeyValueDTO> binaList =  dataServiceService.getBinaList(sokakList.get(42).getValue(),"");
    	
    	KeyValueDTO dto = binaList.get(1);
    	assertThat(dto.getValue()).isEqualTo("225962");
    	
    	List<String> result =  dataServiceService.getKapiNo(dto.getValue());
    	assertThat(result).isNotNull();
    }
    
    @Test
    @Transactional
    public void migrate() throws Exception {
    	dataServiceService.migrate();
    	List<City> cityList = cityRepository.findAll();
    	assertThat(cityList.size()).isEqualTo(1);
    	assertThat(cityList.get(0).isCompleted()).isTrue();
    
       	List<District> districts = districtRepository.findAll();
    	assertThat(districts.size()).isEqualTo(16);
    	
 
    }
}

