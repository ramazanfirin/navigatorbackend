(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','User','AlertService','NgMap','$sessionStorage','Station','Vehicle','Task','InterestPoint'];

    function HomeController ($scope, Principal, LoginService, $state,User,AlertService,NgMap,$sessionStorage,Station,Vehicle,Task,InterestPoint) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.ilceList = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.updateIlce = updateIlce;
        vm.updateMahalle = updateMahalle;
        vm.updateSokak = updateSokak;
        vm.updateBina = updateBina;
        vm.updateCoordinate = updateCoordinate;
        vm.showInterestPointDetails = showInterestPointDetails;
        vm.findByBuildingName = findByBuildingName;
        vm.interestPoints = [];
        vm.selectedIl;
        vm.selectedIlce;
        vm.selectedMahalle;
        vm.selectedSokak;
        
        vm.sessionStorage = $sessionStorage;	
        
        InterestPoint.query(function(result) {
            vm.interestPoints = result;
           
        });
        
        var marker = new google.maps.Marker();
        var interestPointMarker = new google.maps.Marker();
        interestPointMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/blue-dot.png')

        var map ;
        
        marker.addListener('click', function() {
//            map.setZoom(8);
//            map.setCenter(marker.getPosition());
        	//vm.foo();
        	$sessionStorage.selectedIlce = vm.selectedIlce ;
        	$sessionStorage.selectedMahalle = vm.selectedMahalle ;
        	$sessionStorage.selectedSokak = vm.selectedSokak ;
        	$sessionStorage.selectedBina = vm.selectedBina ;
        	$sessionStorage.coordinates = vm.coordinates ;
        	$state.go('home.new');
          });
        
        
        
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();
        
        vm.foo = function() {
            alert('it works');
          }
        
        NgMap.getMap().then(function(mapTemp) {
            map = mapTemp;
        	console.log(map.getCenter());
            console.log('markers', map.markers);
            console.log('shapes', map.shapes);
          });
        

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
        
        User.getIlList({ }, onSuccessGetIl, onError);
        
        function onSuccessGetIl(data, headers) {
        	vm.ilList = data;
        	vm.selectedIl = data[0];
           
        }
        
        function onError(error) {
            AlertService.error(error.data.message);
        }
        
        function updateIlce(il){
        	User.getIlceList({
        		city:vm.selectedIl.value
        	}, onSuccessGetIlce, onError);
        	
        }
        
        function onSuccessGetIlce(data, headers) {
        	vm.ilceList = data;
           
        }
        
        function updateMahalle(ilce){
        	vm.selectedIlce = ilce;
        	User.getMahalleList({
        		param1:ilce.value,
        		city:vm.selectedIl.value
        	}, onSuccessGetMahalle, onError);
        }
        
        function onSuccessGetMahalle(data, headers) {
        	vm.mahalleList = data;
           
        }
        
        function updateSokak(mahalle){
        	vm.selectedMahalle = mahalle;
        	User.getSokakList({
        		mahalleID:vm.selectedMahalle.value,
        		ilceID:vm.selectedIlce.value,
        		city:vm.selectedIl.value
        	}, onSuccessGetSokak, onError);
        }
        
        function onSuccessGetSokak(data, headers) {
        	vm.sokakList = data;
           
        }
    
        function updateBina(sokak){
        	vm.selectedSokak = sokak;
        	User.getBinaList({
        		param1:sokak.value,
        		mahalleID:vm.selectedMahalle.value,
        		ilceID:vm.selectedIlce.value,
        		city:vm.selectedIl.value
        	}, onSuccessGetBina, onError);
        }
        
        function onSuccessGetBina(data, headers) {
        	vm.binaList = data;
           
        }
        
        
        function updateCoordinate(bina){
        	var param1 = 0 ;
        	var lat = 0;
        	var lng = 0
        	if(bina.value){
        		param1 = bina.value
        	}else{
        		lat = bina.lat;
        		lng = bina.lng
        		
        	}
        	
        	User.getCoordinate({
        		param1:param1,
        		lat:lng,
        		lng:lat,
        		city:vm.selectedIl.value
        	}, onSuccessGetCoordinate, onError);
        }
        
        function onSuccessGetCoordinate(data, headers) {
        	vm.coordinates = data;
        	addMarker(vm.coordinates[1].value, vm.coordinates[0].value,'');
            
        }
        
        function addMarker(lat,lng,title){
        	var latlng = new google.maps.LatLng(lat, lng);
            marker.setPosition(latlng);
            marker.setTitle(title);
            
            map.setCenter(latlng);
            map.setZoom(18);
            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));
            marker.setMap(map);
        }
        
        function addInterestPointMarker(lat,lng,title){
        	var latlng = new google.maps.LatLng(lat, lng);
            interestPointMarker.setPosition(latlng);
            interestPointMarker.setTitle(title);
            
            map.setCenter(latlng);
            map.setZoom(18);
            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));
            interestPointMarker.setMap(map);
        }
        
        function showInterestPointDetails(){
        	addInterestPointMarker(vm.selectedInterestPoint.lat,vm.selectedInterestPoint.lng,vm.selectedInterestPoint.name);
        }
        
        function findByBuildingName(){
        	User.search({
        		param1:vm.buildingName
        	}, onSuccessSearch, onError);
        }
        
        function onSuccessSearch(data, headers) {
        	vm.sessionStorage.buildings = data;
        	vm.sessionStorage.map = map;
        	vm.sessionStorage.marker = marker;
        	$state.go('home.buildingselect');
        }
        
    }
})();
