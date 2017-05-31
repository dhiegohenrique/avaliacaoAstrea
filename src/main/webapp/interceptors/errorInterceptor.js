"use strict";

angular.module("avaliacandidatos").factory("errorInterceptor", ["$q", "$location", "$rootScope", errorInterceptor]);

function errorInterceptor($q, $location, $rootScope) {
    return {
        responseError: function(rejection) {
            $rootScope.statusCode = rejection.status;
            $location.path("/error");
            return $q.reject(rejection);
        }
    };
}