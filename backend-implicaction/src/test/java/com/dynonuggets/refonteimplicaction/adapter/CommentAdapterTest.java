package com.dynonuggets.refonteimplicaction.adapter;

import com.dynonuggets.refonteimplicaction.dto.CommentDto;
import com.dynonuggets.refonteimplicaction.model.Comment;
import com.dynonuggets.refonteimplicaction.model.Post;
import com.dynonuggets.refonteimplicaction.model.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CommentAdapterTest {

    CommentAdapter commentAdapter = new CommentAdapter();

    @Test
    void toModel() {
        // given
        User user = User.builder().id(123L).username("Marc Elbichon").build();
        Post post = Post.builder().id(243L).build();
        CommentDto dto = CommentDto.builder()
                .id(123L)
                .postId(243L)
                .createdAt(Instant.now())
                .text("voici mon commentaire sur ce point !")
                .username("Marc Elbichon")
                .createdAt(Instant.now())
                .build();
        Comment expectedComment = Comment.builder()
                .id(dto.getId())
                .text(dto.getText())
                .createdAt(dto.getCreatedAt())
                .post(post)
                .user(user)
                .build();

        // when
        Comment actualComment = commentAdapter.toModel(dto, post, user);

        // then
        assertThat(actualComment.getId()).isEqualTo(expectedComment.getId());
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
        assertThat(actualComment.getCreatedAt()).isEqualTo(expectedComment.getCreatedAt());
        assertThat(actualComment.getPost()).isEqualTo(expectedComment.getPost());
        assertThat(actualComment.getUser()).isEqualTo(expectedComment.getUser());
    }

    @Test
    void toDto() {
        // when
        User user = User.builder().id(123L).username("Marc Elbichon").build();
        Post post = Post.builder().id(243L).build();
        Comment comment = Comment.builder()
                .id(123L)
                .post(post)
                .createdAt(Instant.now())
                .text("voici mon commentaire sur ce point !")
                .user(user)
                .build();

        // when
        CommentDto actualDto = commentAdapter.toDto(comment);

        // then
        assertThat(actualDto.getId()).isEqualTo(comment.getId());
        assertThat(actualDto.getPostId()).isEqualTo(comment.getPost().getId());
        assertThat(actualDto.getCreatedAt()).isEqualTo(comment.getCreatedAt());
        assertThat(actualDto.getText()).isEqualTo(comment.getText());
        assertThat(actualDto.getUsername()).isEqualTo(comment.getUser().getUsername());
    }
}