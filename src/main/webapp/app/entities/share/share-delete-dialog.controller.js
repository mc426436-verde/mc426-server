(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('ShareDeleteController',ShareDeleteController);

    ShareDeleteController.$inject = ['$uibModalInstance', 'entity', 'Share'];

    function ShareDeleteController($uibModalInstance, entity, Share) {
        var vm = this;
        vm.share = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Share.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
