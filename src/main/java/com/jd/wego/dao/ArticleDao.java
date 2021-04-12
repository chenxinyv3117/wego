package com.jd.wego.dao;

import com.jd.wego.entity.Article;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.*;

/**
 * @author hbquan
 * @date 2021/4/6 14:43
 */
@Mapper
public interface ArticleDao {

    String FILED_VALUE = "article_title, article_summary, article_content, article_view_count, " +
            "            article_like_count, article_comment_count, created_time, update_time, is_deleted";
    String SELECT_VALUE = "article_id " + FILED_VALUE;

    /**
     * 插入数据
     * @param article
     */
    @Insert("insert into article(article_title, article_summary, article_content, article_view_count, " +
            "article_like_count, article_comment_count, created_time, update_time, is_deleted) values(#{articleTitle}," +
            "#{articleSummary}, #{articleContent}, #{articleViewCount}, #{articleLikeCount}, #{articleCommentCount}," +
            "#{createdTime}, #{updateTime}, #{isDeleted})")
    void insertArticle(Article article);

    /**
     * 可以根据需要，只更新部分字段 注意这里的写法，test里面的单引号双引号使用不当会报错
     * @param article
     */
    @Update("<script>" +
            "update article " +
            "<set>" +
            "<if test ='article_title != null'>article_title = #{articleTitle},</if>" +
            "<if test ='article_summary != null'>article_summary = #{articleSummary},</if>" +
            "<if test ='article_content != null'>article_content = #{articleContent},</if>" +
            "<if test ='article_view_count != null'>article_view_count = #{articleViewCount},</if>" +
            "<if test ='article_like_count != null'>article_like_count = #{articleLikeCount},</if>" +
            "<if test ='article_comment_count != null'>article_comment_count = #{articleCommentCount},</if>" +
            "<if test ='created_time != null'>created_time = #{createdTime},</if>" +
            "<if test ='update_time != null'>update_time = #{updateTime},</if>" +
            "<if test ='is_deleted != null'>is_deleted = #{isDeleted}</if>" +
            "</set>" +
            "where article_id = #{articleId}" +
            "</script>")
    void updateArticle(Article article);

    /**
     * 这里的删除只做逻辑删除 0：未删除 1：删除
     * @param articleId
     */
    @Update("update article set is_deleted = 1 where article_id=#{articleId}")
    void deleteArticle(int articleId);


    /**
     * 根据不同的分类id来查找对应的文章，默认按照发表文章的更新时间来进行排序
     * 并保证初始的时候，创建时间和更新时间必须相同
     */
    @Select("select " + SELECT_VALUE + " from article where article_category_id = #{categoryId} and is_deleted = 0 order by update_time")
    List<Article> selectArticleByCategoryId(int categoryId);


    /**
     * 根据点赞数量进行排序，如果点赞数相同的话，然后在按照更新时间进行排序
     * @return
     */
    @Select("select " + SELECT_VALUE + " from article order by article_like_count, update_time where is_deleted = 0")
    List<Article> selectArticleByLikeCount();

    /**
     * 根据评论数量进行排序，如果点赞数相同的话，然后在按照更新时间进行排序
     * @return
     */
    @Select("select " + SELECT_VALUE + " from article order by article_like_count, update_time where is_deleted = 0")
    List<Article> selectArticleByCommentCount();


    /**
     * 根据浏览数量进行排序，如果点赞数相同的话，然后在按照更新时间进行排序
     * @return
     */
    @Select("select " + SELECT_VALUE + " from article order by article_view_count, update_time where is_deleted = 0")
    List<Article> selectArticleByViewCount();

    /**
     * 根据学校的不同，来展示不同的文章效果
     * @param userId
     * @return
     */
    @Select("select " + SELECT_VALUE + " from article where order by update_time" )
    List<Article> selectArticleBySchool(int userId);
}