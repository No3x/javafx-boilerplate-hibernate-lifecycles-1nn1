package sample.database;

import org.kohsuke.randname.RandomNameGenerator;
import sample.database.dao.IGenericDAO;
import sample.model.Person;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by No3x on 01.02.2017.
 */
public class PersonService {

    private final IGenericDAO<Person, Integer> personDAO;

    private final RandomNameGenerator randomNameGenerator = new RandomNameGenerator(0);

    @Inject
    public PersonService(IGenericDAO<Person, Integer> personDAO) {
        this.personDAO = personDAO;
    }

    public List<Person> getAll() {
        return personDAO.getAll();
    }

    public Person createRandom() {
        final Person person = new Person(randomNameGenerator.next());
        personDAO.add(person);
        return person;
    }

    public void save(Person person) {
        personDAO.saveOrUpdate(person);
    }
}
