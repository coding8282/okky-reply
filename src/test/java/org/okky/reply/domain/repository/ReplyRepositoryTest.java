package org.okky.reply.domain.repository;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.okky.reply.TestMother;
import org.okky.reply.domain.model.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static lombok.AccessLevel.PRIVATE;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@FieldDefaults(level = PRIVATE)
public class ReplyRepositoryTest extends TestMother {
    @Autowired
    ReplyRepository repository;

    @Test
    public void findPinned() {
        Reply reply1 = fixture();
        Reply reply2 = fixture();
        reply2.pin("m");
        repository.save(reply1);
        repository.save(reply2);
        Reply found = repository.findPinned("a-1").get();

        assertEquals("찾은 것은 고정된 답글이 아니다.", reply2, found);
    }

    // -----------------------------------
    private Reply fixture() {
        String articleId = "a-1";
        String body = "답변 드립니다..";
        String replierId = "m-334455";
        String replierName = "coding8282";
        return new Reply(articleId, body, replierId, replierName);
    }
}