package simo.transport.backend;

public class ReferenceDTO {
	
	private String description;
	private int privateCode;
	private int originId;
	private int destinationId;
	private String origin;
	private String destination;
	private int routeId;
	private int originSeq;
	private int destinationSeq;
	private boolean[] days;
	

	public ReferenceDTO() {
		// TODO Auto-generated constructor stub
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrivateCode() {
		return privateCode;
	}

	public void setPrivateCode(int privateCode) {
		this.privateCode = privateCode;
	}

	public boolean[] getDays() {
		return days;
	}

	public void setDays(boolean[] days) {
		this.days = days;
	}

	public int getOriginId() {
		return originId;
	}

	public void setOriginId(int originId) {
		this.originId = originId;
	}

	public int getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(int destinationId) {
		this.destinationId = destinationId;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public int getOriginSeq() {
		return originSeq;
	}

	public void setOriginSeq(int originSeq) {
		this.originSeq = originSeq;
	}

	public int getDestinationSeq() {
		return destinationSeq;
	}

	public void setDestinationSeq(int destinationSeq) {
		this.destinationSeq = destinationSeq;
	}

}
