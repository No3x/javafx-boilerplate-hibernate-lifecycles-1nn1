package sample.model;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import sample.gui.GUIRepresentable;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by No3x on 03.02.2017.
 */
@Entity
public class Team implements GUIRepresentable {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final StringProperty name = new SimpleStringProperty(this, "name");

    public Team(String s) {
        this.name.set(s);
    }

    public Team() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }
    public IntegerProperty idProperty() {
        return id;
    }
    @Basic
    @Column(name = "name")
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public StringProperty nameProperty() {
        return name;
    }

    private Set<Person> persons;

    @ManyToMany(mappedBy="teams")
    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    @Override
    @Transient
    public String getTitle() {
        return name.getValue();
    }

    public static Callback<Team, Observable[]> extractor() {
        return (Team p) -> new Observable[]{p.nameProperty()};
    }

    @Override
    public String toString() {
        return name.getValue();
    }

}
