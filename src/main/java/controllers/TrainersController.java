package controllers;

import models.Trainer;


public class TrainersController extends PeopleController<Trainer> {
    public void addPerson(String fiscalCode, String name, String surname, float salary) throws IllegalArgumentException {
        super.addPerson(new Trainer(fiscalCode, name, surname, salary));
    }
}
