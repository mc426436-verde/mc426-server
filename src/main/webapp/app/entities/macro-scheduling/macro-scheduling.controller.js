(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('MacroSchedulingController', MacroSchedulingController);

    MacroSchedulingController.$inject = ['$scope', '$state', 'MacroScheduling'];

    function MacroSchedulingController ($scope, $state, MacroScheduling) {
        var vm = this;
        vm.macroSchedulings = [];
        vm.loadAll = function() {
            MacroScheduling.query(function(result) {
                vm.macroSchedulings = result;
            });
        };

        vm.loadAll();
        
    }
})();
