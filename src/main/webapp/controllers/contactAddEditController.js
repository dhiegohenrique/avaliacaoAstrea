var contactAddEditController;

contactAddEditController = function($scope, $location, $stateParams, contactService) {
	init();
	
	$scope.save = function() {
		if ($scope.contact.name != null && $scope.contact.name != "") {
			$scope.submitted = true;
			
			var id = $scope.contact.id;
			if (id) {
				update();
			} else {
				insert();
			}
		}
	};
	
	function insert() {
		contactService.insertContact($scope.contact)
			.then(function(response) {
//				$state.go("main.contacts", {}, {reload: "main.contacts"});
//				$location.path("/contacts");
				$location.path("/contacts").replace();
	        });
	};
	
	function update() {
		contactService.updateContact($scope.contact)
			.then(function(response) {
//	            $state.go("main.contacts", {}, {reload: "main.contacts"});
//				$location.path("/contacts");
				$location.path("/contacts").replace();
	        });
	};

	$scope.addMorePhones = function() {
		$scope.contact.phones.push('');
	}; 

	$scope.addMoreEmails = function() {
		$scope.contact.emails.push('');
	};

	$scope.deletePhone = function(index){
		if (index > -1) {
    		$scope.contact.phones.splice(index, 1);
		}

		if ($scope.contact.phones.length < 1){
			$scope.addMorePhones();
		}
	};

	$scope.deleteEmail = function(index){
		if (index > -1) {
    		$scope.contact.emails.splice(index, 1);
		}

		if ($scope.contact.emails.length < 1){
			$scope.addMoreEmails();
		}
	};
	
	function init() {
		$scope.contact = {};
		$scope.contact.emails = [''];
		$scope.contact.phones = [''];
		$scope.submitted = false;
		$scope.sucess = false;
		$scope.spinner = false;
		$scope.title = "Adicionar contato";
		
		var id = $stateParams.id;
		if (id) {
			$scope.title = "Editar contato";
			fillInContact(id);
		}
	};
	
	function fillInContact(id) {
		contactService.getContactById(id)
			.then(function(response) {
	            $scope.contact = response;
	        });
	}
};

angular.module('avaliacandidatos').controller("contactAddEditController", ["$scope", "$location", "$stateParams", "contactService", contactAddEditController]);