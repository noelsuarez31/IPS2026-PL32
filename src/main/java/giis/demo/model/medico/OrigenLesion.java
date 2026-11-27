package giis.demo.model.medico;

public enum OrigenLesion {
	
		PARTIDO("Partido"), ENTRENAMIENTO("Entrenamiento"), OTRO("Otro");

		private final String texto;

		OrigenLesion(String texto) {
			this.texto = texto;
		}

		public String getTexto() {
			return texto;
		}

		public boolean requiereEvento() {
			return this != OTRO;
		}
		
}
