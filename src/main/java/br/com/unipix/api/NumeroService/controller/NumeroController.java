package br.com.unipix.api.NumeroService.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.unipix.api.NumeroService.dto.request.NumeroRequestDTO;
import br.com.unipix.api.NumeroService.dto.response.NumeroResponseDTO;
import br.com.unipix.api.NumeroService.service.NumeroService;

@RestController
@RequestMapping("/numeros")
public class NumeroController {

    @Autowired
    NumeroService numbersService;

    @PostMapping(value = "/validacao-sip/arquivo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<NumeroResponseDTO>> create(MultipartFile arquivo) throws IOException, InterruptedException, ExecutionException {
        return ResponseEntity.ok().body(numbersService.processarArquivoNumeros(arquivo));
    }

    @PostMapping(value = "/validacao-sip")
    public ResponseEntity<List<NumeroResponseDTO>> validacaoSip(@RequestBody NumeroRequestDTO numeroRequestDTO) throws IOException, InterruptedException, ExecutionException {
        return ResponseEntity.ok().body(numbersService.criarNumeros(numeroRequestDTO.getNumeros()));
    }
}
