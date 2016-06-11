(function() {
    'use strict';

    angular
        .module('dinoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('action', {
            parent: 'entity',
            url: '/action',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dinoApp.action.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/action/actions.html',
                    controller: 'ActionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('action');
                    $translatePartialLoader.addPart('deviceStatusEnum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('action-detail', {
            parent: 'entity',
            url: '/action/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dinoApp.action.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/action/action-detail.html',
                    controller: 'ActionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('action');
                    $translatePartialLoader.addPart('deviceStatusEnum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Action', function($stateParams, Action) {
                    return Action.get({id : $stateParams.id});
                }]
            }
        })
        .state('action.new', {
            parent: 'action',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action/action-dialog.html',
                    controller: 'ActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('action', null, { reload: true });
                }, function() {
                    $state.go('action');
                });
            }]
        })
        .state('action.edit', {
            parent: 'action',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action/action-dialog.html',
                    controller: 'ActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Action', function(Action) {
                            return Action.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('action', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('action.delete', {
            parent: 'action',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action/action-delete-dialog.html',
                    controller: 'ActionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Action', function(Action) {
                            return Action.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('action', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
