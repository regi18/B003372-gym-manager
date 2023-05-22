package businessLogic;

import dao.TrainerDAO;
import domainModel.Trainer;


public class TrainersController extends PeopleController<Trainer> {

    public TrainersController(TrainerDAO trainerDAO) {
        super(trainerDAO);
    }

    /**
     * Add a new trainer
     *
     * @return The fiscal code of the newly created trainer
     * @throws Exception bubbles up exceptions of PeopleController::addPerson()
     */
    public String addPerson(String fiscalCode, String name, String surname, float salary) throws Exception {
        Trainer t = new Trainer(fiscalCode, name, surname, salary);
        return super.addPerson(t);
    }
}
