package br.com.unipix.api.NumeroService.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import br.com.unipix.api.NumeroService.dto.response.NumeroResponseDTO;
import br.com.unipix.api.NumeroService.file.read.ReadNumberFile;
import br.com.unipix.api.NumeroService.mapper.NumerosMapper;
import br.com.unipix.api.NumeroService.model.Numero;
import br.com.unipix.api.NumeroService.repository.NumeroRepository;

@Service
public class NumeroService {

    @Autowired
    ReadNumberFile readNumberFile;

    @Autowired
    NumeroRepository numeroRepository;

    @Autowired
    SipService sipService;

    public List<Numero> processarArquivoNumeros(MultipartFile arquivo)
            throws IOException, InterruptedException, ExecutionException {
        List<String> numeros = readNumberFile.readAndValidateFile(arquivo);
        return this.criarNumeros(numeros);
    }

    public List<Numero> criarNumeros(List<String> numbers)
            throws IOException, InterruptedException, ExecutionException {

        numbers = numbers.stream().distinct().collect(Collectors.toList());
        List<Numero> numerosValidos = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();

        // Criando objetos Numero e adicionando na lista
        for (String n : numbers) {
            Numero numero = new Numero();
            numero.setNumero(n);
            numero.setStatusCode(0);
            numero.setStatusProcessamento("Aguardando Processamento");
            numero.setQuantidadeReprocessamento(0);
            numero.setDataCriacao(today);
            numerosValidos.add(numero);
        }

        numerosValidos = this.saveAll(numerosValidos);
        return numerosValidos;
    }

    public List<Numero> findNumerosByMaxDate(LocalDateTime date) {
        return this.numeroRepository.findNumerosMaxDate(date);
    }

    public void deleteNumeros(List<Numero> numeros) {
        numeroRepository.deleteAll(numeros);
    }

    public List<Numero> saveAll(List<Numero> numeros) {
        return this.numeroRepository.saveAll(numeros);
    }

    public Long countNumerosByStatusProcessamento(String statusProcessamento) {
        return this.numeroRepository.countNumerosByStatusProcessamento(statusProcessamento);
    }

    public List<Numero> findNumerosByStatusProcessamento(String statusProcessamento, PageRequest pageable) {
        return this.numeroRepository.findNumerosByStatusProcessamento(statusProcessamento, pageable);
    }

    public List<Numero> findNumerosByStatusProcessamentoAndDate(String statusProcessamento, LocalDateTime date) {
        return this.numeroRepository.findNumerosByStatusProcessamentoAndDate(statusProcessamento, date);
    }

}
