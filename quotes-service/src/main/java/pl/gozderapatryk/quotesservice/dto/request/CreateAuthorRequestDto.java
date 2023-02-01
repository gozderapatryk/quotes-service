package pl.gozderapatryk.quotesservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateAuthorRequestDto {
    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]{2,}$", message = "must match \"^[A-Z][a-z]{2,}$\"")
    private String firstName;
    @Pattern(regexp = "^[A-Z][a-z]{2,}$", message = "must match \"^[A-Z][a-z]{2,}$\"")
    private String middleName;
    @NotNull
    @Pattern(regexp = "^([A-Z][a-z]{2,})(?:-[A-Z][a-z]{2,})?$", message = "must match \"^([A-Z][a-z]{2,})(?:-[A-Z][a-z]{2,})?$\"")
    private String lastName;
}
