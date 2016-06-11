(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('MacroController', MacroController);

    MacroController.$inject = ['$scope', '$state', 'Macro'];

    function MacroController ($scope, $state, Macro) {
        var vm = this;
        vm.macros = [];
        vm.loadAll = function() {
            Macro.query(function(result) {
                vm.macros = result;
            });
        };

        vm.loadAll();
        
    }
})();
