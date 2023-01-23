package br.com.unipix.api.NumeroService.enumaration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExtensaoArquivoEnum {
	CSV(".csv"),
	XLS(".xls"),
	XLSX(".xlsx"),
	PDF(".pdf"),
	TXT(".txt");
	
	private String extensao;
}
