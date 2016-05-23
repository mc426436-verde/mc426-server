(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('ShareDialogController', ShareDialogController);

    ShareDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Share', 'User', 'Device'];

    function ShareDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Share, User, Device) {
        var vm = this;
        vm.share = entity;
        vm.users = User.query();
        vm.devices = Device.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('dinoApp:shareUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.share.id !== null) {
                Share.update(vm.share, onSaveSuccess, onSaveError);
            } else {
                Share.save(vm.share, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
