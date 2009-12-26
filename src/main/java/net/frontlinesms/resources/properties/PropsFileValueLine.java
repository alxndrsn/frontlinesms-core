package net.frontlinesms.resources.properties;


class PropsFileValueLine implements PropsFileLine {
	private final String key;
	
	PropsFileValueLine(String key) {
		super();
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
