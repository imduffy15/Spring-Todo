'use strict';

todoApp.factory('Todo', ['$resource',
    function ($resource) {
        return $resource('app/rest/todos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
