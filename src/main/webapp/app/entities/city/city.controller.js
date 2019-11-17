(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('CityController', CityController);

    CityController.$inject = ['City'];

    function CityController(City) {

        var vm = this;

        vm.cities = [];

        loadAll();

        function loadAll() {
            City.query(function(result) {
                vm.cities = result;
                vm.searchQuery = null;
            });
        }
    }
})();
