package com.mastertek.navigator.web.rest;

import com.mastertek.navigator.NavigatorbackendApp;
import com.mastertek.navigator.service.KayseriDataServiceService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the Import REST controller.
 *
 * @see ImportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavigatorbackendApp.class)
public class ImportResourceIntTest {

    private MockMvc restMockMvc;
    
    private KayseriDataServiceService kayseriDataServiceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ImportResource importResource = new ImportResource(kayseriDataServiceService);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(importResource)
            .build();
    }

    /**
    * Test importKayseri
    */
    @Test
    public void testImportKayseri() throws Exception {
        restMockMvc.perform(get("/api/import/import-kayseri"))
            .andExpect(status().isOk());
    }

}
