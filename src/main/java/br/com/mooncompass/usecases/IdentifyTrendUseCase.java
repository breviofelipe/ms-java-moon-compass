package br.com.mooncompass.usecases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.mooncompass.enums.Groups;
import br.com.mooncompass.enums.Trend;
import br.com.mooncompass.usecases.data.IdentifyTrendIn;
import br.com.mooncompass.usecases.data.ResponseTrend;

public class IdentifyTrendUseCase {

	private List<ResponseTrend> groupUpUpUp = new ArrayList<>();
	private List<ResponseTrend> groupUpUpDown = new ArrayList<>();
	private List<ResponseTrend> groupUpDownDown = new ArrayList<>();
	private List<ResponseTrend> groupDownDownDown = new ArrayList<>();
	private List<ResponseTrend> groupUpDownUp = new ArrayList<>();
	private List<ResponseTrend> groupDownDownUp = new ArrayList<>();
	private List<ResponseTrend> groupDownUpUp = new ArrayList<>();
	private List<ResponseTrend> groupDownUpDown = new ArrayList<>();

	public Trend run(IdentifyTrendIn in) {

		List<Double> group = new ArrayList<>(4);
		List<List<Double>> groups = new ArrayList<>();
		List<Double> current = Arrays.asList(in.getHistory().get(0), in.getHistory().get(1), in.getHistory().get(2));
		List<Double> next = Arrays.asList(in.getHistory().get(3), in.getHistory().get(4), in.getHistory().get(5));
		List<Double> history = in.getHistory();
		String trend = verifyTrend(current, next);
		System.out.println("ATUAL: " + trend);
		List<ResponseTrend> retornaGrup = retornaGrup(trend);

	//	retornaGrup.forEach(r -> System.out.println(r.getGroup()));
		int i;
		for (i = 0; i < history.size(); i++) {

			if (group.size() % 3 == 0 && i != 0) {
				groups.add(group);
				group = new ArrayList<>(3);
				group.add(history.get(i));
			} else {
				group.add(history.get(i));
			}

		}

		// verifyTrend(current, next);
		// Collections.reverse(groups);
		Map<Integer, String> resultado = new HashMap<>();
		String texto;
		for (i = 0; i < groups.size() - 1; i++) {
			if (groups.size() > i) {
				texto = verifyTrend(groups.get(i), groups.get(i + 1));

				resultado.put(i, texto);
			}
		}
		List<ResponseTrend> trendsResponse = new ArrayList<ResponseTrend>();

		for (; i > 1; i--) {
			Groups groupEnum = Groups.valueOf(resultado.get(i - 1));
			Trend trendEnum;
			String string = resultado.get(i - 1);
			String string2 = resultado.get(i - 2);

			if (string2.startsWith("U")) {
				string = string + "_UP";
				trendEnum = Trend.UP;
			} else {
				string = string + "_DOWN";
				trendEnum = Trend.DOWN;
			}
			// System.out.println(string);
			ResponseTrend response = new ResponseTrend();
			response.setGroup(groupEnum);
			response.setTrend(trendEnum);
			trendsResponse.add(response);
		}

		// Collections.reverse(trendsResponse);
		System.out.println("---------------------- history ----------------------");

		trendsResponse.forEach(t -> {
			// System.out.println(t.getGroup() + " trend -> " + t.getTrend());
			agrupaTrend(t);
		});
		System.out.println("--- recurrence " + retornaGrup.size());
		//retornaGrup.forEach(r -> System.out.println(r.getTrend()));
		System.out.println("---------------------- trend ----------------");
		return researchHistory(retornaGrup);

	}

	private String verifyTrend(List<Double> current, List<Double> next) {

		StringBuilder builder = new StringBuilder();
		Collections.reverse(current);
		// Collections.reverse(next);
		builder.append(upOrDown(next.get(0), current.get(0)));
		builder.append(upOrDown(current.get(0), current.get(1)));
		builder.append(upOrDown(current.get(1), current.get(2)));

		// System.out.println(builder.toString().substring(0, builder.length() - 1));
		return builder.toString().substring(0, builder.length() - 1);
	}

	private String upOrDown(double current, double next) {
		if (current > next) {
			return "DOWN_";
		} else {
			return "UP_";
		}
	}

	private List<ResponseTrend> retornaGrup(String trend) {

		Groups group = Groups.valueOf(trend);
		switch (group) {
		case UP_UP_UP: {
			return groupUpUpUp;

		}
		case DOWN_DOWN_DOWN: {
			return groupDownDownDown;
		}
		case UP_DOWN_DOWN: {
			return groupUpDownDown;
		}
		case UP_UP_DOWN: {
			return groupUpUpDown;
		}
		case UP_DOWN_UP: {
			return groupUpDownUp;

		}
		case DOWN_DOWN_UP: {
			return groupDownDownUp;

		}

		case DOWN_UP_UP: {
			return groupDownUpUp;

		}
		case DOWN_UP_DOWN: {
			return groupDownUpDown;

		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + group);
		}
	}

	private void agrupaTrend(ResponseTrend responseTrend) {

		Groups group = responseTrend.getGroup();
		switch (group) {
		case UP_UP_UP: {
			groupUpUpUp.add(responseTrend);
			break;

		}
		case DOWN_DOWN_DOWN: {
			groupDownDownDown.add(responseTrend);
			break;
		}
		case UP_DOWN_DOWN: {
			groupUpDownDown.add(responseTrend);
			break;
		}
		case UP_UP_DOWN: {
			groupUpUpDown.add(responseTrend);
			break;
		}
		case UP_DOWN_UP: {
			groupUpDownUp.add(responseTrend);
			break;
		}
		case DOWN_DOWN_UP: {
			groupDownDownUp.add(responseTrend);
			break;
		}

		case DOWN_UP_UP: {
			groupDownUpUp.add(responseTrend);
			break;
		}
		case DOWN_UP_DOWN: {
			groupDownUpDown.add(responseTrend);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + group);
		}
	}

	private Trend researchHistory(List<ResponseTrend> listGroup) {

		int sizeUP = listGroup.stream().filter(trend -> trend.getTrend() == Trend.UP).collect(Collectors.toList())
				.size();
		int sizeDOWN = listGroup.stream().filter(trend -> trend.getTrend() == Trend.DOWN).collect(Collectors.toList())
				.size();

		double sizeTotal = listGroup.size();

		System.out.println("UP=" + (sizeUP / sizeTotal) * 100+"%");
		System.out.println("DOWN=" + (sizeDOWN / sizeTotal) * 100+"%");
		if (sizeUP > sizeDOWN) {
			return Trend.UP;
		} else if (sizeUP == sizeDOWN) {
			return Trend.NULL;
		}
		return Trend.DOWN;

	}

	private Groups identifyGroup(List<Double> grp) {

		if (grp.get(0) > grp.get(1) && grp.get(1) > grp.get(2) && grp.get(2) > grp.get(3)) {
			return Groups.UP_UP_UP;
		} else if (grp.get(0) < grp.get(1) && grp.get(1) < grp.get(2) && grp.get(2) < grp.get(3)) {

			return Groups.DOWN_DOWN_DOWN;

		} else if (grp.get(0) > grp.get(1) && grp.get(1) > grp.get(2) && grp.get(2) < grp.get(3)) {

			return Groups.UP_UP_DOWN;

		} else {

			return Groups.UP_DOWN_DOWN;
		}
	}

}
