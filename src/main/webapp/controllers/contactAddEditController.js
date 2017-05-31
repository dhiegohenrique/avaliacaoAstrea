var contactAddEditController;

contactAddEditController = function($scope, $state, contactService) {
	$scope.contact = {};
	$scope.contact.emails = [''];
	$scope.contact.phones = [''];
	$scope.submitted = false;
	$scope.sucess = false;
	
	$scope.save = function() {

		$scope.submitted = true;

		if ($scope.contact.name != null && $scope.contact.name != "") {
			contactService.insertUpdate($scope.contact)
			.then(function(response) {
//				$state.go("result");
				$scope.sucess = true;
			})
			.finally(function() {
				$scope.spinner = false;
			});
			// Chamar o servlet /contacts com um método 'POST' para salvar um contato no banco de dados.
		}
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

};

angular.module('avaliacandidatos').controller("contactAddEditController", ["$scope", "$state", "contactService", contactAddEditController]);