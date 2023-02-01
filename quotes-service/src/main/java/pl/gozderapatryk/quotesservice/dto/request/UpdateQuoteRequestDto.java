package pl.gozderapatryk.quotesservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateQuoteRequestDto {
    @NotNull
    @Size(min = 3, max = 10000)
    private String quote;
    @NotNull
    @Positive
    private Long authorId;
}
