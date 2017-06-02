var app = angular.module('avaliacandidatos', [ 'ngRoute', 'ui.router',
		'ui.bootstrap' ]);

app.config([ '$urlRouterProvider', '$stateProvider',
		function($urlRouterProvider, $stateProvider) {

			$stateProvider.state('main', {
				url : '/',
				templateUrl : '/view/main.html'
			});
			$stateProvider.state('main.contacts', {
				url : 'contacts',
				templateUrl : '/view/contacts/contacts.html'
			});
			$stateProvider.state('main.addeditcontact', {
//				url : 'addeditcontact',
				url: 'addeditcontact/:id',
//				url: '/party/:partyID/:partyLocation'
				templateUrl : '/view/contacts/contactaddedit.html'
			});
			// $stateProvider.state('result', {
			// url: 'result',
			// templateUrl: "/view/contacts/sucess.html"
			// });

			$stateProvider.state("error", {
				url : "/error",
				templateUrl : "./../partials/errorMessage.html",
				controller : "errorMessageController"
			}).state("result", {
				parent : "main.addeditcontact",
//				url : "/result",
				templateUrl : "/view/contacts/result.html"
			});

			$urlRouterProvider.otherwise("/")

		} ]);

app.directive('ngBack', function() {
	return function(scope, element, attrs) {
		element.bind('click', function(evt) {
			history.back();
		});
	};
});

mainController = function($scope, $window) {
	return $window.initRoot = function() {
		$scope.backend_ready = true;
		return $scope.$apply();
	};
};

app.controller('mainController', mainController);
app.$inject = [ '$scope', '$http', '$cookies' ];
