package br.com.unipix.api.NumeroService.dto.request;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NumeroRequestDTO {
    @Builder.Default
    private List<String> numeros  = new ArrayList<>();;
}
