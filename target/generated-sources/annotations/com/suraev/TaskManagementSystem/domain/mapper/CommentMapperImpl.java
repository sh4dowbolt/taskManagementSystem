package com.suraev.TaskManagementSystem.domain.mapper;

import com.suraev.TaskManagementSystem.domain.entity.Comment;
import com.suraev.TaskManagementSystem.domain.entity.Comment.CommentBuilder;
import com.suraev.TaskManagementSystem.dto.CommentCreateDTO;
import com.suraev.TaskManagementSystem.dto.CommentDTO;
import com.suraev.TaskManagementSystem.dto.CommentDTO.CommentDTOBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-21T22:06:26+0400",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentDTO map(Comment model) {
        if ( model == null ) {
            return null;
        }

        CommentDTOBuilder commentDTO = CommentDTO.builder();

        commentDTO.id( model.getId() );
        commentDTO.description( model.getDescription() );
        commentDTO.authorId( model.getAuthorId() );

        return commentDTO.build();
    }

    @Override
    public Comment map(CommentCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CommentBuilder comment = Comment.builder();

        comment.description( dto.getDescription() );

        return comment.build();
    }
}
