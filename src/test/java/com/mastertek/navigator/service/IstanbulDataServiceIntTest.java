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
public class IstanbulDataServiceIntTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IstanbulDataService dataServiceService;
    
    @Autowired
    private CityRepository cityRepository;


    @Autowired
    private DistrictRepository districtRepository;

    private User user;


    @Test
    @Transactional
    public void getIlceList() throws Exception {
    	List<KeyValueDTO> list =  dataServiceService.getIlceList();
    	
    	assertThat(list.size()).isEqualTo(39);
    }
    
    @Test
    @Transactional
    public void getMahalleList() throws Exception {
    	String url = "2052"; //çekmekoy
    	List<KeyValueDTO> list =  dataServiceService.getMahalleList(url);
    	
    	assertThat(list.size()).isEqualTo(21);
    }
    
    @Test
    @Transactional
    public void getSokakList() throws Exception {
    	String url1 = "2052"; //çekmekoy
    	String url2= "40769";//cumhuriyert mah
    	List<KeyValueDTO> list =  dataServiceService.getSokakList(url1,url2);
    	
    	assertThat(list.size()).isEqualTo(70);
    }
    
    @Test
    @Transactional
    public void getBinaList() throws Exception {
    	String url1 = "2052"; //çekmekoy
    	String url2= "40769";//cumhuriyert mah
    	String url3= "1109218"; // şadırvan sokak
    	List<KeyValueDTO> list =  dataServiceService.getBinaList(url1,url2,url3);
    	
    	assertThat(list.size()).isEqualTo(23);
    }
    
    @Test
    @Transactional
    public void getKapiNo() throws Exception {
    	List<String> result =  dataServiceService.getKapiNo("225962","","");
    	
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
    	List<KeyValueDTO> mahalleList =  dataServiceService.getMahalleList(list.get(15).getValue());
    	List<KeyValueDTO> sokakList =  dataServiceService.getSokakList(list.get(15).getValue(),mahalleList.get(2).getValue());
    	List<KeyValueDTO> binaList =  dataServiceService.getBinaList(list.get(15).getValue(),mahalleList.get(2).getValue(),sokakList.get(58).getValue(),"");
    	
    	KeyValueDTO dto = binaList.get(1);
    	assertThat(dto.getKey()).contains("TAŞDELEN");
    	
//    	List<String> result =  dataServiceService.getKapiNo(dto.getValue());
//    	assertThat(result).isNotNull();
    }
    
    @Test
    @Transactional
    public void migrate() throws Exception {
    	//dataServiceService.migrate();
    	List<City> cityList = cityRepository.findAll();
    	assertThat(cityList.size()).isEqualTo(1);
    	assertThat(cityList.get(0).isCompleted()).isTrue();
    
       	List<District> districts = districtRepository.findAll();
    	assertThat(districts.size()).isEqualTo(16);
    	
 
    }
}

