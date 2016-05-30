(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('RoomController', RoomController);

    RoomController.$inject = ['$scope', '$state', 'Room'];

    function RoomController ($scope, $state, Room) {
        var vm = this;
        vm.rooms = [];
        vm.loadAll = function() {
            Room.query(function(result) {
                vm.rooms = result;
            });
        };

        vm.loadAll();
        
    }
})();
