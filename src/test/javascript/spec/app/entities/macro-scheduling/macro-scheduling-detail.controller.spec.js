'use strict';

describe('Controller Tests', function() {

    describe('MacroScheduling Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMacroScheduling, MockMacro;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMacroScheduling = jasmine.createSpy('MockMacroScheduling');
            MockMacro = jasmine.createSpy('MockMacro');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MacroScheduling': MockMacroScheduling,
                'Macro': MockMacro
            };
            createController = function() {
                $injector.get('$controller')("MacroSchedulingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'dinoApp:macroSchedulingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
