(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('StreetDeleteController',StreetDeleteController);

    StreetDeleteController.$inject = ['$uibModalInstance', 'entity', 'Street'];

    function StreetDeleteController($uibModalInstance, entity, Street) {
        var vm = this;

        vm.street = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Street.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
