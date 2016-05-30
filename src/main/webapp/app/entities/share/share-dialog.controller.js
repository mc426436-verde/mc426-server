(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('ShareDialogController', ShareDialogController);

    ShareDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Share', 'User', 'Device'];

    function ShareDialogController ($scope, $stateParams, $uibModalInstance, entity, Share, User, Device) {
        var vm = this;
        vm.share = entity;
        vm.users = User.query();
        vm.devices = Device.query();
        vm.load = function(id) {
            Share.get({id : id}, function(result) {
                vm.share = result;
            });
        };

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
