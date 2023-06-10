package br.com.mooncompass.usecases.data;

import br.com.mooncompass.enums.Groups;
import br.com.mooncompass.enums.Trend;

public class ResponseTrend {

	private Groups group;
	private Trend trend;

	public Groups getGroup() {
		return group;
	}

	public void setGroup(Groups group) {
		this.group = group;
	}

	public Trend getTrend() {
		return trend;
	}

	public void setTrend(Trend trend) {
		this.trend = trend;
	}

}
