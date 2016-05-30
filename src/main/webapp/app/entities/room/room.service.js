(function() {
    'use strict';
    angular
        .module('dinoApp')
        .factory('Room', Room);

    Room.$inject = ['$resource'];

    function Room ($resource) {
        var resourceUrl =  'api/rooms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
