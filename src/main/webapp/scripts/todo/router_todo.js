'use strict';

todoApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/todo', {
                    templateUrl: 'views/todos.html',
                    controller: 'TodoController',
                    resolve:{
                        resolvedTodo: ['Todo', function (Todo) {
                            return Todo.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
