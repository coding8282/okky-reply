package org.okky.reply.domain.repository;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.okky.reply.TestMother;
import org.okky.reply.domain.model.ReplyVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static lombok.AccessLevel.PRIVATE;
import static org.junit.Assert.*;
import static org.okky.reply.domain.model.VotingDirection.DOWN;
import static org.okky.reply.domain.model.VotingDirection.UP;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@FieldDefaults(level = PRIVATE)
public class ReplyVoteRepositoryTest extends TestMother {
    @Autowired
    ReplyVoteRepository repository;

    @Test
    public void replyId와_voterId_유니크_제약조건_확인() {
        expect(DataIntegrityViolationException.class);

        repository.saveAndFlush(new ReplyVote("r1", "m1", UP));
        repository.saveAndFlush(new ReplyVote("r1", "m1", UP));
    }

    @Test
    public void wasAlreadyVoted_투표했다면_true() {
        repository.save(new ReplyVote("r1", "m1", UP));
        repository.save(new ReplyVote("r1", "m2", DOWN));
        boolean alreadyVoted = repository.wasAlreadyVoted("r1", "m2");

        assertTrue("레코드가 존재하므로 true여야 한다.", alreadyVoted);
    }

    @Test
    public void wasAlreadyVoted_투표하지_않았다면_false() {
        repository.save(new ReplyVote("r1", "m1", UP));
        repository.save(new ReplyVote("r1", "m2", DOWN));
        boolean alreadyVoted = repository.wasAlreadyVoted("r1", "m9");

        assertFalse("레코드가 존재하므로 false여야 한다.", alreadyVoted);
    }

    @Test
    public void find() {
        ReplyVote vote1 = new ReplyVote("r1", "m1", UP);
        ReplyVote vote2 = new ReplyVote("r1", "m2", DOWN);
        ReplyVote vote3 = new ReplyVote("r1", "m3", DOWN);
        ReplyVote vote4 = new ReplyVote("r1", "m4", UP);
        repository.save(vote1);
        repository.save(vote2);
        repository.save(vote3);
        repository.save(vote4);
        ReplyVote found = repository.find("r1", "m3").get();

        assertEquals("발견된 투표는 3번째여야 한다.", found, vote3);
    }
}