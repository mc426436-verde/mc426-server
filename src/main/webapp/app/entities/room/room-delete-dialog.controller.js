(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('RoomDeleteController',RoomDeleteController);

    RoomDeleteController.$inject = ['$uibModalInstance', 'entity', 'Room'];

    function RoomDeleteController($uibModalInstance, entity, Room) {
        var vm = this;
        vm.room = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Room.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
