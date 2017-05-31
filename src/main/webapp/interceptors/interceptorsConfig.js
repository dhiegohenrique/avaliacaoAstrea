"use strict";

angular.module("avaliacandidatos").config(["$httpProvider", interceptorsConfig]);

function interceptorsConfig($httpProvider) {
    $httpProvider.interceptors.push("errorInterceptor");
    $httpProvider.interceptors.push("loadingInterceptor");
};