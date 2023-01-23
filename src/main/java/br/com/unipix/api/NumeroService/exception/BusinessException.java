package br.com.unipix.api.NumeroService.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	public BusinessException (String mensage) {
		super(mensage);
		log.error(mensage);
	}
	
	public BusinessException (ErroArquivoEnum mensage) {
		super(mensage.getMensagem());
		log.error(mensage.getMensagem());
	}
	
	public BusinessException() {
		super();
	}
	
}
