package com.mastertek.navigator.web.rest.vm;

public class KeyValueDTO {
String key;
String value;

String lat;
String lng;


public KeyValueDTO() {
	super();
	// TODO Auto-generated constructor stub
}
public KeyValueDTO(String key, String value) {
	super();
	this.key = key;
	this.value = value;
}
public String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
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
}
