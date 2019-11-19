(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('street', {
            parent: 'entity',
            url: '/street',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navigatorbackendApp.street.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/street/streets.html',
                    controller: 'StreetController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('street');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('street-detail', {
            parent: 'street',
            url: '/street/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navigatorbackendApp.street.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/street/street-detail.html',
                    controller: 'StreetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('street');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Street', function($stateParams, Street) {
                    return Street.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'street',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('street-detail.edit', {
            parent: 'street-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/street/street-dialog.html',
                    controller: 'StreetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Street', function(Street) {
                            return Street.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('street.new', {
            parent: 'street',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/street/street-dialog.html',
                    controller: 'StreetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                lat: null,
                                lng: null,
                                completed: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('street', null, { reload: 'street' });
                }, function() {
                    $state.go('street');
                });
            }]
        })
        .state('street.edit', {
            parent: 'street',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/street/street-dialog.html',
                    controller: 'StreetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Street', function(Street) {
                            return Street.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('street', null, { reload: 'street' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('street.delete', {
            parent: 'street',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/street/street-delete-dialog.html',
                    controller: 'StreetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Street', function(Street) {
                            return Street.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('street', null, { reload: 'street' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
