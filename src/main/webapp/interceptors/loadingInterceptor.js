"use strict";

angular.module("avaliacandidatos").factory("loadingInterceptor", ["$q", "$injector", loadingInterceptor]);

function loadingInterceptor($q, $injector) {
    return {
        request: function(config) {
            if (config.url.indexOf("/result/") < 0) {
                return config;
            }

            var loadingService = $injector.get("loadingService");
            loadingService.openModal();
            return config;
        },

        requestError: function(rejection) {
            var loadingService = $injector.get("loadingService");
            loadingService.closeModal();
            return $q.reject(rejection);
        },

        response: function(response) {
            if (response.config.url.indexOf("/result/") < 0) {
                return response;
            }

            var loadingService = $injector.get("loadingService");
            loadingService.closeModal();
            return response;
        },

        responseError: function(rejection) {
            var loadingService = $injector.get("loadingService");
            loadingService.closeModal();
            return $q.reject(rejection);
        }
    };
}