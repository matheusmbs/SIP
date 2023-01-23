package br.com.unipix.api.NumeroService.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.unipix.api.NumeroService.model.Campanha;



@Repository
public interface CampanhaRepository extends MongoRepository<Campanha, String>  {
    
    @Query(value = "{ 'dataStatus': { $gte: ISODate('2022-12-01T00:00:00.000Z'), $lte: ISODate('2023-01-10T23:59:59.000Z') }}")
    public List<Campanha> findAllLimit(Pageable pageable);
}
