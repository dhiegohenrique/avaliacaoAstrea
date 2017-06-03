[![Build Status](https://travis-ci.org/dhiegohenrique/avaliacaoastrea.svg?branch=master)](https://travis-ci.org/dhiegohenrique/avaliacaoastrea)

Requisitos:
1) Maven 3 ou superior;
2) Java 7;

Para testar:
mvn test

Para rodar a aplicação:
mvn appengine:devserver

A cada commit, serão realizados testes unitários no Travis, se passarem, o deploy será realizado em http://1-dot-avaliacaoastrea.appspot.com