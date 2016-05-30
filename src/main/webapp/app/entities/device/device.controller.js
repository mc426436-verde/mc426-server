(function() {
    'use strict';

    angular
        .module('dinoApp')
        .controller('DeviceController', DeviceController);

    DeviceController.$inject = ['$scope', '$state', 'Device'];

    function DeviceController ($scope, $state, Device) {
        var vm = this;
        vm.devices = [];
        vm.loadAll = function() {
            Device.query(function(result) {
                vm.devices = result;
            });
        };

        vm.loadAll();
        
    }
})();
