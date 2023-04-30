package controllers;

import dao.TrainerDAO;
import models.Trainer;


public class TrainersController extends PeopleController<Trainer> {

    private final TrainerDAO trainerDAO;

    public TrainersController(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
        // Load trainers from the DAO
        for (Trainer t : trainerDAO.getAll()) super.addPerson(t);
    }

    public void addPerson(String fiscalCode, String name, String surname, float salary) throws IllegalArgumentException {
        Trainer t = new Trainer(fiscalCode, name, surname, salary);
        super.addPerson(t);
        trainerDAO.insert(t);
    }
}
