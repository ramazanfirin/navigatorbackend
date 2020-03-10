(function() {
    'use strict';
    angular
        .module('navigatorbackendApp')
        .factory('InterestPoint', InterestPoint);

    InterestPoint.$inject = ['$resource'];

    function InterestPoint ($resource) {
        var resourceUrl =  'api/interest-points/:id';

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
