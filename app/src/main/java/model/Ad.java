package model;

public class Ad {
	private int id;
	private User user;
	private boolean driver;
	private String title;
	private String description;
	private int nbPlace;
	private boolean airCond;
	private boolean heater;
	
	public Ad(int id, User user, boolean driver, String title, String description, int nbPlace, boolean airCond, boolean heater) {
		this.id = id;
		this.user = user;
		this.driver = driver;
		this.title = title;
		this.description = description;
		this.nbPlace = nbPlace;
		this.airCond = airCond;
		this.heater = heater;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isDriver() {
		return driver;
	}

	public void setDriver(boolean driver) {
		this.driver = driver;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNbPlace() {
		return nbPlace;
	}

	public void setNbPlace(int nbPlace) {
		this.nbPlace = nbPlace;
	}

	public boolean isAirCond() {
		return airCond;
	}

	public void setAirCond(boolean airCond) {
		this.airCond = airCond;
	}

	public boolean isHeater() {
		return heater;
	}

	public void setHeater(boolean heater) {
		this.heater = heater;
	}
}
