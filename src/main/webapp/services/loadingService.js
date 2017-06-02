"use strict";

angular.module("avaliacandidatos").service("loadingService", loadingService);

function loadingService($uibModal) {
	console.log("entrou em loadingService");
	
    var service = {};
    var instance;

    service.openModal = function openModal() {
        if (instance) {
            return;
        }
        
        console.log("Abrir modal");

//        instance = $uibModal.open({
//            template: $templateCache.get("/view/loadingModal.html"),
//            size : "sm",
//            backdrop : "static"
//        });
    };

    service.closeModal = function closeModal() {
    	console.log("fechar modal");
    	
        if (!instance) {
            return;
        }

        instance.close();
        instance = null;
    }

    return service;
}