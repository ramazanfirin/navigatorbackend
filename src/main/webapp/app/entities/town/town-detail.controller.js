(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('TownDetailController', TownDetailController);

    TownDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Town', 'District'];

    function TownDetailController($scope, $rootScope, $stateParams, previousState, entity, Town, District) {
        var vm = this;

        vm.town = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navigatorbackendApp:townUpdate', function(event, result) {
            vm.town = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
