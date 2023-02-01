package pl.gozderapatryk.quotesservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.gozderapatryk.quotesservice.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    @Query("select a from Author a where (:author is null or a.firstName LIKE %:author% or a.middleName LIKE %:author% or a.lastName LIKE %:author%)")
    Page<Author> findAll(Pageable pageable, @Param(value = "author") String author);
}
