(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('StationDetailController', StationDetailController);

    StationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Station', 'City'];

    function StationDetailController($scope, $rootScope, $stateParams, previousState, entity, Station, City) {
        var vm = this;

        vm.station = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navigatorbackendApp:stationUpdate', function(event, result) {
            vm.station = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
