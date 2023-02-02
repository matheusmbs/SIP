package br.com.unipix.api.NumeroService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
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

    @Query(value = "{ 'statusProcessamento': ?0}")
    public List<Numero> findNumerosByStatusProcessamento(String status, PageRequest pageable);

    @Query(value = "{ 'statusProcessamento': ?0}", count = true)
    public Long countNumerosByStatusProcessamento(String status);

    @Query(value = "{ 'statusProcessamento': ?0, 'dataProcessamento': { $gt: ?1 }}")
    public List<Numero> findNumerosByStatusProcessamentoAndDate(String statusProcessamento, LocalDateTime date);

}
