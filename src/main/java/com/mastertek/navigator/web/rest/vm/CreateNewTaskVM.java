package com.mastertek.navigator.web.rest.vm;

import com.mastertek.navigator.domain.Vehicle;

public class CreateNewTaskVM {

	KeyValueDTO ilce;
	
	KeyValueDTO mahalle;
	
	KeyValueDTO sokak;
	
	KeyValueDTO bina;
	
	String lat;
	
	String lng;
	
	Vehicle vehicle;

	public KeyValueDTO getIlce() {
		return ilce;
	}

	public void setIlce(KeyValueDTO ilce) {
		this.ilce = ilce;
	}

	public KeyValueDTO getMahalle() {
		return mahalle;
	}

	public void setMahalle(KeyValueDTO mahalle) {
		this.mahalle = mahalle;
	}

	public KeyValueDTO getSokak() {
		return sokak;
	}

	public void setSokak(KeyValueDTO sokak) {
		this.sokak = sokak;
	}

	public KeyValueDTO getBina() {
		return bina;
	}

	public void setBina(KeyValueDTO bina) {
		this.bina = bina;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
}
