package com.example.realworld.domain.article.repository

import com.example.realworld.domain.article.model.Article
import com.example.realworld.domain.profile.model.Profile
import com.example.realworld.domain.article.model.inout.ArticleSearchCond
import com.example.realworld.domain.tag.model.Tag
import com.example.realworld.domain.tag.repository.TagRepository
import com.example.realworld.domain.user.model.User
import com.example.realworld.domain.user.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager

@Transactional
@SpringBootTest
class CustomArticleRepositoryImplTests {

    @Autowired
    lateinit var articleRepository: ArticleRepository


    @Autowired
    lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var em: EntityManager

    @Test
    fun findBySearchCond() {
        val user1 = saveUser("user1")
        val user2 = saveUser("user2")


        val tag1 = saveTag("tag1")
        val tag2 = saveTag("tag2")
        val tag3 = saveTag("tag3")
        val article1 = saveArticle("title1", user1, tag1, tag2)
        val article2 = saveArticle("title2", user1, tag3)
        val article3 = saveArticle("title3", user2, tag2)
        val article4 = saveArticle("title4", user2)

        user1.profile.favoriteOrUnfavorite(article3)
        user2.profile.favoriteOrUnfavorite(article1)
        user2.profile.favoriteOrUnfavorite(article3)

        em.flush()
        em.clear()

        val find1 = articleRepository.findBySearchCond()
        assertThat(find1).hasSize(4)

        val findFavorite = articleRepository.findBySearchCond(ArticleSearchCond(favorited = "user1"))
        assertThat(findFavorite).hasSize(1)
        assertThat(findFavorite[0].title).isEqualTo("title3")


        val findFavorite2 = articleRepository.findBySearchCond(ArticleSearchCond(favorited = "user2"))
        assertThat(findFavorite2).hasSize(2)
        assertThat(findFavorite2[0].title).isEqualTo("title3")
        assertThat(findFavorite2).hasSize(2)
        assertThat(findFavorite2[1].title).isEqualTo("title1")


        val find2 = articleRepository.findBySearchCond(ArticleSearchCond(favorited = "nouser"))
        assertThat(find2).hasSize(0)

        val find3 = articleRepository.findBySearchCond(ArticleSearchCond(tag = "tag1"))
        assertThat(find3).hasSize(1)
        assertThat(find3[0].title).isEqualTo("title1")
        assertThat(find3[0].authorUsername).isEqualTo("user1")

        val find4 = articleRepository.findBySearchCond(ArticleSearchCond(tag = "tag2", author = "user1"))
        assertThat(find4).hasSize(1)
    }

    @Test
    fun findFeedBySearchCond() {
        val user1 = saveUser("user1")
        val user2 = saveUser("user2")


        val tag1 = saveTag("tag1")
        val tag2 = saveTag("tag2")
        val tag3 = saveTag("tag3")
        val article1 = saveArticle("title1", user1, tag1, tag2)
        val article2 = saveArticle("title2", user1, tag3)
        val article3 = saveArticle("title3", user2, tag2)
        val article4 = saveArticle("title4", user2)

        user1.profile.favoriteOrUnfavorite(article3)
        user2.profile.favoriteOrUnfavorite(article1)
        user2.profile.favoriteOrUnfavorite(article3)

        user1.profile.followOrUnfollow(user2.profile)

        em.flush()
        em.clear()
        // user 2 의 article 만 조회 되어야 함
        val currentUserProfile = user1.profile
        val followingIds = currentUserProfile.following.map { it.id }

        val find1 = articleRepository.findFeedBySearchCond(followingIds)
        assertThat(find1).hasSize(2)

        val findFavorite = articleRepository.findFeedBySearchCond(followingIds, ArticleSearchCond(favorited = "user1"))
        assertThat(findFavorite).hasSize(1)
        assertThat(findFavorite[0].title).isEqualTo("title3")


        val findFavorite2 = articleRepository.findFeedBySearchCond(followingIds, ArticleSearchCond(favorited = "user2"))
        assertThat(findFavorite2).hasSize(1)
        assertThat(findFavorite2[0].title).isEqualTo("title3")


        val find2 = articleRepository.findFeedBySearchCond(followingIds, ArticleSearchCond(favorited = "nouser"))
        assertThat(find2).hasSize(0)

        val find3 = articleRepository.findFeedBySearchCond(followingIds, ArticleSearchCond(tag = "tag1"))
        assertThat(find3).hasSize(0)

        val find4 = articleRepository.findFeedBySearchCond(followingIds, ArticleSearchCond(tag = "tag2", author = "user1"))
        assertThat(find4).hasSize(0)
    }


    private fun saveArticle(title: String, user: User, vararg tag: Tag) = articleRepository.save(
        Article(
            title, "desc", "body",
            mutableListOf(*tag),
            user.profile
        )
    )

    private fun saveTag(name: String) = tagRepository.save(Tag(name))

    private fun saveUser(username: String) = userRepository.save(
        User(
            UUID.randomUUID().toString(), "papa", Profile(
                username
            )
        )
    )


}