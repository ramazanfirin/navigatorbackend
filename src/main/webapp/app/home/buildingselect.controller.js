(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('BuildingSelectController',BuildingSelectController);

    BuildingSelectController.$inject = ['$stateParams', '$uibModalInstance' , 'User', 'JhiLanguageService','$sessionStorage','Station','Vehicle','AlertService','Task'];

    function BuildingSelectController ($stateParams, $uibModalInstance,  User, JhiLanguageService,$sessionStorage,Station,Vehicle,AlertService,Task) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        
        vm.test="sdfsdfsdf"
        vm.sessionStorage = $sessionStorage;	
        vm.updateCoordinate = updateCoordinate;
        vm.selectedBuilding = '';
        
        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });
        
        loadAll();
        
        //vm.buildings=[];
        
        function loadAll() {
        	vm.buildings = vm.sessionStorage.buildings;
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save () {
            vm.isSaving = true;
            Task.createNewTask({
            	ilce:vm.sessionStorage.selectedIlce,
            	mahalle:vm.sessionStorage.selectedMahalle,
            	sokak:vm.sessionStorage.selectedSokak,
            	bina:vm.sessionStorage.selectedBina,
            	lat:vm.sessionStorage.coordinates[1].value,
            	lng:vm.sessionStorage.coordinates[0].value,
            	vehicle:vm.selectedVehicle,
            },saveOnSuccess,onError)
        }
        
        function saveOnSuccess(data, headers){
        	vm.isSaving = false;
        	//AlertService.success('başarılı');
        	clear ()
        }
        
        function onError(error) {
            AlertService.error(error.data.message);
        }
        
        
        function updateCoordinate(){
        	User.getCoordinate({
        		param1:selectedBuilding
        	}, onSuccessGetCoordinate, onError);
        }
        
        function onSuccessGetCoordinate(data, headers) {
        	vm.coordinates = data;
        	addMarker(vm.coordinates[1].value, vm.coordinates[0].value,'');
        	clear();
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
        
    }
})();
