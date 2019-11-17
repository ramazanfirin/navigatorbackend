(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('StreetController', StreetController);

    StreetController.$inject = ['Street'];

    function StreetController(Street) {

        var vm = this;

        vm.streets = [];

        loadAll();

        function loadAll() {
            Street.query(function(result) {
                vm.streets = result;
                vm.searchQuery = null;
            });
        }
    }
})();
