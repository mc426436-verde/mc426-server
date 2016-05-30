(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('RoomDialogController', RoomDialogController);

    RoomDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Room', 'Device'];

    function RoomDialogController ($scope, $stateParams, $uibModalInstance, entity, Room, Device) {
        var vm = this;
        vm.room = entity;
        vm.devices = Device.query();
        vm.load = function(id) {
            Room.get({id : id}, function(result) {
                vm.room = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dinoApp:roomUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.room.id !== null) {
                Room.update(vm.room, onSaveSuccess, onSaveError);
            } else {
                Room.save(vm.room, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
