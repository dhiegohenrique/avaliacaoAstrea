var contactAddEditController;

contactAddEditController = function($scope, $location, $stateParams, contactService) {
	var invalidEmails = [];
	init();
	
	$scope.save = function() {
		$scope.formContact.$setDirty();
		
		if ($scope.formContact.$invalid || invalidEmails.length > 0) {
			return;
		}
		
		var id = $scope.contact.id;
		if (id) {
			update();
		} else {
			insert();
		}
	};
	
	function insert() {
		$scope.contact.phones = removeEmptyElements($scope.contact.phones);
		$scope.contact.emails = removeEmptyElements($scope.contact.emails);
		
		contactService.insertContact($scope.contact)
			.then(function(response) {
				$location.path("/contacts").replace();
	        });
	};
	
	function removeEmptyElements(arr) {
		return arr.filter(Number); 
	}
	
	function update() {
		contactService.updateContact($scope.contact)
			.then(function(response) {
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
		$scope.sucess = false;
		$scope.spinner = false;
		$scope.title = "Adicionar contato";
		$scope.isEmailValid = true;
		
		var id = $stateParams.id;
		if (id) {
			$scope.title = "Editar contato";
			fillInContact(id);
		} else {
			$("#firstname").focus();
		}
	};
	
	function fillInContact(id) {
		contactService.getContactById(id)
			.then(function(response) {
	            $scope.contact = response;
	        });
	}
	
	$scope.validateCpf = function() {
		var cpf = $("#cpf").val();
		cpf = cpf.replace(/[^\d]+/g,'');
		if (cpf.length < 11) {
			$("#cpf").val("");
		}
	};
	
	$scope.validatePhone = function($event) {
		var id = $event.target.id;
		var phone = $("#" + id).val();
		phone = phone.replace(/[^\d]+/g,'');
		if (phone.length < 10) {
			$("#" + id).val("");
		}
	};
	
	$scope.validateDay = function() {
		var day = $("#day").val();
		day = parseInt(day);
		if (day < 1 || day > 31) {
			$("#day").val("");
		}
	};
	
	$scope.validateMonth = function() {
		var month = $("#month").val();
		month = parseInt(month);
		if (month < 1 || month > 12) {
			$("#month").val("");
		}
	};
	
	$scope.validateYear = function() {
		var year = $("#year").val();
		if (year.length < 4) {
			$("#year").val("");
		}
	};
	
	$scope.validateEmail = function($event) {
		var regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		
		var id = $event.target.id;
		var email = $("#" + id).val();
		var emailId = parseInt(id.replace("email", ""));
		var index = invalidEmails.indexOf(emailId);
		
		if (email.length === 0) {
			$("#alertEmail" + emailId).hide();
			return;
		}
		
		if (!regex.test(email)) {
			if (index < 0) {
				invalidEmails.push(emailId);
			}
			
			$("#alertEmail" + emailId).show();
			return;
		}
		
		if (index >= 0) {
			invalidEmails.splice(index, 1);
		}
		
		$("#alertEmail" + emailId).hide();
	}
};

angular.module('avaliacandidatos').controller("contactAddEditController", ["$scope", "$location", "$stateParams", "contactService", contactAddEditController]);