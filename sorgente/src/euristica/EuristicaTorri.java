package euristica;

import java.util.List;

import core.Euristica;
import core.MurusGallicus;

public class EuristicaTorri implements Euristica {

	private String colore;
	
	//TIENE CONTO DELLA DISTANZA DELLA MIA TORRE MAGGIORMENTE VICINA ALLA BASE AVVERSARIA
	private int VALOREDIST;
	//TIENE CONTO DELLA DISTANZA DELLA TORRE AVVERSARIA MAGGIORMENTE VICINA ALLA MIA BASE
	private int VALOREDISTADV;
	//MASSIMIZZA LA DIFFERENZA TRA IL NUMERO DI TORRI DEI DUE GIOCATORI
	private int VALORETORRI;
	//TIENE CONTO DELLA CENTRALITA' DELLE TORRI SULLA SCACCHIERA 
	private int VALOREMID;
	//TIENE CONTO DEL NUMERO DI MOSSE DELLE TORRI BLOCCATE DALLE PIETRE
	private int VALOREPIETRE;
	
	//VALORI CHE TENGONO CONTO DELLA POSIZIONE DELLE PIETRE AVVERSARIE SULLA SCACCHIERA
	private int PIETRARIGABASSA;		//RIGA D SE SIAMO NERI O BIANCHI
	private int PIETRARIGAINTERMEDIA;	//RIGA C SE SIAMO NERI, RIGA E SE SIAMO BIANCHI
	private int PIETRARIGAALTA;			//RIGA B SE SIAMO NERI, RIGA F SE SIAMO BIANCHI
	
