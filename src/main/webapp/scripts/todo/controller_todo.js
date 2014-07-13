'use strict';

todoApp.controller('TodoController', ['$scope', 'resolvedTodo', 'Todo',
    function ($scope, resolvedTodo, Todo) {

        $scope.todos = resolvedTodo;

        $scope.create = function () {
            Todo.save($scope.todo,
                function () {
                    $scope.todos = Todo.query();
                    $('#saveTodoModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.todo = Todo.get({id: id});
            $('#saveTodoModal').modal('show');
        };

        $scope.delete = function (id) {
            Todo.delete({id: id},
                function () {
                    $scope.todos = Todo.query();
                });
        };

        $scope.clear = function () {
            $scope.todo = {id: null, task: null, completed: null};
        };
    }]);
