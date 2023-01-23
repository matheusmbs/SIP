package br.com.unipix.api.NumeroService.file.read;

import com.monitorjbl.xlsx.StreamingReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import br.com.unipix.api.NumeroService.exception.BusinessException;
import br.com.unipix.api.NumeroService.exception.ErroArquivoEnum;
import br.com.unipix.api.NumeroService.validate.ValidateArquivo;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ReadNumberFile {
    public List<String> readAndValidateFile(MultipartFile file) {

        Optional<String> fileType = ReadExtension.getFileExtension(file.getOriginalFilename());
        List<String> lines = new ArrayList<>();

        if (fileType.isPresent()) {
            if (fileType.get().equals("xlsx")) {
                lines = readAndValidateXlsxFile(file);
            } else if (fileType.get().equals("xls")) {
                lines = readAndValidateXlsFile(file);
            } else if (fileType.get().equals("csv")) {
                lines = readAndValidateCsvFile(file);
            } else if (fileType.get().equals("txt")) {
                lines = readAndValidateTxtFile(file);
            }
        } else {
            throw new BusinessException(ErroArquivoEnum.FORMATO_INVALIDO);
        }
        return lines;
    }

    @SuppressWarnings("deprecation")
    private List<String> readAndValidateXlsxFile(MultipartFile file) {

        List<String> lines = new ArrayList<>();
        Workbook workbook;

        try {
            workbook = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096)
                    .open(file.getInputStream());

        } catch (Exception e) {
            throw new BusinessException(ErroArquivoEnum.LEITURA_ARQUIVO);
        }

        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getLastCellNum() <= 0) {
                continue;
            }

            String cells = new String();
            Cell cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            cells = cell.getStringCellValue();
            lines.add(cells);
        }

        try {
            workbook.close();
        } catch (IOException ignored) {
        }

        return lines;
    }

    @SuppressWarnings("deprecation")
    private List<String> readAndValidateXlsFile(MultipartFile file) {

        List<String> lines = new ArrayList<>();
        Workbook workbook;

        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (Exception e) {
            throw new BusinessException(ErroArquivoEnum.LEITURA_ARQUIVO);
        }

        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {

            if (row.getLastCellNum() <= 0) {
                continue;
            }
            String cells = new String();
            cells = row.getCell(0).getStringCellValue();
            lines.add(cells);
        }
        return lines;
    }

    private List<String> readAndValidateCsvFile(MultipartFile file) {

        Reader reader;
        try {
            reader = new InputStreamReader(file.getInputStream(), "UTF-8");
        } catch (Exception e) {
            throw new BusinessException("Erro ao Ler o arquivo");
        }
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0)
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build();

        try {
            String separator = ValidateArquivo.separadorCsv(0, file);
            if (separator.equals(",")) {
                csvReader = new CSVReaderBuilder(reader).withSkipLines(0)
                        .withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build();
            }
        } catch (IOException e1) {

        }

        List<String> lines = new ArrayList<>();
        String[] line;

        try {
            while ((line = csvReader.readNext()) != null) {
                lines.add(line[0]);
            }
            reader.close();
            csvReader.close();
        } catch (Exception e) {
            throw new BusinessException("Arquivo CSV Invalido");
        }

        return lines;
    }

    private List<String> readAndValidateTxtFile(MultipartFile file) {
        List<String> lines = new ArrayList<>();

        InputStreamReader reader;
        try {
            reader = new InputStreamReader(file.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();

            while (line != null) {
                String[] column = line.split(";");
                lines.add(column[0]);
                line = br.readLine();
            }
            return lines;
        } catch (IOException e) {
            throw new BusinessException(ErroArquivoEnum.LEITURA_ARQUIVO);
        }
    }
}