	public void setColore(String colore) {
		
		this.colore = colore;
		
		if(colore.equals("white")) {
			VALOREDIST = 16;
			VALOREDISTADV = 9; //con 10 battiamo ambigulus ma perdiamo contro di noi, con 9 battiamo copy in meno mosse 
			VALORETORRI = 4; // se lo aumento peggiora
			VALOREMID = 5; // se lo diminuisco peggiora: da 3 a 5 migiora 
			VALOREPIETRE = 3; //� un divisore, piu � basso, piu peso do alle pietre 3 = SWEETSPOT
			PIETRARIGABASSA = 3;
			PIETRARIGAINTERMEDIA = 4; //ha pi� valore pur essendo intermedia perch� se poi l'adv ci crea una torre pu� vincere in 1 mossa
			PIETRARIGAALTA = 2;
		}
		else {
			VALOREDIST = 8; //8
			VALOREDISTADV = 14;	//14
			VALORETORRI = 5;
			VALOREMID = 5; // da 4 a 5 ho trovato migliorie
			VALOREPIETRE = 2; // da 5 a 2 ho trovato migliorie
			PIETRARIGABASSA = 3;
			PIETRARIGAINTERMEDIA = 4; // questi 3 valori diminuendoli peggioriamo e anche aumentandoli
			PIETRARIGAALTA = 2;
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
		List<Integer> pietre;
		List<Integer> pietreAdv;
		int minDist = Integer.MAX_VALUE; // distanza della torre piu vicina alla base avversaria
		int minDistAdv = Integer.MAX_VALUE;
		int torriMid = 0;
		int blocchiPietre = 0;
		
		if(massimizzatore) {
			torri = configurazione.getTorriMie();
			torriAdv = configurazione.getTorriAvversario();
			pietre = configurazione.getPietreMie();
			pietreAdv = configurazione.getPietreAvversario();
			
			if(configurazione.getColoreMio() == 'B') { //COLORE BIANCO ARRIVO IN 6
				for(Integer torre : torri) { //TROVO LA MIA TORRE PIU VICINA ALLA BASE ADV
					int rigaTorre = torre/8;
					int distTorre = 6 - rigaTorre;
				
					if(minDist > distTorre)
						minDist = distTorre; // prendo il piu vicino alla base avversaria
					
					int colonna = torre%8; 
					if ( colonna >= 2 && colonna <= 5)
						torriMid ++;
				}
				
				for(Integer torreAdv : torriAdv) { //TROVO LA TORREADV PIU VICINA ALLA MIA BASE
					int rigaTorreAdv = torreAdv/8;
					if(minDistAdv > rigaTorreAdv) {
						minDistAdv = rigaTorreAdv;
					}
					
					blocchiPietre += calcolaBlocchiMosse(pietre, torreAdv);
				}
			}
			else { //COLORE NERO ARRIVO IN 0
				for(Integer torre : torri) {
					int rigaTorre = torre/8;
					if(minDist > rigaTorre)
						minDist = rigaTorre;
					
					int colonna = torre%8; 
					if ( colonna >= 2 && colonna <= 5) 
						torriMid ++;
				}
				
				for(Integer torreAdv : torriAdv) { //TROVO LA TORREADV PIU VICINA ALLA MIA BASE
					int rigaTorreAdv = torreAdv/8;
					int distTorreAdv = 6 - rigaTorreAdv;
					
					if(minDistAdv > distTorreAdv) {
						minDistAdv = distTorreAdv;
					}
					
					blocchiPietre += calcolaBlocchiMosse(pietre, torreAdv);
				}
			}
		} // MASSIMIZZATORE
		
		else { //MINIMIZZATORE
			torri = configurazione.getTorriAvversario();
			torriAdv = configurazione.getTorriMie(); //per l'avversario le torri avversarie sono le mie
			pietre = configurazione.getPietreAvversario();
			pietreAdv = configurazione.getPietreMie();
			
			if(configurazione.getColoreAvversario() == 'B') { //COLORE BIANCO ARRIVO IN 6
				for(Integer torre : torri) {
					int rigaTorre = torre/8;
					int distTorre = 6 - rigaTorre;
					if(minDist > distTorre)
						minDist = distTorre; // prendo il piu vicino alla base avversaria
					
					int colonna = torre%8; 
					if ( colonna >= 2 && colonna <= 5) 
						torriMid ++;
				}
				
				for(Integer torreAdv : torriAdv) { //TROVO LA TORREADV PIU VICINA ALLA MIA BASE
					int rigaTorreAdv = torreAdv/8;
					if(minDistAdv > rigaTorreAdv) {
						minDistAdv = rigaTorreAdv;
					}
					
					blocchiPietre += calcolaBlocchiMosse(pietre, torreAdv);
				}
				
			}
			else { //COLORE NERO ARRIVO IN 0
				for(Integer torre : torri) {
					int rigaTorre = torre/8;
					if(minDist > rigaTorre)
						minDist = rigaTorre;
					
					int colonna = torre%8; 
					if ( colonna >= 2 && colonna <= 5)
						torriMid ++;
				}
				
				for(Integer torreAdv : torriAdv) { //TROVO LA TORREADV PIU VICINA ALLA MIA BASE
					int rigaTorreAdv = torreAdv/8;
					int distTorreAdv = 6 - rigaTorreAdv;
					
					if(minDistAdv > distTorreAdv) {
						minDistAdv = distTorreAdv;
					}
					
					blocchiPietre += calcolaBlocchiMosse(pietre, torreAdv);
				}
			}
			
		} //MINIMIZZATORE
		
		int euristica = 0;
		
		int valoreDist = 6 - minDist;
		int valoreDistAdv = 6 - minDistAdv;
		int numeroTorri = configurazione.getTorriMie().size() - configurazione.getTorriAvversario().size();
		int densitaPietre = calcolaDensitaPietre(pietreAdv);
		
		if(massimizzatore) {
			euristica += valoreDist*VALOREDIST;
			euristica -= valoreDistAdv*VALOREDISTADV;
			euristica += numeroTorri*VALORETORRI;
			euristica += torriMid*VALOREMID;
			euristica += blocchiPietre/VALOREPIETRE;
			euristica -= densitaPietre;
		}
		else {
			euristica -= valoreDist*VALOREDIST;
			euristica += valoreDistAdv*VALOREDISTADV;
			euristica += numeroTorri*VALORETORRI; //POTREBBE GIA ESSERE NEGATIVO
			euristica -= torriMid*VALOREMID;
			euristica -= blocchiPietre/VALOREPIETRE;
			euristica += densitaPietre;
		}
		
		return euristica;
	}

	private int calcolaBlocchiMosse(List<Integer> pietre, int torre) {
		// RESTITUISCE LA SOMMA DI MOSSE BLOCCATE DELLA TORREADV  DALLE PIETRE
		int somma = 0;
		
		//CALCOLO RIGA E COLONNA DELLA TORRE
		int rigaT = torre/8;
		int colonnaT = torre%8;
		
		for(Integer pietra : pietre) {
			int rigaP = pietra/8;
			int colonnaP = pietra%8;
			
			int distRiga = Math.abs(rigaT - rigaP);
			int distColonna = Math.abs(colonnaT - colonnaP);
			
			//PIETRA A DIST 1 DA TORRE
			if((distRiga == 1 && distColonna == 0) || (distRiga == 0 && distColonna == 1)) { //1 CASELLA A SUD NORD EST OVEST
				somma++;
				continue;
			}
			
			if(distRiga == 1 && distColonna == 1) { //1 CASELLA A SW SE NW NE
				somma++;
				continue;
			}
			
			//PIETRA A DIST 2 DA TORRE
			if((distRiga == 2 && distColonna == 0) || (distRiga == 0 && distColonna == 2)) { //2 CASELLE A  SUD NORD EST E OVEST
				somma+= 2;
				continue;
			}
			
			if(distRiga == 2 && distColonna == 2) { //2 CASELLE A SW SE NW NE
				somma+= 2;
				continue;
			}	
		}
		//System.out.println(somma);
		return somma;
		
	}
	
	private int calcolaDensitaPietre(List<Integer> pietre) {
		//VA A CALCOLARE IN NUMERO DI PIETRE CHE SI TROVANO IN RIGHE FASTIDIOSE PER L'ATTACCANTE
		//METODO IMPIEGATO NELLA VALUTAZIONE DELL'EURISTICA PER IL PURO SCOPO DIFENSIVO
		int d = 0;
		if(colore.equals("white")) {
		//LE PIETRE CHE MI DANNO FASTIDIO SONO TRA 24,32 (RIGA D); 16,23 (RIGA E); 8,15 (RIGA F)
			for(Integer pietra : pietre) {
				if(pietra >= 24 && pietra <= 32)//RIGA D
					d += PIETRARIGABASSA;
				if(pietra >= 16 && pietra <= 23)//RIGA E
					d += PIETRARIGAINTERMEDIA;
				if(pietra >= 8 && pietra <= 15)//RIGA F
					d += PIETRARIGAALTA;
			}
		}
		else {
		//LE PIETRE CHE MI DANNO FASTIDIO SONO TRA 24,32 (RIGA D); 32,39 (RIGA C); 40,47 (RIGA B)
			for(Integer pietra : pietre) {
				if(pietra >= 24 && pietra <= 32)//RIGA D
					d += PIETRARIGABASSA;
				if(pietra >= 32 && pietra <= 39)//RIGA C
					d += PIETRARIGAINTERMEDIA;
				if(pietra >= 40 && pietra <= 47)//RIGA B
					d += PIETRARIGAALTA;
			}
		}
		return d;
	}

}//class

