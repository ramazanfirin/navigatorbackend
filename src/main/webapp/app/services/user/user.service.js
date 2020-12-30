(function () {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'getIlList': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/getIlList'},
            'getIlceList': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/getIlceList/:city'},
            'getMahalleList': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/getMahalleList/:param1/:city'},
            'getSokakList': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/getSokakList/:mahalleID/:ilceID/:city'},
            'getBinaList': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/getBinaList/:param1/:mahalleID/:ilceID/:city'},
            'getCoordinate': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/getCoordinate/:param1/:lat/:lng/:city'},
            'search': {method: 'GET', isArray: true,url:'/api/cbs-data-controller/search/:param1'},
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
