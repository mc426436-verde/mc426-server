'use strict';

describe('Controller Tests', function() {

    describe('Share Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockShare, MockUser, MockDevice;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockShare = jasmine.createSpy('MockShare');
            MockUser = jasmine.createSpy('MockUser');
            MockDevice = jasmine.createSpy('MockDevice');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Share': MockShare,
                'User': MockUser,
                'Device': MockDevice
            };
            createController = function() {
                $injector.get('$controller')("ShareDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'dinoApp:shareUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
