(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('ActionDeleteController',ActionDeleteController);

    ActionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Action'];

    function ActionDeleteController($uibModalInstance, entity, Action) {
        var vm = this;
        vm.action = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Action.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
