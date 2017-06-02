var contactAddEditController;

contactAddEditController = function($scope, $state, $stateParams, contactService) {
	init();
	
	$scope.save = function() {
		if ($scope.contact.name != null && $scope.contact.name != "") {
			$scope.submitted = true;
			$scope.spinner = true;
			
//			console.log("vai inserir: " + JSON.stringify($scope.contact));
			
			var id = $scope.contact.id;
			if (id) {
				update();
			} else {
				insert();
			}
			// Chamar o servlet /contacts com um método 'POST' para salvar um contato no banco de dados.
		}
	};
	
	function insert() {
		contactService.insertContact($scope.contact)
			.then(function(response) {
	            console.log("Resultado insert: " + JSON.stringify(response));
	            $state.go("result");
	            init();
	        });
	//        .finally(function(){
	//            $scope.spinner = false;
	//        });
	};
	
	function update() {
		contactService.updateContact($scope.contact)
			.then(function(response) {
	            $state.go("main.contacts", {}, {reload: "main.contacts"});
//	            init();
	        });
	//        .finally(function(){
	//            $scope.spinner = false;
	//        });
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
		
		var id = $stateParams.id;
		if (id) {
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

angular.module('avaliacandidatos').controller("contactAddEditController", ["$scope", "$state", "$stateParams", "contactService", contactAddEditController]);