package br.com.unipix.api.NumeroService.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<NumeroResponseDTO> processarArquivoNumeros(MultipartFile arquivo)
            throws IOException, InterruptedException, ExecutionException {
        List<String> numeros = readNumberFile.readAndValidateFile(arquivo);
        return criarNumeros(numeros);
    }

    public List<NumeroResponseDTO> criarNumeros(List<String> numbers)
            throws IOException, InterruptedException, ExecutionException {
        List<Numero> numerosValidos = new ArrayList<>();
        List<Numero> numeroProcessados = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();

        // Criando objetos Numero e adicionando na lista
        for (String n : numbers) {
            Numero numero = new Numero();
            numero.setNumero(n);
            numero.setStatusCode(0);
            numero.setDataCriacao(today);
            numerosValidos.add(numero);
        }

        // Realizando chamadas somente se houver números válidos
        if (!numerosValidos.isEmpty()) {
            // int subListSize = 10;
            // ListIterator<Numero> iterator = numerosValidos.listIterator();
            // List<List<Numero>> subLists = new ArrayList<>();
            // List<Numero> subList = new ArrayList<>(subListSize);
            // while (iterator.hasNext()) {
            // subList.add(iterator.next());
            // if (subList.size() == subListSize) {
            // subLists.add(subList);
            // subList = new ArrayList<>(subListSize);
            // }
            // }
            // if (!subList.isEmpty()) {
            // subLists.add(subList);
            // }

            // Integer i = 1;

            // for (List<Numero> ns : subLists) {
            // System.out.println("Processando " + (i * 10) + " de " +
            // numerosValidos.size());
            // this.sipService.call(ns);
            // i++;
            // }

            // numerosValidos = this.sipService.call(numerosValidos);

            this.sipService.call(numerosValidos);
        }

        // Transformando objetos Numero em NumeroResponseDTO
        List<NumeroResponseDTO> numeroResponseDTOs = NumerosMapper.convertNumeroToNumeroResponse(numeroProcessados);
        return numeroResponseDTOs;
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

}
