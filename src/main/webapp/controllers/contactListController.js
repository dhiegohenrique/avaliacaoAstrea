var contactListController;

contactListController = function($scope, $state, contactService) {
	$scope.contacts = [];
	$scope.preDeletedContact = {};

	$scope.init = function() {
		$scope.listAllContacts();
	};
	
	$scope.listAllContacts = function() {
		$scope.spinner = true;
		$('#loadingModal').modal('show');
		
		contactService.getContacts()
			.then(function(response) {
				$scope.contacts = response;
				$('#loadingModal').modal('hide');
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
		if (!$scope.preDeletedContact) {
			return;
		}
		
		contactService.deleteContact($scope.preDeletedContact)
			.then(function(response) {
				$('#myModal').modal('hide');
//				$('#myModal').close();
//				$('#myModal').hide();
//				$state.go("main.contacts", {}, {reload: "main.contacts"});
				
				$('#myModal').on('hidden.bs.modal', function () {
					console.log("FECHOU A MODAL");
					$state.go("main.contacts", {}, {reload: "main.contacts"});
				})
			})
			.finally(function() {
				$scope.spinner = false;
			});

		// Chamar o servlet /contacts com um método 'DELETE' para deletar um contato do banco de dados passando um parâmetro de identificação.
	};

	$scope.bday = function(c) {
		if(c.birthDay==null || c.birthDay == ""){
			return "";
		} else {
			return c.birthDay + "/" + c.birthMonth + "/" + c.birthYear;
		}
	};
};

angular.module('avaliacandidatos').controller("contactListController", ["$scope", "$state", "contactService", contactListController]);