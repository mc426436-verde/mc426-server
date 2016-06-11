(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('ActionDetailController', ActionDetailController);

    ActionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Action'];

    function ActionDetailController($scope, $rootScope, $stateParams, entity, Action) {
        var vm = this;
        vm.action = entity;
        
        var unsubscribe = $rootScope.$on('dinoApp:actionUpdate', function(event, result) {
            vm.action = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
