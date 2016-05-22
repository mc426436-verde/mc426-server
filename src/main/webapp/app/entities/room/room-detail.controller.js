(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('RoomDetailController', RoomDetailController);

    RoomDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Room', 'Device'];

    function RoomDetailController($scope, $rootScope, $stateParams, entity, Room, Device) {
        var vm = this;
        vm.room = entity;
        
        var unsubscribe = $rootScope.$on('dinoApp:roomUpdate', function(event, result) {
            vm.room = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
