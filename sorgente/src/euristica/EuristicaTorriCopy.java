package euristica;

import java.util.List;

import core.Euristica;
import core.MurusGallicus;

//NON UTILIZZATA
public class EuristicaTorriCopy implements Euristica {
	
	@SuppressWarnings("unused")
	private String colore;
	
	private int VALOREDIST;
	private int VALOREDISTADV;
	private int VALORETORRI;
	
	public void setColore(String colore) {
		
		this.colore = colore;
		
		if(colore.equals("white")) {
			VALOREDIST = 16;
			VALOREDISTADV = 10;
			VALORETORRI = 3;
		}
		else {
			VALOREDIST = 10;
			VALOREDISTADV = 16;
			VALORETORRI = 3;
		}
	}
	
	public int calcolaEuristica(MurusGallicus configurazione, boolean massimizzatore, int vittoria, int livello) {
		
		//VINCE MASSIMIZZATORE
		if(massimizzatore && vittoria == 1)
			return 1000 - livello;
		//PERDE MASSIMIZZATORE
		if(massimizzatore && vittoria == -1)
			return -1000 + livello;
				
		//VINCE MINIMIZZATORE
		if(!massimizzatore && vittoria == -1)
			return -1000 + livello;
		//PERDE MINIMIZZATORE
		if(!massimizzatore && vittoria == 1)
			return 1000 - livello;
				
		List<Integer> torri;
		List<Integer> torriAdv;
		int minDist = Integer.MAX_VALUE; // distanza della torre piu vicina alla base avversaria
		int minDistAdv = Integer.MAX_VALUE;
		
		if(massimizzatore) {
			torri = configurazione.getTorriMie();
			torriAdv = configurazione.getTorriAvversario();
					
			if(configurazione.getColoreMio() == 'B') { //COLORE BIANCO ARRIVO IN 6
				for(Integer torre : torri) { //TROVO LA MIA TORRE PIU VICINA ALLA BASE ADV
					int rigaTorre = torre/8;
					int distTorre = 6 - rigaTorre;
						
					if(minDist > distTorre)
						minDist = distTorre; // prendo il piu vicino alla base avversaria
				}
						
				for(Integer torreAdv : torriAdv) { //TROVO LA TORREADV PIU VICINA ALLA MIA BASE
					int rigaTorreAdv = torreAdv/8;
					if(minDistAdv > rigaTorreAdv) 
						minDistAdv = rigaTorreAdv;
				}
			}
			else { //COLORE NERO ARRIVO IN 0
				for(Integer torre : torri) {
					int rigaTorre = torre/8;
					if(minDist > rigaTorre)
						minDist = rigaTorre;
				}
						
				for(Integer torreAdv : torriAdv) { //TROVO LA TORREADV PIU VICINA ALLA MIA BASE
					int rigaTorreAdv = torreAdv/8;
					int distTorreAdv = 6 - rigaTorreAdv;
							
					if(minDistAdv > distTorreAdv) {
						minDistAdv = distTorreAdv;
					}
				}
			}
			//VADO A CONTROLLARE LE PIETRE
			
			
		} // MASSIMIZZATORE
				
		else { 
			torri = configurazione.getTorriAvversario();
			torriAdv = configurazione.getTorriMie(); //per l'avversario le torri avversarie sono le mie
					
			if(configurazione.getColoreAvversario() == 'B') { //COLORE BIANCO ARRIVO IN 6
				for(Integer torre : torri) {
					int rigaTorre = torre/8;
					int distTorre = 6 - rigaTorre;
					if(minDist > distTorre)
						minDist = distTorre; // prendo il piu vicino alla base avversaria
				}
						
				for(Integer torreAdv : torriAdv) { //TROVO LA TORREADV PIU VICINA ALLA MIA BASE
					int rigaTorreAdv = torreAdv/8;
					if(minDistAdv > rigaTorreAdv) 
						minDistAdv = rigaTorreAdv;
					
				}
						
			}
			else { //COLORE NERO ARRIVO IN 0
				for(Integer torre : torri) {
					int rigaTorre = torre/8;
					if(minDist > rigaTorre)
						minDist = rigaTorre;
				}
						
				for(Integer torreAdv : torriAdv) { //TROVO LA TORREADV PIU VICINA ALLA MIA BASE
					int rigaTorreAdv = torreAdv/8;
					int distTorreAdv = 6 - rigaTorreAdv;
							
					if(minDistAdv > distTorreAdv) {
						minDistAdv = distTorreAdv;
					}
				}
			}
		} //MINIMIZZATORE
				
		int euristica = 0;
		
		int valoreDist = 6 - minDist;
		int valoreDistAdv = 6 - minDistAdv;
		int numeroTorri = configurazione.getTorriMie().size() - configurazione.getTorriAvversario().size();
				
		if(massimizzatore) {
			euristica += valoreDist*VALOREDIST;
			euristica -= valoreDistAdv*VALOREDISTADV;
			euristica += numeroTorri*VALORETORRI;
		}
		else {
			euristica -= valoreDist*VALOREDIST;
			euristica += valoreDistAdv*VALOREDISTADV;
			euristica += numeroTorri*VALORETORRI; //POTREBBE GIA ESSERE NEGATIVO
		}
				
		return euristica;
	}

}
