(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('MacroDeleteController',MacroDeleteController);

    MacroDeleteController.$inject = ['$uibModalInstance', 'entity', 'Macro'];

    function MacroDeleteController($uibModalInstance, entity, Macro) {
        var vm = this;
        vm.macro = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Macro.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
