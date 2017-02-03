package sample.controller;

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

    public void addRandomAction(ActionEvent event) {
        Team t = teamService.createRandom();
        Person p = personService.createRandom();
        p.getTeams().add(t);
        personDAO.saveOrUpdate(p);
        personObservableList.add(p);
    }

    public Person getPersonSelected() {
        return personsListView.getSelectionModel().getSelectedItem();
    }

}
