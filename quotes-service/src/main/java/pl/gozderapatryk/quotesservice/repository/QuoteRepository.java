package pl.gozderapatryk.quotesservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.gozderapatryk.quotesservice.entity.Quote;

import java.util.List;

@Repository
public interface QuoteRepository  extends JpaRepository<Quote, Long> {
    boolean existsByQuote(@Param("quote") String quote);

    @Query("select q from Quote q where (:authorId is null or q.author.id = :authorId)")
    Page<Quote> findAll(Pageable pageable, @Param(value = "authorId") Long authorId);
}
