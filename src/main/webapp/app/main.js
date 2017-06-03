var app = angular.module('avaliacandidatos', [ 'ngRoute', 'ui.router',
		'ui.bootstrap', 'ui.utils.masks','brasil.filters' ]);

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
				url: 'addeditcontact/:id',
				templateUrl : '/view/contacts/contactaddedit.html'
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
app.$inject = [ '$scope', '$http', '$cookies'];