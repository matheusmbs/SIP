package br.com.unipix.api.NumeroService.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("numero")
public class Numero {
    @Id
	private String id;
    private String numero;
    private String callId;
    private String statusSMS;
    private Integer statusCode;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataProcessamento;
}
