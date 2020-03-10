(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('InterestPointDetailController', InterestPointDetailController);

    InterestPointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InterestPoint'];

    function InterestPointDetailController($scope, $rootScope, $stateParams, previousState, entity, InterestPoint) {
        var vm = this;

        vm.interestPoint = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navigatorbackendApp:interestPointUpdate', function(event, result) {
            vm.interestPoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
