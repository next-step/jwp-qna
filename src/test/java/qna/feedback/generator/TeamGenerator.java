package qna.feedback.generator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.feedback.entity.lazy.Team;
import qna.feedback.repository.TeamRepository;

@TestConstructor(autowireMode = AutowireMode.ALL)
public class TeamGenerator {

    private final TeamRepository teamRepository;

    public TeamGenerator(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public static int COUNTER = 0;
    public static final String TEAM_NAME = "team name";

    public Team generateTeam() {
        COUNTER++;
        return new Team(TEAM_NAME + COUNTER);
    }

    public Team savedTeam() {
        return teamRepository.save(generateTeam());
    }

    public List<Team> savedTeams(int count) {
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            teams.add(generateTeam());
        }
        return teamRepository.saveAll(teams);
    }
}
