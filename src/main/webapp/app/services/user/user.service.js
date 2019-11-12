(function () {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'getIlceList': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/getIlceList'},
            'getMahalleList': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/getMahalleList/:param1'},
            'getSokakList': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/getSokakList/:param1'},
            'getBinaList': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/getBinaList/:param1'},
            'getCoordinate': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/getCoordinate/:param1'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;
    }
})();
