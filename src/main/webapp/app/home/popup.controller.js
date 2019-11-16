(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .controller('PopupController',PopupController);

    PopupController.$inject = ['$stateParams', '$uibModalInstance' , 'User', 'JhiLanguageService','$sessionStorage'];

    function PopupController ($stateParams, $uibModalInstance,  User, JhiLanguageService,$sessionStorage) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        
        vm.test="sdfsdfsdf"
        vm.sessionStorage = $sessionStorage;	
        	
        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save () {
            vm.isSaving = true;
            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }
    }
})();
