package org.okky.reply.domain.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.okky.reply.TestMother;
import org.okky.reply.domain.event.DomainEventPublisher;
import org.okky.share.event.ReplyPinned;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DomainEventPublisher.class)
public class ReplyTest extends TestMother {
    @Before
    public void setUp() {
        PowerMockito.mockStatic(DomainEventPublisher.class);
    }

    @Test
    public void new_최초에는_pin_상태가_아님() {
        Reply reply = fixture();

        assertFalse("최초 생성시에는 답글이 고정된 상태가 아니다.", reply.pinned());
    }

    @Test
    public void pin_고정_후에는_상태가_true여야_하며_날짜도_null이_아니어야_함() {
        PowerMockito.doNothing().when(DomainEventPublisher.class);
        DomainEventPublisher.fire(any(ReplyPinned.class));

        Reply reply = fixture();
        reply.pin("m");

        assertTrue("고정하였으므로 상태는 true여야 한다.", reply.pinned());

        PowerMockito.verifyStatic(DomainEventPublisher.class);
        DomainEventPublisher.fire(any(ReplyPinned.class));
    }

    @Test
    public void pin_이미_고정_상태에서_여러번_고정하면_상태가_true여야_하며_날짜도_null이_아니어야_함() {
        PowerMockito.doNothing().when(DomainEventPublisher.class);
        DomainEventPublisher.fire(any(ReplyPinned.class));

        Reply reply = fixture();
        reply.pin("m");
        reply.pin("m");
        reply.pin("m");
        reply.pin("m");

        assertTrue("고정하였으므로 상태는 true여야 한다.", reply.pinned());

        PowerMockito.verifyStatic(DomainEventPublisher.class, times(4));
        DomainEventPublisher.fire(any(ReplyPinned.class));
    }

    @Test
    public void unpin_고정_해제_후에는_상태가_false여야_하며_날짜도_null이어야_함() {
        Reply reply = fixture();
        reply.unpin();

        assertFalse("고정한 상태가 아니므로 false여야 한다.", reply.pinned());
    }

    @Test
    public void unpin_고정_상태가_아닌데_여러_번_고정_해제하면_상태가_false여야_하며_날짜도_null이어야_함() {
        Reply reply = fixture();
        reply.unpin();
        reply.unpin();
        reply.unpin();
        reply.unpin();

        assertFalse("고정한 상태가 아니므로 false여야 한다.", reply.pinned());
    }

    @Test
    public void choice_토글_확인() {
        Reply reply = fixture();
        assertFalse(reply.accepted());

        reply.toggleAccept();
        assertTrue(reply.accepted());

        reply.toggleAccept();
        assertFalse(reply.accepted());
    }

    @Test
    public void 수정_확인() {
        Reply reply = fixture();
        reply.modify("답변 드립니다2222");

        assertThat(reply.getBody(), is("답변 드립니다2222"));
    }

    @Test
    public void 채택_전에는_날짜가_null이고_후에는_날짜_존재_확인() {
        Reply reply = fixture();
        assertNull(reply.getAcceptedOn());

        reply.toggleAccept();
        assertNotNull(reply.getAcceptedOn());
    }

    // ----------------------------
    private Reply fixture() {
        String id = "a1";
        String articleId = "a03";
        String body = "답변 드립니다..";
        String replierId = "m-334455";
        String replierName = "coding8282";
        return new Reply(articleId, body, replierId, replierName);
    }
}