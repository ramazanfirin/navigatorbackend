(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('DistrictController', DistrictController);

    DistrictController.$inject = ['District'];

    function DistrictController(District) {

        var vm = this;

        vm.districts = [];

        loadAll();

        function loadAll() {
            District.query(function(result) {
                vm.districts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
