package sample.database;

import org.kohsuke.randname.RandomNameGenerator;
import sample.database.dao.IGenericDAO;
import sample.model.Team;

import javax.inject.Inject;
import java.util.List;
import java.util.Random;

/**
 * Created by No3x on 01.02.2017.
 */
public class TeamService {

    private final IGenericDAO<Team, Integer> teamDAO;

    private final RandomNameGenerator randomNameGenerator = new RandomNameGenerator(new Random().nextInt());

    @Inject
    public TeamService(IGenericDAO<Team, Integer> teamDAO) {
        this.teamDAO = teamDAO;
    }

    public List<Team> getAll() {
        return teamDAO.getAll();
    }

    public Team createRandom() {
        final Team team = new Team(randomNameGenerator.next());
        teamDAO.add(team);
        return team;
    }
}
