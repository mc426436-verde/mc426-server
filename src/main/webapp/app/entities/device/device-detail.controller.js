(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('DeviceDetailController', DeviceDetailController);

    DeviceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Device', 'Room', 'User'];

    function DeviceDetailController($scope, $rootScope, $stateParams, entity, Device, Room, User) {
        var vm = this;
        vm.device = entity;
        
        var unsubscribe = $rootScope.$on('dinoApp:deviceUpdate', function(event, result) {
            vm.device = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
