(function() {
    'use strict';
    angular
        .module('navigatorbackendApp')
        .factory('Task', Task);

    Task.$inject = ['$resource', 'DateUtils'];

    function Task ($resource, DateUtils) {
        var resourceUrl =  'api/tasks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'createNewTask': { method: 'POST', url:'api/createNewTask'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.insertDate = DateUtils.convertDateTimeFromServer(data.insertDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
