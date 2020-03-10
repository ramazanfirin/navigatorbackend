(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('InterestPointController', InterestPointController);

    InterestPointController.$inject = ['InterestPoint'];

    function InterestPointController(InterestPoint) {

        var vm = this;

        vm.interestPoints = [];

        loadAll();

        function loadAll() {
            InterestPoint.query(function(result) {
                vm.interestPoints = result;
                vm.searchQuery = null;
            });
        }
    }
})();
