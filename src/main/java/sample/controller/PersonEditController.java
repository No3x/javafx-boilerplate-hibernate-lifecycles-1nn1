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
import sample.model.Person;
import sample.model.Team;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.stream.Collectors;

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

    private Person personSelected;

    @FXML
    private void initialize() {
        System.out.println("initialize");
        personSelected = personListController.getPersonSelected();
        teamCombobox.getItems().addAll(teamService.getAll());
        teamCombobox.getSelectionModel().selectFirst();
        teamListview.getItems().addAll(personSelected.getTeams());
        name.setText(personSelected.getName());
    }

    public void saveAction(ActionEvent event) {
        personSelected.setName(name.getText());
        personSelected.setTeams(teamListview.getItems().stream().collect(Collectors.toSet()));
        personService.save(personSelected);
        closeWindow(event);
    }

    public void addTeamAction(ActionEvent event) {
        final Team selectedItem = teamCombobox.getSelectionModel().getSelectedItem();
        // Do not add them more than once to the ListView
        if(!teamListview.getItems().contains(selectedItem)) {
            teamListview.getItems().add(selectedItem);
        }
    }

    public void removeTeamAction(ActionEvent event) {
        teamListview.getItems().remove( teamListview.getSelectionModel().getSelectedItem() );
    }

    private void closeWindow(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
