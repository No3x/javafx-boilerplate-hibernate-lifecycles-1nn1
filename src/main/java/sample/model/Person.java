package sample.model;

import com.google.common.collect.ImmutableList;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import sample.gui.GUIRepresentable;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by No3x on 01.02.2017.
 */
@Entity
public class Person implements GUIRepresentable, Comparable<Person> {
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final StringProperty name = new SimpleStringProperty(this, "name");
    private Set<PersonTeam> personTeams = new TreeSet<>();

    public Person(String s) {
        this.name.set(s);
    }

    public Person() {}

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.person", cascade=CascadeType.ALL, orphanRemoval = true)
    public Set<PersonTeam> getPersonTeams() {
        return personTeams;
    }

    public void setPersonTeams(Set<PersonTeam> personTeams) {
        this.personTeams = personTeams;
    }

    @Override
    @Transient
    public String getTitle() {
        return name.getValue();
    }

    public static Callback<Person, Observable[]> extractor() {
        return (Person p) -> new Observable[]{p.nameProperty()};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (getId() != null ? !getId().equals(person.getId()) : person.getId() != null) return false;
        if (getName() != null ? !getName().equals(person.getName()) : person.getName() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Transient
    public ImmutableList<Team> getTeams() {
        return new ImmutableList.Builder<Team>().addAll(personTeams.stream().map(PersonTeam::getTeam).iterator()).build();
    }

    public void addTeam(Team team, String createdBy, Date createdDate) {
        final PersonTeam personTeam = new PersonTeam(this, team);
        personTeam.setCreatedBy(createdBy);
        personTeam.setCreatedDate(createdDate);
        personTeams.add(personTeam);
        team.getPersonTeams().add( personTeam );
    }

    public void removeTeam(Team team) {
        PersonTeam personTeam = new PersonTeam( this, team );
        team.getPersonTeams().remove( personTeam );
        personTeams.remove( personTeam );
        personTeam.setPerson( null );
        personTeam.setTeam( null );
    }

    @Override
    public String toString() {
        return name.getValue();
    }

    @Override
    public int compareTo(Person o) {
        //TODO: implement sort
        return 0;
    }
}
