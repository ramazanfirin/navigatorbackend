(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('StationController', StationController);

    StationController.$inject = ['Station'];

    function StationController(Station) {

        var vm = this;

        vm.stations = [];

        loadAll();

        function loadAll() {
            Station.query(function(result) {
                vm.stations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
