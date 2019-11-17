(function() {
    'use strict';
    angular
        .module('navigatorbackendApp')
        .factory('Street', Street);

    Street.$inject = ['$resource'];

    function Street ($resource) {
        var resourceUrl =  'api/streets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
