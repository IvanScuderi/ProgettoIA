package euristica;

import java.util.List;

import core.Euristica;
import core.MurusGallicus;

//NON UTILIZZATA
public class EuristicaPond implements Euristica{
	
	@SuppressWarnings("unused")
	private String colore;
	
	private int VALOREDIST1= 0;
	private int VALOREDIST2= 32 + 27;
	private int VALOREDIST3= 16 + 9;
	private int VALOREDIST4= 8 + 3;
	private int VALOREDIST5= 4;
	private int VALOREDIST6= 2;
	
	
	
	private int VALOREDISTADV1= 0;
	private int VALOREDISTADV2= 32 + 16; 
	private int VALOREDISTADV3= 16 + 8;
	private int VALOREDISTADV4= 8  + 4;
	private int VALOREDISTADV5= 4;   
	private int VALOREDISTADV6= 2;
	
	private final int VALORETORRI = 3 ;
	
	public int calcolaEuristica(MurusGallicus configurazione, boolean massimizzatore, int vittoria, int livello) {
		//WINNING CONDITION CON IMPLEMENTAZIONE DI A*
		
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
		
		List<Integer> torri, torriAdv;
		int mediaDist = 0;
		int mediaDistAdv = 0;
		//SONO MASSIMIZZATORE
		if(massimizzatore) {
			
			torri = configurazione.getTorriMie();
			torriAdv = configurazione.getTorriAvversario();
			
			//SONO IL BIANCO ARRIVO IN 6
			if(configurazione.getColoreMio() == 'B') { 
				int sommePesi = 0;
				int sommePesiAdv = 0;
				//CALCOLO MEDIA PONDERATA PER LE MIE TORRI
				for(Integer torre : torri) {
					int rigaTorre = torre/8;
					int distTorre = 6 - rigaTorre;
					
					if(distTorre == 1) {
						mediaDist += distTorre*VALOREDIST1; 
						sommePesi += VALOREDIST1;
					}
					
					if(distTorre == 2) {
						mediaDist += distTorre*VALOREDIST2; 
						sommePesi += VALOREDIST2;
					}
					
					if(distTorre == 3) {
						mediaDist += distTorre*VALOREDIST3; 
						sommePesi += VALOREDIST3;
					}
					
					if(distTorre == 4) {
						mediaDist += distTorre*VALOREDIST4; 
						sommePesi += VALOREDIST4;
					}
					
					if(distTorre == 5) {
						mediaDist += distTorre*VALOREDIST5; 
						sommePesi += VALOREDIST5;
					}
					
					if(distTorre == 6) {
						mediaDist += distTorre*VALOREDIST6; 
						sommePesi += VALOREDIST6;
					}
				}
				mediaDist = mediaDist/sommePesi;
				//L'AVVERSARIO SARA' NERO E ARRIVA IN 0
				
				for(Integer torreAdv : torriAdv) { //TROVO LA TORREADV PIU VICINA ALLA MIA BASE
					int distTorreAdv = torreAdv/8;
					
					if(distTorreAdv == 1) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV1; 
						sommePesiAdv += VALOREDISTADV1;
					}
					
					if(distTorreAdv == 2) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV2; 
						sommePesiAdv += VALOREDISTADV2;
					}
					
					if(distTorreAdv == 3) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV3; 
						sommePesiAdv += VALOREDISTADV3;
					}
					
					if(distTorreAdv == 4) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV4; 
						sommePesiAdv += VALOREDISTADV4;
					}
					
