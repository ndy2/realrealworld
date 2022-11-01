package com.example.realworld.common.querydsl

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.JPQLQuery
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport
import org.springframework.data.jpa.repository.support.Querydsl
import org.springframework.data.querydsl.SimpleEntityPathResolver
import java.util.function.Consumer
import java.util.function.Supplier
import javax.persistence.EntityManager


abstract class Querydsl5RepositorySupport(
    val domainClass: Class<*>
) {

    protected lateinit var querydsl: Querydsl
    protected lateinit var em: EntityManager
    protected lateinit var queryFactory: JPAQueryFactory

    @Autowired
    fun doLateInit(em: EntityManager) {
        val entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em)
        val resolver = SimpleEntityPathResolver.INSTANCE
        val path = resolver.createPath(entityInformation.javaType)

        this.em = em
        querydsl = Querydsl(em, PathBuilder(path.type, path.metadata))
        queryFactory = JPAQueryFactory(em)
    }


    protected fun <T> select(expr: Expression<T>): JPAQuery<T> {
        return queryFactory.select(expr)
    }

    protected fun <T> selectFrom(expr: EntityPath<T>): JPAQuery<T> {
        return queryFactory.selectFrom(expr)
    }

    protected fun <T> applyPagination(
        pageable: Pageable,
        query: JPQLQuery<T>,
        lazyLoader: Consumer<T>? = null
    ): List<T> {
        querydsl.applyPagination(pageable, query)
        val fetch = query.fetch()

        //apply lazy loading
        lazyLoader?.let { fetch.forEach(lazyLoader) }
        return fetch
    }

    protected fun nullSafeBuilder(cond: Supplier<BooleanExpression>): BooleanBuilder {
        return try {
            BooleanBuilder(cond.get())
        } catch (e: IllegalArgumentException) {
            BooleanBuilder()
        } catch (e: NullPointerException) {
            BooleanBuilder()
        }
    }
}