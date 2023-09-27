package com.kCalControl.dto.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDataDTO {

    private String firstName;
    private String lastName;
    private String mobile;
    private String email;

}
