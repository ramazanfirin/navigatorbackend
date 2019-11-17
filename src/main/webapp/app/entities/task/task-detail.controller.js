(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Task', 'Vehicle', 'District', 'Town', 'Street', 'Building'];

    function TaskDetailController($scope, $rootScope, $stateParams, previousState, entity, Task, Vehicle, District, Town, Street, Building) {
        var vm = this;

        vm.task = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('navigatorbackendApp:taskUpdate', function(event, result) {
            vm.task = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
