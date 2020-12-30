package com.mastertek.navigator.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastertek.navigator.NavigatorbackendApp;
import com.mastertek.navigator.repository.CityRepository;
import com.mastertek.navigator.service.IstanbulDataService;
import com.mastertek.navigator.service.KayseriDataServiceService;
import com.mastertek.navigator.web.rest.vm.KeyValueDTO;
/**
 * Test class for the CbsDataController REST controller.
 *
 * @see CbsDataControllerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavigatorbackendApp.class)
public class KayseriDataControllerResourceIntTest {

    private MockMvc restMockMvc;

    @Autowired
    KayseriDataServiceService kayseriDataServiceService;
    
    @Autowired
    IstanbulDataService istanbulDataService;

    @Autowired
    CityRepository cityRepository;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        CbsDataControllerResource cbsDataControllerResource = new CbsDataControllerResource(kayseriDataServiceService,istanbulDataService,cityRepository);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(cbsDataControllerResource)
            .build();
    }

    /**
    * Test defaultAction
    */
    @Test
    public void testDefaultAction() throws Exception {
        restMockMvc.perform(get("/api/cbs-data-controller/default-action"))
            .andExpect(status().isOk());
    }
    
    @Test
    @Transactional
    public void getIlceList() throws Exception {
        
    	MvcResult result = restMockMvc.perform(get("/api/cbs-data-controller/getIlceList/38")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

    	ObjectMapper mapper = new ObjectMapper();
        List<KeyValueDTO> asList = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<KeyValueDTO>>() { });
        assertThat(asList.size()).isEqualTo(16);
    }
    
    @Test
    @Transactional
    public void getMahalleList() throws Exception {
        
    	List<KeyValueDTO> ilceList = kayseriDataServiceService.getIlceList();
    	
    	
    	MvcResult result = restMockMvc.perform(get("/api/cbs-data-controller/getMahalleList/"+ilceList.get(7).getValue())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

    	ObjectMapper mapper = new ObjectMapper();
        List<KeyValueDTO> asList = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<KeyValueDTO>>() { });
        assertThat(asList.size()).isEqualTo(58);
    }

    
    @Test
    @Transactional
    public void getSokakList() throws Exception {
        
    	List<KeyValueDTO> ilceList = kayseriDataServiceService.getIlceList();
    	List<KeyValueDTO> mahalleList =  kayseriDataServiceService.getMahalleList(ilceList.get(7).getValue());
    	
    	MvcResult result = restMockMvc.perform(get("/api/cbs-data-controller/getSokakList/"+mahalleList.get(35).getValue())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

    	ObjectMapper mapper = new ObjectMapper();
        List<KeyValueDTO> asList = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<KeyValueDTO>>() { });
        assertThat(asList.size()).isEqualTo(79);
    }
    
    @Test
    @Transactional
    public void getBinaList() throws Exception {
        
    	List<KeyValueDTO> ilceList = kayseriDataServiceService.getIlceList();
    	List<KeyValueDTO> mahalleList =  kayseriDataServiceService.getMahalleList(ilceList.get(7).getValue());
    	List<KeyValueDTO> sokakList =  kayseriDataServiceService.getSokakList(mahalleList.get(35).getValue());
    	
    	
    	MvcResult result = restMockMvc.perform(get("/api/cbs-data-controller/getBinaList/"+sokakList.get(42).getValue())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

    	ObjectMapper mapper = new ObjectMapper();
        List<KeyValueDTO> asList = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<KeyValueDTO>>() { });
        assertThat(asList.size()).isEqualTo(8);
    }
    
    @Test
    @Transactional
    public void getKapiNo() throws Exception {
        
    	List<KeyValueDTO> ilceList = kayseriDataServiceService.getIlceList();
    	List<KeyValueDTO> mahalleList =  kayseriDataServiceService.getMahalleList(ilceList.get(7).getValue());
    	List<KeyValueDTO> sokakList =  kayseriDataServiceService.getSokakList(mahalleList.get(35).getValue());
    	List<KeyValueDTO> binaList =  kayseriDataServiceService.getBinaList(sokakList.get(42).getValue(),"");
    	
    	MvcResult result = restMockMvc.perform(get("/api/cbs-data-controller/getCoordinate/"+binaList.get(1).getValue())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

    	ObjectMapper mapper = new ObjectMapper();
        List<KeyValueDTO> asList = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<KeyValueDTO>>() { });
        assertThat(asList.size()).isEqualTo(8);
    }
}

