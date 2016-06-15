(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('ActionDetailController', ActionDetailController);

    ActionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Action', 'Device'];

    function ActionDetailController($scope, $rootScope, $stateParams, entity, Action, Device) {
        var vm = this;
        vm.action = entity;
        
        var unsubscribe = $rootScope.$on('dinoApp:actionUpdate', function(event, result) {
            vm.action = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
