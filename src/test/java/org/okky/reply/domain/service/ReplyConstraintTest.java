package org.okky.reply.domain.service;

import lombok.experimental.FieldDefaults;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.okky.reply.TestMother;
import org.okky.reply.domain.repository.ReplyRepository;
import org.okky.reply.resource.ContextHolder;
import org.okky.share.execption.ExternalServiceError;
import org.okky.share.execption.ModelConflicted;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = PRIVATE)
public class ReplyConstraintTest extends TestMother {
    @InjectMocks
    ReplyConstraint constraint;
    @Mock
    ReplyRepository repository;
    @Mock
    RestTemplate template;
    @Mock
    ContextHolder holder;
    @Mock
    ResponseEntity<Boolean> entity;

    @Test
    public void rejectTogglePinIfWriterNotMatched_요청하는_사용자와_해당_게시글_작성자가_일치할_때() {
        when(holder.getId()).thenReturn("w-1");
        when(entity.getBody()).thenReturn(true);
        when(template.getForEntity("/articles/a-1/writers/w-1/match", Boolean.class)).thenReturn(entity);

        constraint.rejectIfWriterNotMatched("a-1");
    }

    @Test
    public void rejectTogglePinIfWriterNotMatched_요청하는_사용자와_해당_게시글_작성자가_일치하지_않을_때_예외() {
        expect(ModelConflicted.class, "답글 고정/해제는 오로지 게시글 작성자만 가능합니다: 'a-1'");

        when(holder.getId()).thenReturn("w-1");
        when(entity.getBody()).thenReturn(false);
        when(template.getForEntity("/articles/a-1/writers/w-1/match", Boolean.class)).thenReturn(entity);

        constraint.rejectIfWriterNotMatched("a-1");
    }

    @Ignore
    @Test
    public void rejectTogglePinIfWriterNotMatched_요청하는_중_오류가_발생하면_예외() {
        expect(ExternalServiceError.class);

        when(holder.getId()).thenReturn("w-1");
        when(template.getForEntity("/articles/a-1/writers/w-1/match", Boolean.class)).thenThrow(mock(HttpStatusCodeException.class));

        constraint.rejectIfWriterNotMatched("a-1");
    }
}