package br.com.unipix.api.NumeroService.validate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import br.com.unipix.api.NumeroService.exception.BusinessException;


public class ValidateArquivo {
	
	public static String separadorCsv(Integer lineToSkip, MultipartFile file) throws IOException {
		Reader reader = new InputStreamReader(file.getInputStream());
		CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(lineToSkip)
				.withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build();

		String[] lineTest;
		try {
			lineTest = csvReader.readNext();
			reader.close();
			csvReader.close();
		} catch (CsvValidationException | IOException e) {
			throw new BusinessException("Arquivo CSV Invalido");
		}

		if (lineTest == null) {
			throw new BusinessException("Arquivo CSV vazio");
		}

		List<String> colunas = Arrays.asList(lineTest[0].split(","));
		if (colunas.size() > 1) {
			return ",";
		}

		return ";";
	}

	public static String separadorCsv(Integer lineToSkip, InputStream file) throws IOException {
		Reader reader = new InputStreamReader(file);
		CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(lineToSkip)
				.withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build();

		String[] lineTest;
		try {
			lineTest = csvReader.readNext();
			reader.close();
			csvReader.close();
		} catch (CsvValidationException | IOException e) {
			throw new BusinessException("Arquivo CSV Invalido");
		}

		List<String> colunas = Arrays.asList(lineTest[0].split(","));
		if (colunas.size() > 1) {
			return ",";
		}
		return ";";
	}
}
