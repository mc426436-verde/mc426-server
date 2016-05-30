(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('ShareDetailController', ShareDetailController);

    ShareDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Share', 'User', 'Device'];

    function ShareDetailController($scope, $rootScope, $stateParams, entity, Share, User, Device) {
        var vm = this;
        vm.share = entity;
        vm.load = function (id) {
            Share.get({id: id}, function(result) {
                vm.share = result;
            });
        };
        var unsubscribe = $rootScope.$on('dinoApp:shareUpdate', function(event, result) {
            vm.share = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
