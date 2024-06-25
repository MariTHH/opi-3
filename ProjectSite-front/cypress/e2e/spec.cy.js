describe('template spec', () => {
    it('passes', () => {
        cy.visit('https://example.cypress.io')
    })
})

describe('Registration Test', () => {
    it('Should register a new user', () => {

        cy.intercept('GET', 'http://127.0.0.1:3001/events');
        cy.visit('http://localhost:3000/');


        cy.get('input[type="text"]').type('user12a');
        cy.get('input[type="password"]').type('testPassword1');
        cy.get('a').contains('log in').click();


        cy.url().should('include', 'http://localhost:3000/profilepage');
    });
});

describe('Weight test', () => {
    it('Should create weight', () => {

        cy.visit('http://localhost:3000/profilepage');
        cy.get('.profile').click();

        cy.get('.weight').type('75');
        cy.get('#man').check();
        cy.get('.save').contains('Save').click();
    });
});

describe('Calendar Functionality Test', () => {
    it('Should create and verify an event', () => {
        cy.visit('http://localhost:3000/schedule');

        cy.get('button').contains('Day').click();

        cy.get('button').contains('Create').click();

        cy.get('button').contains('Exercise').click();
        cy.get('button').contains('press').click();
        cy.get('button').contains('Create').click();
        cy.get('button').contains('Month').click();
    });
});
