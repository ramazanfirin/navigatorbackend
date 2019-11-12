(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','User','AlertService'];

    function HomeController ($scope, Principal, LoginService, $state,User,AlertService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.ilceList = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.updateMahalle = updateMahalle;
        vm.updateSokak = updateSokak;
        vm.updateBina = updateBina;
        vm.updateCoordinate = updateCoordinate;
        
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();
        
        

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
        
        
        
        User.getIlceList({ }, onSuccessGetIlce, onError);
        
        function onSuccessGetIlce(data, headers) {
        	vm.ilceList = data;
        	vm.selectedIlce = data[0];
           
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        
        
        
        function updateMahalle(ilce){
        	User.getMahalleList({
        		param1:ilce.value
        	}, onSuccessGetMahalle, onError);
        }
        
        function onSuccessGetMahalle(data, headers) {
        	vm.mahalleList = data;
           
        }
        
        
        function updateSokak(mahalle){
        	User.getSokakList({
        		param1:mahalle.value
        	}, onSuccessGetSokak, onError);
        }
        
        function onSuccessGetSokak(data, headers) {
        	vm.sokakList = data;
           
        }
    
        function updateBina(sokak){
        	User.getBinaList({
        		param1:sokak.value
        	}, onSuccessGetBina, onError);
        }
        
        function onSuccessGetBina(data, headers) {
        	vm.binaList = data;
           
        }
        
        
        function updateCoordinate(bina){
        	User.getCoordinate({
        		param1:bina.value
        	}, onSuccessGetCoordinate, onError);
        }
        
        function onSuccessGetCoordinate(data, headers) {
        	vm.coordinates = data;
           
        }
    }
})();
