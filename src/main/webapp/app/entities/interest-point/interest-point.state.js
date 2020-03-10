(function() {
    'use strict';

    angular
        .module('navigatorbackendApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('interest-point', {
            parent: 'entity',
            url: '/interest-point',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navigatorbackendApp.interestPoint.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/interest-point/interest-points.html',
                    controller: 'InterestPointController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('interestPoint');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('interest-point-detail', {
            parent: 'interest-point',
            url: '/interest-point/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'navigatorbackendApp.interestPoint.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/interest-point/interest-point-detail.html',
                    controller: 'InterestPointDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('interestPoint');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InterestPoint', function($stateParams, InterestPoint) {
                    return InterestPoint.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'interest-point',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('interest-point-detail.edit', {
            parent: 'interest-point-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/interest-point/interest-point-dialog.html',
                    controller: 'InterestPointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InterestPoint', function(InterestPoint) {
                            return InterestPoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('interest-point.new', {
            parent: 'interest-point',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/interest-point/interest-point-dialog.html',
                    controller: 'InterestPointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                lat: null,
                                lng: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('interest-point', null, { reload: 'interest-point' });
                }, function() {
                    $state.go('interest-point');
                });
            }]
        })
        .state('interest-point.edit', {
            parent: 'interest-point',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/interest-point/interest-point-dialog.html',
                    controller: 'InterestPointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InterestPoint', function(InterestPoint) {
                            return InterestPoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('interest-point', null, { reload: 'interest-point' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('interest-point.delete', {
            parent: 'interest-point',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/interest-point/interest-point-delete-dialog.html',
                    controller: 'InterestPointDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InterestPoint', function(InterestPoint) {
                            return InterestPoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('interest-point', null, { reload: 'interest-point' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
