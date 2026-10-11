package giis.demo.model.reserva;

public class Instalacion {
	
	private int instalacionId;
	private String name;
	
	public Instalacion(int instalacionId, String name) {
		super();
		this.instalacionId = instalacionId;
		this.name = name;
	}
	
	public int getInstalacionId() {
		return instalacionId;
	}
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "" + instalacionId + " - " + name;
	}
	
	

}
