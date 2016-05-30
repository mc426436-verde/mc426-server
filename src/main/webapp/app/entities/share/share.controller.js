(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('ShareController', ShareController);

    ShareController.$inject = ['$scope', '$state', 'Share'];

    function ShareController ($scope, $state, Share) {
        var vm = this;
        vm.shares = [];
        vm.loadAll = function() {
            Share.query(function(result) {
                vm.shares = result;
            });
        };

        vm.loadAll();
        
    }
})();
