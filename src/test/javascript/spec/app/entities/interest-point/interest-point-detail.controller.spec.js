'use strict';

describe('Controller Tests', function() {

    describe('InterestPoint Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockInterestPoint;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockInterestPoint = jasmine.createSpy('MockInterestPoint');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'InterestPoint': MockInterestPoint
            };
            createController = function() {
                $injector.get('$controller')("InterestPointDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'navigatorbackendApp:interestPointUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
