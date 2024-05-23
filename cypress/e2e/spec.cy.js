
describe('template spec', () => {
  it('passes', () => {
    cy.visit('https://example.cypress.io');
  });
});

describe('Registration Test', () => {
  it('Should register a new user', () => {
    // Заходим на страницу регистрации
    cy.intercept('GET', 'http://localhost:3001/events');
    cy.visit('http://localhost:3000/');

    // Заполняем форму регистрации
    cy.get('input[type="text"]').type('user12a');
    cy.get('input[type="password"]').type('testPassword1');
    // Нажимаем кнопку "Register"
    cy.get('a').contains('log in').click();
    cy.intercept('GET', 'http://localhost:3001/events');

    // Проверяем, что регистрация прошла успешно
    cy.url().should('include', 'http://localhost:3000/profilepage');
  });
});

describe('Weight test', () => {
  it('Should create weight', () => {
    // Заходим на страницу регистрации
    cy.intercept('GET', 'http://localhost:3001/events');
    cy.visit('http://localhost:3000/profilepage');
    cy.get('.profile').click();

    // Заполняем форму веса
    cy.get('.weight').type('75');
    cy.get('#man').check();
    cy.get('.save').contains('Save').click();

    // Проверяем, что вес и пол отображаются правильно
    cy.get('.weight').should('contain', '75');
  });
});

describe('Calendar Functionality Test', () => {
  it('Should create and verify an event', () => {
    // Заходим на страницу календаря
    cy.visit('http://localhost:3000/schedule');

    cy.get('button').contains('Day').click();

    // Нажимаем на кнопку создания нового события
    cy.get('button').contains('Create').click();

    // Выбираем упражнение
    cy.get('button').contains('Exercise').click();
    cy.get('button').contains('press').click();
    cy.get('button').contains('Create').click();
    cy.get('button').contains('Month').click();

    // Проверяем, что событие отображается в календаре
    cy.get('.calendar-event').should('contain', 'press');
  });
});
