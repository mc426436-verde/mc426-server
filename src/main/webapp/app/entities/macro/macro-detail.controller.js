(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('MacroDetailController', MacroDetailController);

    MacroDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Macro', 'Action'];

    function MacroDetailController($scope, $rootScope, $stateParams, entity, Macro, Action) {
        var vm = this;
        vm.macro = entity;
        
        var unsubscribe = $rootScope.$on('dinoApp:macroUpdate', function(event, result) {
            vm.macro = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
