"use strict";

angular.module("avaliacandidatos").service("contactService", contactService);

function contactService($http, $q) {
    function getContacts(city, lang) {
        var deferred = $q.defer();

        $http.get("/contacts")
            .then(function(response) {
                deferred.resolve(response.data);
            }, function(error) {
                deferred.reject(error);
            });

        return deferred.promise;
    };
    
    function insertUpdate(contact) {
    	var deferred = $q.defer();
    	console.log("vai inserir");
    	
    	$http.post("/contacts", contact)
	        .then(function(response) {
	            deferred.resolve(response.data);
	        }, function(error) {
	            deferred.reject(error);
	        });

    	return deferred.promise;
    };
    
    function insertUpdate(contact) {
    	var deferred = $q.defer();
    	
    	$http.post("/contacts", contact)
	        .then(function(response) {
	            deferred.resolve(response.data);
	        }, function(error) {
	            deferred.reject(error);
	        });

    	return deferred.promise;
    };
    
    function deleteContact(contact) {
    	var deferred = $q.defer();
    	
    	$http.delete("/contacts/" + contact.id)
	        .then(function(response) {
	            deferred.resolve(response.data);
	        }, function(error) {
	            deferred.reject(error);
	        });

    	return deferred.promise;
    };

    return {
        "getContacts" : getContacts,
        "insertUpdate" : insertUpdate,
        "deleteContact" : deleteContact
    }
}