var contactListController;

contactListController = function($scope, $location, contactService) {
	$scope.contacts = [];
	$scope.preDeletedContact = {};

//	$scope.init = function() {
//	$scope.listAllContacts();
//	};
	
	init();
	function init() {
		console.log("vai carregar contatos");
		
		contactService.getContacts()
		.then(function(response) {
			$scope.contacts = response;
			console.log("Carregou");
		});
	};
	
//	$scope.listAllContacts = function() {
//		contactService.getContacts()
//			.then(function(response) {
//				$scope.contacts = response;
//			});
//	};

	$scope.preDelete = function(contact) {
		$scope.preDeletedContact = contact;
		$('#myModal').modal('show');
	};

	$scope.deleteContact = function() {
		if (!$scope.preDeletedContact) {
			return;
		}
		
		contactService.deleteContact($scope.preDeletedContact)
			.then(function(response) {
				$('#myModal').modal('hide');
				
				var index = $scope.contacts.indexOf($scope.preDeletedContact);
				$scope.contacts.splice(index, 1);
				
				$('#myModal').on('hidden.bs.modal', function () {
//					$state.go("main.contacts", {}, {reload: "main.contacts"});
//					$state.go("main.contacts", {}, {reload: true});
//					$location.path("/contacts").replace();
//					console.log("fechou modal");
//					$('.modal-backdrop').remove();
//					$location.path("/contacts");
//					var index = $scope.contacts.indexOf($scope.preDeletedContact);
//					console.log("index: " + index);
//					
//					console.log("tamanho1: " + $scope.contacts.length);
//					$scope.contacts.splice(index, 1);
//					console.log("tamanho2: " + $scope.contacts.length);
//					$scope.$apply();
				})
			});
	};

	$scope.bday = function(c) {
		if(c.birthDay==null || c.birthDay == ""){
			return "";
		} else {
			return c.birthDay + "/" + c.birthMonth + "/" + c.birthYear;
		}
	};
};

angular.module('avaliacandidatos').controller("contactListController", ["$scope", "$location", "contactService", contactListController]);