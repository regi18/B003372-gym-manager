package businessLogic;

import dao.TrainerDAO;
import domainModel.Trainer;


public class TrainersController extends PeopleController<Trainer> {

    public TrainersController(TrainerDAO trainerDAO) {
        super(trainerDAO);
    }

    public void addPerson(String fiscalCode, String name, String surname, float salary) throws IllegalArgumentException {
        Trainer t = new Trainer(fiscalCode, name, surname, salary);
        super.addPerson(t);
    }
}
