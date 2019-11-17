(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('StreetDialogController', StreetDialogController);

    StreetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Street', 'Town'];

    function StreetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Street, Town) {
        var vm = this;

        vm.street = entity;
        vm.clear = clear;
        vm.save = save;
        vm.towns = Town.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.street.id !== null) {
                Street.update(vm.street, onSaveSuccess, onSaveError);
            } else {
                Street.save(vm.street, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('navigatorbackendApp:streetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
