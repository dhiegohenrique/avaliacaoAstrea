"use strict";

//$modal

angular.module("avaliacandidatos").factory("loadingInterceptor", ["$q", loadingInterceptor]);

//angular.module("avaliacandidatos").factory("loadingInterceptor", loadingInterceptor);
//loadingInterceptor.$inject = ['$uibModal'];

function loadingInterceptor($q) {
    return {
        request: function(config) {
        	console.log("request: " + config.url);
//        	console.log("entrou aqui1: " + $uibModal);
        	
//        	var injector=angular.injector(["avaliacandidatos", "ng"]);
//			console.log("entrou aqui1: " + injector);
//			console.log("entrou aqui2: " + JSON.stringify(injector));
//        	console.log("entrou aqui2: " + injector.get("loadingService"));
        	
//            if (config.url.indexOf("/result/") < 0) {
//                return config;
//            }

//        	console.log("$injector1: " + $injector);
//        	console.log("$injector2: " + JSON.stringify($injector));
        	
//            var loadingService = $injector.get("loadingService");
//            console.log("reqLoading: " + loadingService);
//            
//            loadingService.openModal();
            return config;
        },

        requestError: function(rejection) {
//            var loadingService = $injector.get("loadingService");
//            loadingService.closeModal();
            return $q.reject(rejection);
        },

        response: function(response) {
        	console.log("response: " + response.config.url);
        	
//            if (response.config.url.indexOf("/result/") < 0) {
//                return response;
//            }

//            var loadingService = $injector.get("loadingService");
//            console.log("respLoading: " + loadingService);
//            
//            loadingService.closeModal();
            return response;
        },

        responseError: function(rejection) {
//            var loadingService = $injector.get("loadingService");
//            loadingService.closeModal();
            return $q.reject(rejection);
        }
    };
}