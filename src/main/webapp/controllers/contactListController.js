var contactListController;

contactListController = function($scope, contactService) {
	$scope.contacts = [];
	$scope.preDeletedContact = {};

	$scope.init = function() {
		$scope.listAllContacts();
	};
	
	$scope.listAllContacts = function() {
		$scope.spinner = true;
		
		contactService.getContacts()
			.then(function(response) {
				$scope.contacts = response;
			})
			.finally(function() {
				$scope.spinner = false;
			});
		// Chamar o servlet /contacts com um método 'GET' para listar os contatos do banco de dados.
	};

	$scope.preDelete = function(contact) {
		$scope.preDeletedContact = contact;
		$('#myModal').modal('show');
	};

	$scope.deleteContact = function() {
		console.log("vai excluir");
		
		if($scope.preDeletedContact != null) {
			console.log("excluir: " + $scope.preDeletedContact);
			
			contactService.deleteContact($scope.preDeletedContact)
				.then(function(response) {
					
				})
				.finally(function() {
					$scope.spinner = false;
				});

			// Chamar o servlet /contacts com um método 'DELETE' para deletar um contato do banco de dados passando um parâmetro de identificação.
		}
	};

	$scope.bday = function(c) {
		if(c.birthDay==null || c.birthDay == ""){
			return "";
		} else {
			return c.birthDay + "/" + c.birthMonth + "/" + c.birthYear;
		}
	};
};

angular.module('avaliacandidatos').controller("contactListController", ["$scope", "contactService", contactListController]);