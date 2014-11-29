// controllers definition
function LoginCtrl($scope, LoginResource, SecurityService, $location, $rootScope) {
    $scope.newUser = {};

    $scope.login = function() {
        if ($scope.newUser.userId != undefined && $scope.newUser.password != undefined) {
            LoginResource($scope.newUser).login($scope.newUser,
                function (response) {
            		var session = response.entity;
            		
            		if(session != null && session != '' && session != 'undefined') {
	                    SecurityService.initSession(session);
	                    $location.path( "/home" );
	                    $scope.newUser = {};
            		}
                }
            );
        }
    };

    $scope.redirectoToSignUp = function() {
        $location.path( "/signup" );
    };

}

function LogoutCtrl($scope, LogoutResource, $location) {
    $scope.newUser = {};

    $scope.logout = function() {    	
    	LogoutResource.logout();
    	$location.path( "/login" );
    };
}

function SignupCtrl($scope, $http, RegistrationResource, $q, $location, $timeout) {
    $scope.register = function() {
        RegistrationResource.save($scope.newUser, function(data) {
            $location.path("/successfulRegistration");
        });
    };
}

function ActivationCtrl($scope, $routeParams, RegistrationResource, SecurityService, $location) {
    $scope.activate = function() {
        RegistrationResource.activate($routeParams.activationCode, function(response) {
        	$location.path( "/login" );          
        }, function(result) {
            $location.path( "/invalidActivationCode" );
        });

    };

    $scope.activate();
}

angular.module('SecurityModule', ['ngResource', 'ngRoute']).config(
	    [ '$routeProvider', function($routeProvider) {
	        $routeProvider.when('/login', {
	            templateUrl : 'partials/login.html',
	            controller : 'LoginCtrl',
	            access : {
	                isFree : true
	            }
	        }).when('/signup', {
	            templateUrl : 'partials/signup.html',
	            controller : 'SignupCtrl',
	            access : {
	                isFree : true
	            }
	        }).when('/activate/:activationCode', {
	            templateUrl : 'partials/activate.html',
	            controller : 'ActivationCtrl',
	            access : {
	                isFree: true
	            }
	        }).when('/successfulRegistration', {
	            templateUrl : 'partials/successfulRegistration.html',
	            access : {
	                isFree : true
	            }
	        }).when('/invalidActivationCode', {
	            templateUrl : 'partials/invalidActivationCode.html',
	            access : {
	                isFree : true
	            }
	        });
	    } ])
	    .factory('LoginResource', function($resource) {
	        return function(newUser) {
	            return $resource('rest/:dest', {}, {
	            login: {method: 'POST', params: {dest:"login"}, headers:{"Authorization": "Basic " + btoa(newUser.userId + ":" + newUser.password)} },
	        });
	    }})
	    .factory('LogoutResource', function($resource) {
	        return $resource('rest/:dest', {}, {
	            logout: {method: 'POST', params: {dest:"logout"}}
	        });
	        })
	    .factory('AdminResource', function($resource) {
	        return $resource('rest/admin/user/:id :dest', {id: "@id"}, {
	        	getUser: {method: 'GET', params: {dest: ""}},
	        	userSave: {method: 'PUT', params: {dest: ""}},
	        	deleteSave: {method: 'DELETE', params: {dest: ""}},
	        	changePassword: {method: 'POST', params: {dest: "password"}},
	            userEnable: {method: 'POST', params: {dest: "enable"}},
	            userDisable: {method: 'POST', params: {dest: "disable"}}
	        });
	    })
	    .factory('UsersResource', function($resource) {
	        return $resource('rest/private/person/:dest', {}, {});
	    })
	    .factory('RegistrationResource', function($resource) {
	        return $resource('rest/registration/:dest', {}, {
	            save: {method: 'POST', params: {dest:""}},
	            activate: {method: 'POST', params: {dest:"activate"}},
	            restorePassword: {method: 'POST', params: {dest:"password/restore"}}
	        });
	    })
	    .factory('SecurityService', function($rootScope) {

	        var SecurityService = function() {
            
	            this.sid = undefined;

	            this.initSession = function(session) {
	                console.log("[INFO] Initializing user session.");
	                console.log("[INFO] SID is :" + session.id);
	                // persist sid, user id to the storage
	                sessionStorage.setItem('sid', session.id);
	                this.sid = session.id;
	            };

	            this.endSession = function() {
	                console.log("[INFO] Ending User Session.");
	                sessionStorage.removeItem('sid');
	                this.sid = undefined;
	                console.log("[INFO] SID removed from session storage.");	                
	            };

	            this.getSid = function() {
	                return sessionStorage.getItem('sid');
	            };

	            this.secureRequest = function(requestConfig) {
	                var sid = this.getSid();

	                if(sid != null && sid != '' && sid != 'undefined') {
	                    requestConfig.headers['Authorization'] = sid;
	                }
	            };
	        };

	        return new SecurityService();
	    })
	    .controller('LoginCtrl', LoginCtrl)
	    .controller('LogoutCtrl', LogoutCtrl)
	    .controller('SignupCtrl', SignupCtrl)
	    .controller('ActivationCtrl', ActivationCtrl);