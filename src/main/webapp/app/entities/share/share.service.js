(function() {
    'use strict';
    angular
        .module('dinoApp')
        .factory('Share', Share);

    Share.$inject = ['$resource'];

    function Share ($resource) {
        var resourceUrl =  'api/shares/:id';

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
