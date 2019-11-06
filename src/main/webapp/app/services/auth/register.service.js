(function () {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
