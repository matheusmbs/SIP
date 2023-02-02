package br.com.unipix.api.NumeroService.dto.response;

import lombok.Data;

@Data
public class NumeroResponseDTO {
    private String numero;
    private Integer statusCode;
    private String statusMessage;
    private String calId;
}
