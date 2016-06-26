(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('MacroSchedulingDialogController', MacroSchedulingDialogController);

    MacroSchedulingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MacroScheduling', 'Macro'];

    function MacroSchedulingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MacroScheduling, Macro) {
        var vm = this;
        vm.macroScheduling = entity;
        vm.macros = Macro.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('dinoApp:macroSchedulingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.macroScheduling.id !== null) {
                MacroScheduling.update(vm.macroScheduling, onSaveSuccess, onSaveError);
            } else {
                MacroScheduling.save(vm.macroScheduling, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
