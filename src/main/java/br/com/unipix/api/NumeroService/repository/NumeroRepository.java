package br.com.unipix.api.NumeroService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.unipix.api.NumeroService.model.Numero;

@Repository
public interface NumeroRepository extends MongoRepository<Numero, String> {

    @Query(value = "{ 'numero': { $all: ?0 }}")
    public List<Numero> findByNumeros(List<String> numeros);

    @Query(value = "{ 'numero': numero}")
    public Numero findByNumero(String numero);

    @Query(value = "{ 'dataCriacao': { $gt: ?0 }}")
    public List<Numero> findNumerosMaxDate(LocalDateTime date);

    @Query(value = "{ 'statusCode': ?0}")
    public List<Numero> findNumerosByStatusCode(Integer status);

}
