package br.com.unipix.api.NumeroService.model;

import lombok.Data;

@Data
public class CodigoSip {
   
   public Integer codigo;
   public String mensagem; 

   public CodigoSip(Integer codigo, String mensagem){
      this.codigo = codigo;
      this.mensagem = mensagem;
   }
}
