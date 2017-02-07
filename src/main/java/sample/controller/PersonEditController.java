package sample.controller;

/**
 * Created by No3x on 01.02.2017.
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.database.PersonService;
import sample.database.TeamService;
import sample.database.dao.IGenericDAO;
import sample.model.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.Optional;

/**
 * Controller to edit a person.
 * The name of the persons and the teams the person attends are loaded once from the database.
 * Any changes to this data are applied to the form data (TextField and the ListView) only.
 * On {@link #saveAction(ActionEvent)} by just setting the modified form data to the person object.
 */
@Singleton
public class PersonEditController {

    @FXML
    private TextField name;

    @FXML
    private ListView<Team> teamListview;

    @FXML
    private ComboBox<Team> teamCombobox;

    @Inject
    private PersonService personService;

    @Inject
    private TeamService teamService;

    @Inject
    private PersonListController personListController;

    @Inject
    private IGenericDAO<PersonTeam, Integer> personTeamDAO;

    private Person personSelected;

    /**
     * Initialize the view by loading the person data and the teams from the database.
     * Then setting the loaded data to the form data.
     * Select the first item in the comboBox for convenience.
     * In contrast to the {@link PersonListController} we do not set a cellFactory as there are no dependent view elements we need to keep in sync.
     */
    @FXML
    private void initialize() {
        System.out.println("initialize");
        personSelected = personListController.getPersonSelected();
        teamCombobox.getItems().addAll(teamService.getAll());
        teamCombobox.getSelectionModel().selectFirst();
        teamListview.getItems().addAll(personSelected.getTeams());
        name.setText(personSelected.getName());
    }

    /**
     * Apply the the form data to the person object and persist.
     * @param event
     */
    public void saveAction(ActionEvent event) {
        personSelected.setName(name.getText());
        new ListCompare<>(personSelected.getTeams(), teamListview.getItems(), new ISyncAction<Team>() {
            @Override
            public void added(Iterable<? extends Team> added) {
                added.forEach( team -> {
                    personSelected.addTeam(team, "Sync", new Date());
                    personService.save(personSelected);
                });
            }

            @Override
            public void removed(Iterable<? extends Team> removed) {
                removed.forEach(team -> {
                    //personService.save(personSelected);
                    final Optional<PersonTeam> optPersonTeam = personSelected.getPersonTeams()
                                                                             .stream()
                                                                             .filter(input -> input.getTeam().equals(team))
                                                                             .findAny();
                    optPersonTeam.ifPresent(personTeam -> {
                        //personTeam.getPerson().getPersonTeams().remove(personTeam);
                        //personTeam.getTeam().getPersonTeams().remove(personTeam);
                        //personTeamDAO.remove(personTeam);
                    });
                    personSelected.removeTeam(team);
                    personService.save(personSelected);
                    /*final Optional<PersonTeam> optPersonTeam = personSelected.getPersonTeams()
                                                                   .stream()
                                                                   .filter(input -> input.getTeam().equals(team))
                                                                   .findAny();
                    optPersonTeam.ifPresent(personTeam -> {
                        personTeam.setPerson(null);
                        personTeam.setTeam(null);
                        team.getPersonTeams().remove(personTeam);
                        personSelected.removeTeam(team);
                        personTeamDAO.remove(personTeam);
                    });*/
                });
            }
        }).syncToDatabase();
        //personSelected.setPersonTeams(teamListview.getItems().stream().map().collect(Collectors.toSet()));
        //personService.save(personSelected);
        closeWindow(event);
    }

    /**
     * Add button action.
     * Add a team from the ComboBox to the ListView.
     * Avoids adding a person more than once.
     * @param event
     */
    public void addTeamAction(ActionEvent event) {
        final Team selectedItem = teamCombobox.getSelectionModel().getSelectedItem();
        // Do not add them more than once to the ListView
        if(!teamListview.getItems().contains(selectedItem)) {
            teamListview.getItems().add(selectedItem);
        }
    }

    /**
     * Remove button action.
     * Remove the team from the ListView.
     * @param event
     */
    public void removeTeamAction(ActionEvent event) {
        teamListview.getItems().remove( teamListview.getSelectionModel().getSelectedItem() );
    }

    private void closeWindow(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
