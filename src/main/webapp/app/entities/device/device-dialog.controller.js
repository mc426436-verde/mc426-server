(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('DeviceDialogController', DeviceDialogController);

    DeviceDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Device', 'Room', 'Share'];

    function DeviceDialogController ($scope, $stateParams, $uibModalInstance, entity, Device, Room, Share) {
        var vm = this;
        vm.device = entity;
        vm.rooms = Room.query();
        vm.shares = Share.query();
        vm.load = function(id) {
            Device.get({id : id}, function(result) {
                vm.device = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dinoApp:deviceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.device.id !== null) {
                Device.update(vm.device, onSaveSuccess, onSaveError);
            } else {
                Device.save(vm.device, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
