package br.com.mooncompass.cotrollers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.mooncompass.enums.Trend;
import br.com.mooncompass.services.IdentifyTrendService;

@RestController
public class IdentifyTrendController {

	@Autowired
	private IdentifyTrendService service;

	@CrossOrigin(origins = "http://localhost:19006")
	@GetMapping("/{paper}")
	public ResponseEntity<Trend> identifyTrend(@PathVariable String paper) throws IOException {

		Trend trend = service.run(paper);
		return new ResponseEntity<>(trend, HttpStatus.OK);
	}

}
