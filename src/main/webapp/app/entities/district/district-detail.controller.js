(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('DistrictDetailController', DistrictDetailController);

    DistrictDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'District', 'City'];

    function DistrictDetailController($scope, $rootScope, $stateParams, previousState, entity, District, City) {
        var vm = this;

        vm.district = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navigatorbackendApp:districtUpdate', function(event, result) {
            vm.district = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
