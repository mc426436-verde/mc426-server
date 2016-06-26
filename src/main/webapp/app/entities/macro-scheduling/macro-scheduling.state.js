(function() {
    'use strict';

    angular
        .module('dinoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('macro-scheduling', {
            parent: 'entity',
            url: '/macro-scheduling',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dinoApp.macroScheduling.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro-scheduling/macro-schedulings.html',
                    controller: 'MacroSchedulingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macroScheduling');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('macro-scheduling-detail', {
            parent: 'entity',
            url: '/macro-scheduling/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dinoApp.macroScheduling.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro-scheduling/macro-scheduling-detail.html',
                    controller: 'MacroSchedulingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macroScheduling');
                    $translatePartialLoader.addPart('daysInWeek');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MacroScheduling', function($stateParams, MacroScheduling) {
                    return MacroScheduling.get({id : $stateParams.id});
                }]
            }
        })
        .state('macro-scheduling.new', {
            parent: 'macro-scheduling',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-scheduling/macro-scheduling-dialog.html',
                    controller: 'MacroSchedulingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                daysInWeek: null,
                                time: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('macro-scheduling', null, { reload: true });
                }, function() {
                    $state.go('macro-scheduling');
                });
            }]
        })
        .state('macro-scheduling.edit', {
            parent: 'macro-scheduling',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-scheduling/macro-scheduling-dialog.html',
                    controller: 'MacroSchedulingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MacroScheduling', function(MacroScheduling) {
                            return MacroScheduling.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro-scheduling', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('macro-scheduling.delete', {
            parent: 'macro-scheduling',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-scheduling/macro-scheduling-delete-dialog.html',
                    controller: 'MacroSchedulingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MacroScheduling', function(MacroScheduling) {
                            return MacroScheduling.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro-scheduling', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
