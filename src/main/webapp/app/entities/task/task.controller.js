(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('TaskController', TaskController);

    TaskController.$inject = ['Task'];

    function TaskController(Task) {

        var vm = this;

        vm.tasks = [];

        loadAll();

        function loadAll() {
            Task.query(function(result) {
                vm.tasks = result;
                vm.searchQuery = null;
            });
        }
    }
})();
