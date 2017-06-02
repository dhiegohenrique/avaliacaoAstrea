"use strict";

angular.module("avaliacandidatos").service("loadingService", ["$uibModal", "$templateCache", loadingService]);

function loadingService($uibModal, $templateCache) {
    var service = {};
    var instance;

    service.openModal = function openModal() {
        if (instance) {
            return;
        }
        
        console.log("Abrir modal");

        instance = $uibModal.open({
            template: $templateCache.get("/view/loadingModal.html"),
            size : "sm",
            backdrop : "static"
        });
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