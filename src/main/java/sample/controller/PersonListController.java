package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import sample.database.PersonService;
import sample.database.TeamService;
import sample.gui.WindowManager;
import sample.gui.modeladapter.ListViewModelAdapter;
import sample.model.Person;
import sample.model.PersonTeam;
import sample.model.Team;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;

/**
 * List of {@link Person}.
 */
@Singleton
public class PersonListController {

    @FXML
    private ListView<Person> personsListView;

    @FXML
    private ListView<Team> teamsOfSelected;

    private ObservableList<Person> personObservableList = FXCollections.observableArrayList();

    @Inject
    private PersonService personService;

    @Inject
    private TeamService teamService;

    @Inject
    private WindowManager windowManager;

    /**
     * Initialize the view by loading the persons from the database.
     * Sets a cell factory to keep the ListView in sync when the name is edited in the {@link PersonEditController}.
     */
    @FXML
    private void initialize() {
        System.out.println("initialize");
        personsListView.setCellFactory((ListView<Person> param) -> new ListViewModelAdapter<>());
        personObservableList = FXCollections.observableArrayList();
        personObservableList.addAll(personService.getAll());
        personsListView.setItems( personObservableList );
        setupListeners();
        personsListView.getSelectionModel().selectFirst();
    }

    /**
     * Support doubleclick to a person to open the {@link PersonEditController}.
     */
    private void setupListeners() {
        System.out.println("setupListeners");
        //Bindings.when(teamsOfSelected.itemsProperty().isNotNull()).then( Bindings.bindBidirectional(teamsOfSelected.itemsProperty().) );
        personsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            teamsOfSelected.itemsProperty().bind(newValue.getTeams());
        });

        personsListView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    windowManager.switchScene(WindowManager.SCENES.PERSON_EDIT_SCENE);
                }
            }
        });
    }

    /**
     * Generate a new person and team with random names.
     * Add the person to the team.
     * @param event
     */
    public void addRandomAction(ActionEvent event) {
        Team t = teamService.createRandom();
        Person p = personService.createRandom();
        PersonTeam pt = new PersonTeam();
        pt.setPerson(p);
        pt.setTeam(t);
        pt.setCreatedDate(new Date()); //extra column
        pt.setCreatedBy("no3x"); //extra column
        p.getPersonTeams().add(pt);
        personService.save(p);
        personObservableList.add(p);
    }

    /**
     * Expose selected person to allow edit it in the {@link PersonEditController}.
     * @return
     */
    public Person getPersonSelected() {
        return personsListView.getSelectionModel().getSelectedItem();
    }

}
