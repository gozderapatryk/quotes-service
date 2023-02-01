package pl.gozderapatryk.quotesservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "quotes")
public class Quote extends BaseEntity {
    @NotNull
    @Size(min = 3, max = 10000)
    @Column(unique = true)
    private String quote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    public Quote(Long id, String quote, Author author) {
        super(id);
        this.quote = quote;
        this.author = author;
    }
}
