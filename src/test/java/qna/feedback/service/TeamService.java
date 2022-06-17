package qna.feedback.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.feedback.entity.lazy.Team;
import qna.feedback.repository.TeamRepository;

@Service
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team getTeamWithJpqlJoin(Long id) {
        return teamRepository.getTeamByIdByJpqlJoin(id);
    }
}
