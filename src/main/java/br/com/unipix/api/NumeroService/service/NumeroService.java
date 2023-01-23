package br.com.unipix.api.NumeroService.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
            throws IOException, InterruptedException {
        List<String> numeros = readNumberFile.readAndValidateFile(arquivo);
        return criarNumeros(numeros, false);
    }

    public List<NumeroResponseDTO> criarNumeros(List<String> numbers, Boolean realizarValidacaoSIP)
            throws IOException, InterruptedException {
        List<Numero> numerosExistentes = this.findByNumeros(numbers);

        numbers.removeAll(numerosExistentes.stream().map(e -> e.getNumero())
                .collect(Collectors.toList()));

        Map<String, List<String>> numerosValidacao = this.validarNumeros(numbers);
        List<String> numerosValidados = numerosValidacao.get("validos");
        List<String> numerosInvalidados = numerosValidacao.get("invalidos");

        List<Numero> numerosValidos = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();
        for (String n : numerosValidados) {
            Numero numero = new Numero();
            numero.setNumero(n);
            numero.setStatusCode(0);
            numero.setDataCriacao(today);
            numerosValidos.add(numero);
        }

        if (realizarValidacaoSIP && numerosValidos.size() > 0) {
            numerosValidos = this.sipService.call(numerosValidos);
        }

        // numerosValidos = this.saveAll(numerosValidos);

        List<Numero> numerosInvalidos = new ArrayList();
        for (String n : numerosInvalidados) {
            Numero numero = new Numero();
            numero.setNumero(n);
            numero.setStatusCode(404);
            numero.setDataCriacao(today);
            numero.setDataProcessamento(today);
            numerosInvalidos.add(numero);
        }

        // numerosInvalidos = this.saveAll(numerosInvalidos);

        List<NumeroResponseDTO> numeroResponseDTOs = new ArrayList<>();
        numeroResponseDTOs.addAll(NumerosMapper.convertNumeroToNumeroResponse(numerosValidos));
        numeroResponseDTOs.addAll(NumerosMapper.convertNumeroToNumeroResponse(numerosExistentes));
        numeroResponseDTOs.addAll(NumerosMapper.convertNumeroToNumeroResponse(numerosInvalidos));

        return numeroResponseDTOs;
    }

    public Map<String, List<String>> validarNumeros(List<String> numeros) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<String> numerosValidos = new ArrayList<String>();
        List<String> numerosInvalidos = new ArrayList<String>();
        for (String n : numeros) {
            if (StringUtils.isNumeric(n)) {
                if (n.length() == 13) {
                    String ddi = n.substring(0, 2);
                    String prefixCell = n.substring(4, 5);
                    if (ddi.equals("55") && prefixCell.equals("9")) {
                        numerosValidos.add(n);
                        continue;
                    }
                }

                if (n.length() == 11) {
                    String prefixCell = n.substring(2, 3);
                    if (prefixCell.equals("9")) {
                        numerosValidos.add("55" + n);
                        continue;
                    }
                }

                numerosInvalidos.add(n);
            }
        }
        map.put("validos", numerosValidos);
        map.put("invalidos", numerosInvalidos);
        return map;
    }

    public List<Numero> findByNumeros(List<String> numeros) {
        return this.numeroRepository.findByNumeros(numeros);
    }

    public Numero findByNumero(String numero) {
        return this.numeroRepository.findByNumero(numero);
    }

    public List<Numero> findNumerosByMaxDate(LocalDateTime date) {
        return this.numeroRepository.findNumerosMaxDate(date);
    }

    public List<Numero> findNumerosByStatusCode(Integer status) {
        return this.numeroRepository.findNumerosByStatusCode(status);
    }

    public void deleteNumeros(List<Numero> numeros) {
        numeroRepository.deleteAll(numeros);
    }

    public List<Numero> saveAll(List<Numero> numeros) {
        return this.numeroRepository.saveAll(numeros);
    }

}
