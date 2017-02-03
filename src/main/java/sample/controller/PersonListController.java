package sample.controller;

import com.google.common.collect.Sets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import sample.database.PersonService;
import sample.database.TeamService;
import sample.database.dao.IGenericDAO;
import sample.gui.WindowManager;
import sample.gui.modeladapter.ListViewModelAdapter;
import sample.model.Person;
import sample.model.Team;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PersonListController {

    @FXML
    private ListView<Person> personsListView;

    private ObservableList<Person> personObservableList;

    @Inject
    private IGenericDAO<Person, Integer> personDAO;

    @Inject
    private IGenericDAO<Team, Integer> teamDAO;

    @Inject
    private PersonService personService;

    @Inject
    private TeamService teamService;

    @Inject WindowManager windowManager;

    @FXML
    private void initialize() {
        System.out.println("initialize");
        personsListView.setCellFactory((ListView<Person> param) -> new ListViewModelAdapter<>());


        Team t = teamService.createRandom();
        Person p = personService.createRandom();

        p.setTeams(Sets.newHashSet(t));
        personDAO.add(p);

        personDAO.getAll(); // Print nice table in the logfiles.
        p.getTeams().clear();
        personDAO.update(p);
        personDAO.getAll(); // Print nice table in the logfiles.

        setupBindings();
        setupListeners();
    }

    private void setupBindings() {
        System.out.println("setupBindings");
        personObservableList = FXCollections.observableArrayList( Person.extractor() );
        personObservableList.addAll(personService.getAll());
        personsListView.setItems( personObservableList );
    }

    private void setupListeners() {
        System.out.println("setupListeners");
        personsListView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    windowManager.switchScene(WindowManager.SCENES.PERSON_EDIT_SCENE);
                }
            }
        });
    }

    public void random(ActionEvent event) {
        personObservableList.add(personService.createRandom());
    }

    public Person getPersonSelected() {
        return personsListView.getSelectionModel().getSelectedItem();
    }

}
