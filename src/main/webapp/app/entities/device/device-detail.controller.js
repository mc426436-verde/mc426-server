(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('DeviceDetailController', DeviceDetailController);

    DeviceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Device', 'Room', 'Share'];

    function DeviceDetailController($scope, $rootScope, $stateParams, entity, Device, Room, Share) {
        var vm = this;
        vm.device = entity;
        vm.load = function (id) {
            Device.get({id: id}, function(result) {
                vm.device = result;
            });
        };
        var unsubscribe = $rootScope.$on('dinoApp:deviceUpdate', function(event, result) {
            vm.device = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
