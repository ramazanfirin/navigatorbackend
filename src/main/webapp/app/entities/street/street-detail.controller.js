(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('StreetDetailController', StreetDetailController);

    StreetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Street', 'Town'];

    function StreetDetailController($scope, $rootScope, $stateParams, previousState, entity, Street, Town) {
        var vm = this;

        vm.street = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navigatorbackendApp:streetUpdate', function(event, result) {
            vm.street = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
