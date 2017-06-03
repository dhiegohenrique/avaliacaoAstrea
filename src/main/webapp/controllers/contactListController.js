var contactListController;

contactListController = function($scope, $location, contactService) {
	$scope.contacts = [];
	$scope.preDeletedContact = {};

	init();
	function init() {
		contactService.getContacts()
		.then(function(response) {
			$scope.contacts = response;
		});
	};
	
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