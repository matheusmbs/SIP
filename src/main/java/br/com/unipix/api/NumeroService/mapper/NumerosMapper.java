package br.com.unipix.api.NumeroService.mapper;

import java.util.ArrayList;
import java.util.List;

import br.com.unipix.api.NumeroService.dto.response.NumeroResponseDTO;
import br.com.unipix.api.NumeroService.model.Numero;

public class NumerosMapper {

    public static List<NumeroResponseDTO> convertNumeroToNumeroResponse(List<Numero> numeros) {
        List<NumeroResponseDTO> numeroResponseDTOs = new ArrayList();
        for (Numero n : numeros) {
            NumeroResponseDTO numeroResponseDTO = new NumeroResponseDTO();
            numeroResponseDTO.setNumero(n.getNumero());
            numeroResponseDTO.setStatusCode(n.getStatusCode());
            numeroResponseDTO.setStatusMessage(n.getStatusMessage());
            numeroResponseDTO.setCalId(n.getCallId());
            numeroResponseDTOs.add(numeroResponseDTO);
        }
        return numeroResponseDTOs;
    }
}
