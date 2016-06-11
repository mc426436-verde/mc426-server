(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('DeviceDialogController', DeviceDialogController);

    DeviceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Device', 'Room'];

    function DeviceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Device, Room) {
        var vm = this;
        vm.device = entity;
        vm.rooms = Room.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

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
