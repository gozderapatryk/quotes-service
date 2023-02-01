package pl.gozderapatryk.quotesservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetAuthorDto {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
}
