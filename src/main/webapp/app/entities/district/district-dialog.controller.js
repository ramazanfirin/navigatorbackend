(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('DistrictDialogController', DistrictDialogController);

    DistrictDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'District', 'City'];

    function DistrictDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, District, City) {
        var vm = this;

        vm.district = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cities = City.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.district.id !== null) {
                District.update(vm.district, onSaveSuccess, onSaveError);
            } else {
                District.save(vm.district, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('navigatorbackendApp:districtUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
