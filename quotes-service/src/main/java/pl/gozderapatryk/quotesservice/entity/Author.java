package pl.gozderapatryk.quotesservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author extends BaseEntity {

    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]{2,}$", message = "must match \"^[A-Z][a-z]{2,}$\"")
    @Column(name = "first_name")
    private String firstName;

    @Pattern(regexp = "^[A-Z][a-z]{2,}$", message = "must match \"^[A-Z][a-z]{2,}$\"")
    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Pattern(regexp = "^([A-Z][a-z]{2,})(?:-[A-Z][a-z]{2,})?$", message = "must match \"^([A-Z][a-z]{2,})(?:-[A-Z][a-z]{2,})?$\"")
    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private Set<Quote> quotes = new HashSet<>();

    public Author(Long id, String firstName, String middleName, String lastName, Set<Quote> quotes) {
        super(id);
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.quotes = quotes;
    }

    public void removeQuote(Quote quote) {
        if (Objects.nonNull(quote)) {
            quote.setAuthor(null);
            this.quotes.remove(quote);
        }
    }

    public void addQuote(Quote quote) {
        if (Objects.nonNull(quote)) {
            quote.setAuthor(this);
            this.quotes.add(quote);
        }
    }

    @Override
    public String toString() {
        return "Author{" +
                "id='" + super.getId() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", quotes=" + quotes +
                '}';
    }
}
