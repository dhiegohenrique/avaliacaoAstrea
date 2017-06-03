"use strict";

//$modal

angular.module("avaliacandidatos").factory("loadingInterceptor", ["$q", "$rootScope", loadingInterceptor]);

//angular.module("avaliacandidatos").factory("loadingInterceptor", loadingInterceptor);
//loadingInterceptor.$inject = ['$uibModal'];

function loadingInterceptor($q, $rootScope) {
    return {
        request: function(config) {
        	$('#loadingModal').modal('show');
        	
//        	if (!$rootScope.showLoadingModal) {
//        		$rootScope.showLoadingModal = true;
//        	}
        	$rootScope.showLoadingModal = !$rootScope.showLoadingModal;
            return config;
        },

        response: function(response) {
        	$('#loadingModal').modal('hide');
        	
//        	if ($rootScope.showLoadingModal) {
//        		$rootScope.showLoadingModal = false;
//        	}
        	
        	$rootScope.showLoadingModal = !$rootScope.showLoadingModal;
            return response;
        }
    };
}