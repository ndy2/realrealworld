package com.example.realworld.domain.article.repository

import com.example.realworld.domain.article.model.Article
import com.example.realworld.domain.article.model.QArticle
import com.example.realworld.domain.profile.model.Profile
import com.example.realworld.domain.profile.model.QProfile
import com.example.realworld.domain.profile.model.inout.ArticleSearchCond
import com.example.realworld.domain.profile.repository.ProfileRepository
import com.example.realworld.domain.tag.model.QTag
import com.example.realworld.domain.tag.model.Tag
import com.example.realworld.domain.tag.repository.TagRepository
import com.example.realworld.domain.user.model.QUser
import com.example.realworld.domain.user.model.User
import com.example.realworld.domain.user.repository.UserRepository
import com.querydsl.core.QueryFactory
import com.querydsl.jpa.impl.JPAQueryFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import javax.persistence.EntityManager

@SpringBootTest
class CustomArticleRepositoryImplTests {

    @Autowired
    lateinit var articleRepository: ArticleRepository


    @Autowired
    lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var profileRepository: ProfileRepository

    @Autowired
    lateinit var em: EntityManager

    @Test
    fun name() {
        val user = userRepository.save(User("haha", "papa", Profile("haha")))
        val tag = tagRepository.save(Tag("tag1"))

        val article = articleRepository.save(
            Article(
                "title", "desc", "body",
                mutableListOf(tag),
                user.profile
            )
        )

        //when
        /*val articles = articleRepository.findBySearchCond(
            ArticleSearchCond(null, null, null),
            Pageable.ofSize(20)
        )

        for (article in articles) {
            println(article.title)
        }*/

        val query = JPAQueryFactory(em)
        val users = query.selectFrom(QUser.user).fetch()
        assertThat(users[0].email).isEqualTo("haha")

        val profiles = query.selectFrom(QProfile.profile).fetch()
        assertThat(profiles[0].username).isEqualTo("haha")

        val articles = query.selectFrom(QArticle.article).fetch()
        assertThat(articles[0].title).isEqualTo("title")


        val tagFilteredArticles = query.select(QArticle.article.id)
            .from(QArticle.article)
            .join(QArticle.article.tags, QTag.tag)
            .where(QTag.tag.name.eq("tag1"))
            .fetch()

        assertThat(tagFilteredArticles[0]).isEqualTo(article.id)

        val articles2 =
            articleRepository.findBySearchCond(ArticleSearchCond("tag1", null, null), Pageable.ofSize(20))
        println("hello")
    }



}