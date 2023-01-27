package br.com.unipix.api.NumeroService.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
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

        // FileWriter fileWriter = new FileWriter("numeroSemRepeticao.txt", false);
        // BufferedWriter writer = new BufferedWriter(fileWriter);
      
        // List<String> nSalvos = new ArrayList<>();
        // for (String n : numeros) {
        //     String[] nAr = n.split(",");
        //     String numero = nAr[0];
        //     Integer i = nSalvos.indexOf(numero);
        //     if(i == -1){
        //         nSalvos.add(numero);
        //         writer.write(n + "\n");
        //     }
        // }
        // writer.close();
        return this.criarNumeros(numeros);
    }

    public List<NumeroResponseDTO> criarNumeros(List<String> numbers)
            throws IOException, InterruptedException, ExecutionException {
        List<Numero> numerosValidos = new ArrayList<>();
        List<Numero> numeroProcessados = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();

        // Criando objetos Numero e adicionando na lista
        for (String n : numbers) {
            String[] nAr = n.split(",");
            Numero numero = new Numero();
            numero.setNumero(nAr[0]);
            numero.setStatusSMS(nAr[1]);
            numero.setStatusCode(0);
            numero.setDataCriacao(today);
            numerosValidos.add(numero);
        }

        // Realizando chamadas somente se houver números válidos
        if (!numerosValidos.isEmpty()) {
            numeroProcessados = this.sipService.processCall(numerosValidos);
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
