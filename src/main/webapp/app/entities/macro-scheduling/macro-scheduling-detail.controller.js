(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('MacroSchedulingDetailController', MacroSchedulingDetailController);

    MacroSchedulingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'MacroScheduling', 'Macro'];

    function MacroSchedulingDetailController($scope, $rootScope, $stateParams, entity, MacroScheduling, Macro) {
        var vm = this;
        vm.macroScheduling = entity;

        var unsubscribe = $rootScope.$on('dinoApp:macroSchedulingUpdate', function(event, result) {
            vm.macroScheduling = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
