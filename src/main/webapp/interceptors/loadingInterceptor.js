angular.module("avaliacandidatos").factory("loadingInterceptor", ["$q", "$rootScope", loadingInterceptor]);

function loadingInterceptor($q, $rootScope) {
    return {
        request: function(config) {
        	$('#loadingModal').modal('show');
        	$rootScope.showLoadingModal = !$rootScope.showLoadingModal;
            return config;
        },

        response: function(response) {
        	$('#loadingModal').modal('hide');
        	$rootScope.showLoadingModal = !$rootScope.showLoadingModal;
            return response;
        }
    };
}