					if(distTorreAdv == 5) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV5; 
						sommePesiAdv += VALOREDISTADV5;
					}
					
					if(distTorreAdv == 6) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV6; 
						sommePesiAdv += VALOREDISTADV6;
					}
				}
				mediaDistAdv = mediaDistAdv/sommePesiAdv;
			}
			
			//SONO IL NERO ARRIVO IN 0
			else { 
				int sommePesi = 0;
				int sommePesiAdv = 0;
				
				for(Integer torre : torri) {
					int distTorre = torre/8;
					
					if(distTorre == 1) {
						mediaDist += distTorre*VALOREDIST1; 
						sommePesi += VALOREDIST1;
					}
					
					if(distTorre == 2) {
						mediaDist += distTorre*VALOREDIST2; 
						sommePesi += VALOREDIST2;
					}
					
					if(distTorre == 3) {
						mediaDist += distTorre*VALOREDIST3; 
						sommePesi += VALOREDIST3;
					}
					
					if(distTorre == 4) {
						mediaDist += distTorre*VALOREDIST4; 
						sommePesi += VALOREDIST4;
					}
					
					if(distTorre == 5) {
						mediaDist += distTorre*VALOREDIST5; 
						sommePesi += VALOREDIST5;
					}
					
					if(distTorre == 6) {
						mediaDist += distTorre*VALOREDIST6; 
						sommePesi += VALOREDIST6;
					}
				}
				mediaDist = mediaDist/sommePesi;
				
				//L'AVVERSARIO SARA' BIANCO E ARRIVA IN 6
				for(Integer torreAdv : torriAdv) { 
					int rigaTorreAdv = torreAdv/8;
					int distTorreAdv = 6 - rigaTorreAdv;
					
					if(distTorreAdv == 1) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV1; 
						sommePesiAdv += VALOREDISTADV1;
					}
					
					if(distTorreAdv == 2) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV2; 
						sommePesiAdv += VALOREDISTADV2;
					}
					
					if(distTorreAdv == 3) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV3; 
						sommePesiAdv += VALOREDISTADV3;
					}
					
					if(distTorreAdv == 4) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV4; 
						sommePesiAdv += VALOREDISTADV4;
					}
					
					if(distTorreAdv == 5) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV5; 
						sommePesiAdv += VALOREDISTADV5;
					}
					
					if(distTorreAdv == 6) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV6; 
						sommePesiAdv += VALOREDISTADV6;
					}
				}
				mediaDistAdv = mediaDistAdv/sommePesiAdv;
			}
		}
		
		//-----------------------------------------------------------------
		
		//SONO MINIMIZZATORE
		else { 
			torri = configurazione.getTorriAvversario();
			torriAdv = configurazione.getTorriMie(); //per l'avversario le torri avversarie sono le mie
			//SONO BIANCO ARRIVO IN 6
			if(configurazione.getColoreAvversario() == 'B') { 
				int sommePesi = 0;
				int sommePesiAdv = 0;
				for(Integer torre : torri) {
					int rigaTorre = torre/8;
					int distTorre = 6 - rigaTorre;
					
					if(distTorre == 1) {
						mediaDist += distTorre*VALOREDIST1; 
						sommePesi += VALOREDIST1;
					}
					
					if(distTorre == 2) {
						mediaDist += distTorre*VALOREDIST2; 
						sommePesi += VALOREDIST2;
					}
					
					if(distTorre == 3) {
						mediaDist += distTorre*VALOREDIST3; 
						sommePesi += VALOREDIST3;
					}
					
					if(distTorre == 4) {
						mediaDist += distTorre*VALOREDIST4; 
						sommePesi += VALOREDIST4;
					}
					
					if(distTorre == 5) {
						mediaDist += distTorre*VALOREDIST5; 
						sommePesi += VALOREDIST5;
					}
					
					if(distTorre == 6) {
						mediaDist += distTorre*VALOREDIST6; 
						sommePesi += VALOREDIST6;
					}
				}
				mediaDist = mediaDist/sommePesi;
				
				//L'AVVERASARIO SARA' NERO E ARRIVA IN 0
				for(Integer torreAdv : torriAdv) { 
					int distTorreAdv = torreAdv/8;
					
					if(distTorreAdv == 1) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV1; 
						sommePesiAdv += VALOREDISTADV1;
					}
					
					if(distTorreAdv == 2) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV2; 
						sommePesiAdv += VALOREDISTADV2;
					}
					
					if(distTorreAdv == 3) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV3; 
						sommePesiAdv += VALOREDISTADV3;
					}
					
					if(distTorreAdv == 4) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV4; 
						sommePesiAdv += VALOREDISTADV4;
					}
					
					if(distTorreAdv == 5) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV5; 
						sommePesiAdv += VALOREDISTADV5;
					}
					
					if(distTorreAdv == 6) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV6; 
						sommePesiAdv += VALOREDISTADV6;
					}
				}
				mediaDistAdv = mediaDistAdv/sommePesiAdv;
			}
			
			//SONO NERO ARRIVO IN 0
			else { 
				int sommePesi = 0;
				int sommePesiAdv = 0;
				for(Integer torre : torri) {
					int distTorre = torre/8;
					
					if(distTorre == 1) {
						mediaDist += distTorre*VALOREDIST1; 
						sommePesi += VALOREDIST1;
					}
					
					if(distTorre == 2) {
						mediaDist += distTorre*VALOREDIST2; 
						sommePesi += VALOREDIST2;
					}
					
					if(distTorre == 3) {
						mediaDist += distTorre*VALOREDIST3; 
						sommePesi += VALOREDIST3;
					}
					
					if(distTorre == 4) {
						mediaDist += distTorre*VALOREDIST4; 
						sommePesi += VALOREDIST4;
					}
					
					if(distTorre == 5) {
						mediaDist += distTorre*VALOREDIST5; 
						sommePesi += VALOREDIST5;
					}
					
					if(distTorre == 6) {
						mediaDist += distTorre*VALOREDIST6; 
						sommePesi += VALOREDIST6;
					}
				}
				mediaDist = mediaDist/sommePesi;
				
				//L'AVVERSARIO SARA' BIANCO E ARRIVA IN 6
				for(Integer torreAdv : torriAdv) { //TROVO LA TORREADV PIU VICINA ALLA MIA BASE
					int rigaTorreAdv = torreAdv/8;
					int distTorreAdv = 6 - rigaTorreAdv;
					
					if(distTorreAdv == 1) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV1; 
						sommePesiAdv += VALOREDISTADV1;
					}
					
					if(distTorreAdv == 2) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV2; 
						sommePesiAdv += VALOREDISTADV2;
					}
					
					if(distTorreAdv == 3) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV3; 
						sommePesiAdv += VALOREDISTADV3;
					}
					
					if(distTorreAdv == 4) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV4; 
						sommePesiAdv += VALOREDISTADV4;
					}
					
					if(distTorreAdv == 5) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV5; 
						sommePesiAdv += VALOREDISTADV5;
					}
					
					if(distTorreAdv == 6) {
						mediaDistAdv += distTorreAdv*VALOREDISTADV6; 
						sommePesiAdv += VALOREDISTADV6;
					}
				}
				mediaDistAdv = mediaDistAdv/sommePesiAdv;
			}
		}
		
		int euristica = 0;
		
		
		
		int numeroTorri = configurazione.getTorriMie().size() - configurazione.getTorriAvversario().size();
		
		if(massimizzatore) {
			euristica += mediaDist;
			euristica -= mediaDistAdv;
			euristica += numeroTorri*VALORETORRI;
			
		}
		else {
			euristica -= mediaDist;
			euristica += mediaDistAdv;
			euristica += numeroTorri*VALORETORRI; //POTREBBE GIA ESSERE NEGATIVO
			
		}
		
		return euristica;
	}

	@Override
	public void setColore(String colore) {
		this.colore = colore;
	}

}
