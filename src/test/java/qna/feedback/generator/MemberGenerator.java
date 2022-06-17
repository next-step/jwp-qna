package qna.feedback.generator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.feedback.entity.lazy.Member;
import qna.feedback.entity.lazy.Team;
import qna.feedback.repository.MemberRepository;

@TestConstructor(autowireMode = AutowireMode.ALL)
public class MemberGenerator {

    private final MemberRepository memberRepository;

    public MemberGenerator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public static int COUNTER = 0;
    public static final String NAME = "member name";
    public static final int AGE = 32;

    public Member generateMember(Team team) {
        COUNTER++;
        return new Member(NAME + COUNTER, AGE + COUNTER, team);
    }

    public Member savedMember(Team savedTeam) {
        return memberRepository.saveAndFlush(generateMember(savedTeam));
    }

    public List<Member> savedMembers(Team savedTeam, int count) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            members.add(generateMember(savedTeam));
        }
        return memberRepository.saveAll(members);
    }
}
