package kpi.donate.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddImageRequestBody {

    @NotNull
    @Size(max = 10000)
    private String image;

    @NotNull
    private String token;
}
