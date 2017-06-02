"use strict";

angular.module("avaliacandidatos").service("contactService", contactService);

function contactService($http, $q) {
    function getContacts() {
        var deferred = $q.defer();

        $http.get("/contacts")
            .then(function(response) {
                deferred.resolve(response.data);
            }, function(error) {
                deferred.reject(error);
            });

        return deferred.promise;
    };
    
    function getContactById(id) {
        var deferred = $q.defer();

        $http.get("/contacts/" + id)
            .then(function(response) {
                deferred.resolve(response.data);
            }, function(error) {
                deferred.reject(error);
            });

        return deferred.promise;
    };
    
    function insertContact(contact) {
    	var deferred = $q.defer();
    	
    	$http.post("/contacts", contact)
	        .then(function(response) {
	            deferred.resolve(response.data);
	        }, function(error) {
	            deferred.reject(error);
	        });

    	return deferred.promise;
    };
    
    function insertContact(contact) {
    	var deferred = $q.defer();
    	
    	$http.post("/contacts", contact)
	        .then(function(response) {
	            deferred.resolve(response.data);
	        }, function(error) {
	            deferred.reject(error);
	        });

    	return deferred.promise;
    };
    
    function updateContact(contact) {
    	var deferred = $q.defer();
    	
    	$http.put("/contacts/" + contact.id, contact)
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
        "insertContact" : insertContact,
        "deleteContact" : deleteContact,
        "getContactById" : getContactById,
        "updateContact" : updateContact
    }
}