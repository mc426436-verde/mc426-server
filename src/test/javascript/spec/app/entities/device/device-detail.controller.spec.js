'use strict';

describe('Controller Tests', function() {

    describe('Device Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDevice, MockRoom, MockShare;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDevice = jasmine.createSpy('MockDevice');
            MockRoom = jasmine.createSpy('MockRoom');
            MockShare = jasmine.createSpy('MockShare');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Device': MockDevice,
                'Room': MockRoom,
                'Share': MockShare
            };
            createController = function() {
                $injector.get('$controller')("DeviceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'dinoApp:deviceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
