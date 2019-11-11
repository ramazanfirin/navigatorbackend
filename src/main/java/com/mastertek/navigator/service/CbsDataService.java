package com.mastertek.navigator.service;

import java.util.List;

import com.mastertek.navigator.web.rest.vm.KeyValueDTO;

public interface CbsDataService {
	List <KeyValueDTO> getIlceList() throws Exception;
	List <KeyValueDTO>  getMahalleList(String url1) throws Exception;
	List <KeyValueDTO>  getSokakList(String url1) throws Exception;
	List <KeyValueDTO> getBinaList(String url1,String mahalleId) throws Exception;
	List <KeyValueDTO> genelAramaByNumber(String dto) throws Exception;
	List <KeyValueDTO> genelArama(String string) throws Exception;
	List <String> getKapiNo(String binaNo) throws Exception;
}
