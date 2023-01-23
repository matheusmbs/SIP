package br.com.unipix.api.NumeroService.file.read;

import static java.util.Objects.nonNull;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import br.com.unipix.api.NumeroService.enumaration.ExtensaoArquivoEnum;
import br.com.unipix.api.NumeroService.exception.BusinessException;



public class ReadExtension {
	
	public static Optional<String> getFileExtension(String file){
		if(nonNull(file) && !StringUtils.isBlank(file) && !file.isEmpty()) {
			return Optional.of(file.substring(file.lastIndexOf(".")+1).toLowerCase());
		}
		return Optional.empty();
	}
	
	public static ExtensaoArquivoEnum getFileExtensionEnum(String file){
		if(nonNull(file) && !StringUtils.isBlank(file) && !file.isEmpty()) {
			Optional<String> extension =  Optional.of(file.substring(file.lastIndexOf(".")+1).toLowerCase());
			switch(extension.get()) {
				case "xls":
					return ExtensaoArquivoEnum.XLS;
				case "xlsx":
					return ExtensaoArquivoEnum.XLSX;
				case "csv":
					return ExtensaoArquivoEnum.CSV;
				case "pdf":
					return ExtensaoArquivoEnum.PDF;
				case "txt":
					return ExtensaoArquivoEnum.TXT;
				default:
					break;
			}
		}
		throw new BusinessException("Nenhuma extensão de arquivo compatível encontrada!");
	}
}
