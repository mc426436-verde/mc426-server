(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('ActionDialogController', ActionDialogController);

    ActionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Action', 'Device'];

    function ActionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Action, Device) {
        var vm = this;
        vm.action = entity;
        vm.devices = Device.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('dinoApp:actionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.action.id !== null) {
                Action.update(vm.action, onSaveSuccess, onSaveError);
            } else {
                Action.save(vm.action, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
