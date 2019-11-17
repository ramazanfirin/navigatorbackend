'use strict';

describe('Controller Tests', function() {

    describe('Task Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTask, MockVehicle, MockDistrict, MockTown, MockStreet, MockBuilding;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTask = jasmine.createSpy('MockTask');
            MockVehicle = jasmine.createSpy('MockVehicle');
            MockDistrict = jasmine.createSpy('MockDistrict');
            MockTown = jasmine.createSpy('MockTown');
            MockStreet = jasmine.createSpy('MockStreet');
            MockBuilding = jasmine.createSpy('MockBuilding');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Task': MockTask,
                'Vehicle': MockVehicle,
                'District': MockDistrict,
                'Town': MockTown,
                'Street': MockStreet,
                'Building': MockBuilding
            };
            createController = function() {
                $injector.get('$controller')("TaskDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'navigatorbackendApp:taskUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
