(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('MacroSchedulingDeleteController',MacroSchedulingDeleteController);

    MacroSchedulingDeleteController.$inject = ['$uibModalInstance', 'entity', 'MacroScheduling'];

    function MacroSchedulingDeleteController($uibModalInstance, entity, MacroScheduling) {
        var vm = this;
        vm.macroScheduling = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            MacroScheduling.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
