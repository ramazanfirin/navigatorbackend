(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('BuildingDetailController', BuildingDetailController);

    BuildingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Building', 'Street'];

    function BuildingDetailController($scope, $rootScope, $stateParams, previousState, entity, Building, Street) {
        var vm = this;

        vm.building = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navigatorbackendApp:buildingUpdate', function(event, result) {
            vm.building = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
