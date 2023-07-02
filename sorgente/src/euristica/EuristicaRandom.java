package euristica;

import java.util.Random;

import core.Euristica;
import core.MurusGallicus;

//NON UTILIZZATA
public class EuristicaRandom implements Euristica {
	
	@SuppressWarnings("unused")
	private String colore;
	
	@Override
	public int calcolaEuristica(MurusGallicus configurazione, boolean massimizzatore, int vittoria, int livello) {
		int low = 10;
		int high = 70;
		//VINCE MASSIMIZZATORE
		if(massimizzatore && vittoria == 1)
			return 100;
		//VINCE MINIMIZZATORE
		if(!massimizzatore && vittoria == -1)
			return 0;
		return new Random().nextInt(high-low)+low;
	}

	@Override
	public void setColore(String colore) {
		this.colore = colore;
	}

}
