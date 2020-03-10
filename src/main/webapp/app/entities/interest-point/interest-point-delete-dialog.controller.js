(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('InterestPointDeleteController',InterestPointDeleteController);

    InterestPointDeleteController.$inject = ['$uibModalInstance', 'entity', 'InterestPoint'];

    function InterestPointDeleteController($uibModalInstance, entity, InterestPoint) {
        var vm = this;

        vm.interestPoint = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InterestPoint.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
