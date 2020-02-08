package com.interest.dao;

import java.util.List;

import com.interest.model.entity.PostCardEntity;
import com.interest.model.response.PostCardResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostCardDao {

    List<PostCardResponse> postcardList(@Param("interestid") String interestid, @Param("pageSize") int pageSize,
                                        @Param("start") int start);

    Integer postcardSize(@Param("interestid") String interestid, @Param("pageSize") int pageSize,
                         @Param("start") int start);

    void insertEntity(PostCardEntity postCardEntity);

    PostCardResponse getPostcard(@Param("id") int id);

    void updateCreatetiem(@Param("id") int id, @Param("replytime") String replytime);

    void deletePostcards(@Param("groupId") List<String> groupId);

    List<PostCardEntity> getALL();

}
