package br.com.unipix.api.NumeroService.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErroArquivoEnum {
	FORMATO_INVALIDO("O arquivo não possui o formato adequado"),
	INSTABILIDADE_ENVIO_ARQUIVO("Falha ao enviar arquivo"),
	LEITURA_ARQUIVO("Erro ao ler arquivo"),
	ARQUIVO_SEM_CABECALHO("O arquivo não possui um cabecalho"),
	ARQUIVO_COLUNA("O arquivo possui mais de uma coluna");
	
	private String mensagem;
}
	

