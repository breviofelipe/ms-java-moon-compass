package br.com.mooncompass.services;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.mooncompass.enums.Trend;
import br.com.mooncompass.usecases.IdentifyTrendUseCase;
import br.com.mooncompass.usecases.data.IdentifyTrendIn;

@Service
public class IdentifyTrendService {

	public Trend run(String paper) throws IOException {
		IdentifyTrendUseCase useCase = new IdentifyTrendUseCase();

		return useCase.run(showHistory(paper));

	}

//	private IdentifyTrendIn showHistory(String paper) throws IOException {
//		Calendar from = Calendar.getInstance();
//		Calendar to = Calendar.getInstance();
//		from.add(Calendar.YEAR, -5); // from 5 years ago
//
//		Stock google = YahooFinance.get(paper, from, to, Interval.DAILY);
//		List<HistoricalQuote> history = google.getHistory();
//		List<Double> historyDoubles = new ArrayList<Double>();
//		history.forEach(d -> {
//			try {
//				historyDoubles.add(d.getClose().doubleValue());
//			} catch (NullPointerException e) {
//				historyDoubles.add(0.0);
//			}
//		});
//		Collections.reverse(historyDoubles);
//		IdentifyTrendIn in = new IdentifyTrendIn();
//		in.setHistory(historyDoubles);
//		return in;
//		// google.print();
//	}

	private IdentifyTrendIn showHistory(String paper) throws IOException {

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List> response = restTemplate.getForEntity("http://127.0.0.1:5000/history?ticker=" + paper,
				List.class);

		@SuppressWarnings("unchecked")
		List<String> historyStr = (List<String>) response.getBody().stream().map(String::valueOf)
				.collect(Collectors.toList());

		List<Double> history = historyStr.stream().map(Double::valueOf).collect(Collectors.toList());

		Collections.reverse(history);
		IdentifyTrendIn in = new IdentifyTrendIn();
		in.setHistory(history);
		return in;
	}
}
