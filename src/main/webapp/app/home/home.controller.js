(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','User','AlertService','NgMap'];

    function HomeController ($scope, Principal, LoginService, $state,User,AlertService,NgMap) {
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
        
        var marker = new google.maps.Marker();
        //var map = NgMap.getMap();
        var map ;
        
        marker.addListener('click', function() {
//            map.setZoom(8);
//            map.setCenter(marker.getPosition());
        	vm.foo();
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

            var latlng = new google.maps.LatLng(vm.coordinates[1].value, vm.coordinates[0].value);
            marker.setPosition(latlng);
            
            map.setCenter(latlng);
            map.setZoom(18);
            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));
            marker.setMap(map);
        }
    }
})();
