function AdminCtrl($scope, AdminResource, $location) {
	$scope.page = {};
	$scope.selectedUser = {};
		
    $scope.logout = function() {    	
    	LogoutResource.logout();
    	$location.path( "/login" );
    };
}


angular.module('AdminModule', ['ngResource', 'ngRoute']).config(
	    [ '$routeProvider', function($routeProvider) {

	    } ])
	    .factory('AdminResource', function($resource) {
	        return $resource('rest/admin/user/:id/:dest', {}, {
	        	getUser: {method: 'GET', params: {id: "@id"}},
	        	userSave: {method: 'PUT', params: {id: "@id"}},
	        	userDelete: {method: 'DELETE', params: {id: "@id"}},
	        	changePassword: {method: 'POST', params: {id: "@id", dest: "password"}},
	            getPage: {method: 'GET', params: {dest: "list"}}
	        });
	    })
	    .controller('AdminCtrl', AdminCtrl)