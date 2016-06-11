(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('MacroDialogController', MacroDialogController);

    MacroDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Macro'];

    function MacroDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Macro) {
        var vm = this;
        vm.macro = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('dinoApp:macroUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.macro.id !== null) {
                Macro.update(vm.macro, onSaveSuccess, onSaveError);
            } else {
                Macro.save(vm.macro, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
