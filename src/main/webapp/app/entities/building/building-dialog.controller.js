(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('BuildingDialogController', BuildingDialogController);

    BuildingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Building', 'Street'];

    function BuildingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Building, Street) {
        var vm = this;

        vm.building = entity;
        vm.clear = clear;
        vm.save = save;
        vm.streets = Street.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.building.id !== null) {
                Building.update(vm.building, onSaveSuccess, onSaveError);
            } else {
                Building.save(vm.building, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('navigatorbackendApp:buildingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
