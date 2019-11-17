(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('TownController', TownController);

    TownController.$inject = ['Town'];

    function TownController(Town) {

        var vm = this;

        vm.towns = [];

        loadAll();

        function loadAll() {
            Town.query(function(result) {
                vm.towns = result;
                vm.searchQuery = null;
            });
        }
    }
})();
