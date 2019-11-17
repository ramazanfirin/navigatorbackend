(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('VehicleDetailController', VehicleDetailController);

    VehicleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vehicle', 'Station'];

    function VehicleDetailController($scope, $rootScope, $stateParams, previousState, entity, Vehicle, Station) {
        var vm = this;

        vm.vehicle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navigatorbackendApp:vehicleUpdate', function(event, result) {
            vm.vehicle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
