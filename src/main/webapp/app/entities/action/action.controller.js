(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('ActionController', ActionController);

    ActionController.$inject = ['$scope', '$state', 'Action'];

    function ActionController ($scope, $state, Action) {
        var vm = this;
        vm.actions = [];
        vm.loadAll = function() {
            Action.query(function(result) {
                vm.actions = result;
            });
        };

        vm.loadAll();
        
    }
})();
