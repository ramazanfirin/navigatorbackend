(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('InterestPointDialogController', InterestPointDialogController);

    InterestPointDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InterestPoint'];

    function InterestPointDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InterestPoint) {
        var vm = this;

        vm.interestPoint = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.interestPoint.id !== null) {
                InterestPoint.update(vm.interestPoint, onSaveSuccess, onSaveError);
            } else {
                InterestPoint.save(vm.interestPoint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('navigatorbackendApp:interestPointUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
