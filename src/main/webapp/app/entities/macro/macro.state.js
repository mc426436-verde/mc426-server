(function() {
    'use strict';

    angular
        .module('dinoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('macro', {
            parent: 'entity',
            url: '/macro',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dinoApp.macro.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro/macros.html',
                    controller: 'MacroController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macro');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('macro-detail', {
            parent: 'entity',
            url: '/macro/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dinoApp.macro.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro/macro-detail.html',
                    controller: 'MacroDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macro');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Macro', function($stateParams, Macro) {
                    return Macro.get({id : $stateParams.id});
                }]
            }
        })
        .state('macro.new', {
            parent: 'macro',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro/macro-dialog.html',
                    controller: 'MacroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('macro', null, { reload: true });
                }, function() {
                    $state.go('macro');
                });
            }]
        })
        .state('macro.edit', {
            parent: 'macro',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro/macro-dialog.html',
                    controller: 'MacroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Macro', function(Macro) {
                            return Macro.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('macro.delete', {
            parent: 'macro',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro/macro-delete-dialog.html',
                    controller: 'MacroDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Macro', function(Macro) {
                            return Macro.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
