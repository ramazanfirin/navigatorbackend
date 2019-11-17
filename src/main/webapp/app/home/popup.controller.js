(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('PopupController',PopupController);

    PopupController.$inject = ['$stateParams', '$uibModalInstance' , 'User', 'JhiLanguageService','$sessionStorage','Station','Vehicle','AlertService','Task'];

    function PopupController ($stateParams, $uibModalInstance,  User, JhiLanguageService,$sessionStorage,Station,Vehicle,AlertService,Task) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        
        vm.test="sdfsdfsdf"
        vm.sessionStorage = $sessionStorage;	
        	
        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });
        
        loadAll();
        
        function loadAll() {
            Station.query(function(result) {
                vm.stations = result;
                
            });
            
            Vehicle.query(function(result) {
                vm.vehicles = result;
                
            });
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
    }
})();
