(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('TownDialogController', TownDialogController);

    TownDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Town', 'District'];

    function TownDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Town, District) {
        var vm = this;

        vm.town = entity;
        vm.clear = clear;
        vm.save = save;
        vm.districts = District.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.town.id !== null) {
                Town.update(vm.town, onSaveSuccess, onSaveError);
            } else {
                Town.save(vm.town, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('navigatorbackendApp:townUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
