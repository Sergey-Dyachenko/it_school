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
            return $resource('rs/private/:dest', {}, {
            login: {method: 'POST', params: {dest:"authenticate"}, headers:{"Authorization": "Basic " + btoa(newUser.userId + ":" + newUser.password)} },
        });
    }})
    .factory('LogoutResource', function($resource) {
        return $resource('rs/private/:dest', {}, {
            logout: {method: 'POST', params: {dest:"logout"}}
        });
        })
     .factory('AdminResource', function($resource) {
        return $resource('rs/private/account/:dest', {}, {
            enableAccount: {method: 'POST', params: {dest:"enableAccount"}},
            disableAccount: {method: 'POST', params: {dest:"disableAccount"}}
        });
    })
    .factory('UsersResource', function($resource) {
        return $resource('rs/private/person/:dest', {}, {});
    })
    .factory('RegistrationResource', function($resource) {
        return $resource('rs/register/:dest', {}, {
            activation: {method: 'POST', params: {dest:"activation"}}
        });
    })
    .factory('SecurityService', function($rootScope) {

        var SecurityService = function() {
            var userName, password;

            this.initSession = function(response) {
                console.log("[INFO] Initializing user session.");
                console.log("[INFO] Token is :" + response.authctoken);
                console.log("[INFO] Token Stored in session storage.");
                // persist token, user id to the storage
                sessionStorage.setItem('token', response.authctoken);
            };

            this.endSession = function() {
                console.log("[INFO] Ending User Session.");
                sessionStorage.removeItem('token');
                console.log("[INFO] Token removed from session storage.");
            };

            this.getToken = function() {
                return sessionStorage.getItem('token');
            };

            this.secureRequest = function(requestConfig) {
                var token = this.getToken();

                if(token != null && token != '' && token != 'undefined') {
                    console.log("[INFO] Securing request.");
                    console.log("[INFO] Setting x-session-token header: " + token);
                    requestConfig.headers['Authorization'] = 'Token ' + token;
                }
            };
        };

        return new SecurityService();
    });

// controllers definition
function LoginCtrl($scope, LoginResource, SecurityService, $location, $rootScope) {
    $scope.newUser = {};

    $scope.login = function() {
        if ($scope.newUser.userId != undefined && $scope.newUser.password != undefined) {
            LoginResource($scope.newUser).login($scope.newUser,
                function (data) {
            		var token = data.authctoken;
            		
            		if(token != null && token != '' && token != 'undefined') {
	                    SecurityService.initSession(data);
	                    $location.path( "/home" );
            		} else {
            			$scope.newUser.password = undefined;
            			$rootScope.messages = ["Authorization failed ! Please, check your login / password ... "];
            		}
                }
            );
        }
    };

    $scope.redirectoToSignUp = function() {
        $location.path( "/signup" );
    };

}

function SignupCtrl($scope, $http, RegistrationResource, $q, $location, $timeout) {
    $scope.register = function() {
        if($scope.newUser.password != $scope.newUser.passwordConfirmation) {
            $scope.errors = {passwordConfirmation : "Password Mismatch !!!"};
            return;
        }

        RegistrationResource.save($scope.newUser, function(data) {
            $location.path("/successfulRegistration");
        });
    };
}

function ActivationCtrl($scope, $routeParams, RegistrationResource, SecurityService, $location) {
    var ac = $routeParams.activationCode;
    $scope.activate = function() {
        RegistrationResource.activation(JSON.stringify(ac), function(data) {
            SecurityService.initSession(data);
            $location.path( "/login" );
        }, function(result) {
            $location.path( "/invalidActivationCode" );
        });

    };

    $scope.activate();
}