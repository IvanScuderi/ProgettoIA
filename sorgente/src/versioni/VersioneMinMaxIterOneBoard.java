package versioni;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import core.Euristica;
import core.MinMaxAlg;
import core.MurusGallicus;

//VERSIONE NON UTILIZZATA
public class VersioneMinMaxIterOneBoard implements MinMaxAlg {

	private MurusGallicus radice;
	private String colore;
	private Euristica euristica;
	
	//-------------------COSTRUTTORE-------------------------
	public VersioneMinMaxIterOneBoard(Euristica euristica) {
		this.euristica=euristica;
	}
	
	//------------- GETTERS AND SETTERS -----------------------------
	public MurusGallicus getRadice() {
		return radice;
	}

	public void setRadice(MurusGallicus radice) {
		this.radice = radice;
	}
	
	public int getNumeroTorri()
	{
		return radice.getNumeroTorri();
	}
	
	public Euristica getEuristica() {
		return euristica;
	}

	public void setEuristica(Euristica euristica) {
		this.euristica = euristica;
	}
	
	//--------------METODI DINAMICI-----------------------------------
	public void inizializza(String colore) {
		this.colore = colore;
		this.radice = new MurusGallicus(colore);
	}
	
	public void mossaAvversario(String mossa) {
		this.radice.effettuaMossaAvversaria(mossa);
	}
	
	//MINMAX ALG restituisce la mossa da effettuare come String
	public String valuta(int lvlMax) {
		//inizializzo il Nodo root
		Nodo nodo = new Nodo();
		nodo.configurazione = radice;
		nodo.livello = 0;
		nodo.massimizzatore = true;
		nodo.figli = new LinkedList<>();	
		//inizializzo le strutture che mi serviranno
		ArrayList<Nodo> coda = new ArrayList<>();
				
		coda.add(nodo);
		//CICLO DI DISCESA
		for(int i = 0; i < coda.size(); i++) {
			//PRENDO IL NODO IN TESTA
			Nodo testa = coda.get(i);
			int j = 0;
			//AGGIORNO IL GIOCO A QUELLO DELLA TESTA PER CALCOLARE LA MOSSA
			while(j< testa.listaMosse.size()) {
				if(j%2==0) {
					//MOSSE DA MASSIMIZZATORE
					int[] mossaDaEseguire = testa.listaMosse.get(j);
					testa.configurazione.effettuaMossa(testa.configurazione.getBoard(), true, mossaDaEseguire[0], mossaDaEseguire[1]);
				}
				else {
					//MOSSE DA MINIMIZZATORE
					int[] mossaDaEseguire = testa.listaMosse.get(j);
					testa.configurazione.effettuaMossa(testa.configurazione.getBoard(), false, mossaDaEseguire[0], mossaDaEseguire[1]);
				}
				j+=1;
			}
			int vittoria = testa.configurazione.vittoria(testa.massimizzatore);
			if(testa.livello<lvlMax && vittoria == 0) {
			//SONO UN NODO INTERMEDIO
				if(testa.massimizzatore)
					testa.euristica = Integer.MIN_VALUE;
				else
					testa.euristica = Integer.MAX_VALUE;
						
				List<Integer> torri;
				if(testa.massimizzatore)
					torri = testa.configurazione.getTorriMie();
				else
					torri = testa.configurazione.getTorriAvversario();
						
				List<Integer> mossePerTorre;
				for(Integer torre : torri) {
					mossePerTorre = MurusGallicus.calcolaMosse(testa.configurazione.getBoard(), torre, testa.massimizzatore);
					for(Integer mossa : mossePerTorre) {
								
						//per ogni mossa devo creare un nodo
						Nodo figlio = new Nodo();
						figlio.padre = testa;
						figlio.massimizzatore = !testa.massimizzatore;
						figlio.livello = testa.livello + 1;
						//MOSSA CHE MI GENERA IL FIGLIO LA SALVO NEL FIGLIO
						figlio.mossa[0] = torre;
						figlio.mossa[1] = mossa;
						//EFFETTUO LA MOSSA PER MODIFICARE LA BOARD DEL FIGLIO
						figlio.configurazione = new MurusGallicus(colore);
						//IL FIGLIO DEVE FARE LA SUA MOSSA
						figlio.listaMosse.add(new int[] {torre,mossa});
						testa.figli.add(figlio);
						coda.add(figlio);
								
					}
				}
						
			}//if
			if(testa.livello>=lvlMax || vittoria!= 0) {
				//SONO UNA FOGLIA
				testa.euristica = euristica.calcolaEuristica(testa.configurazione, testa.massimizzatore, vittoria, testa.livello);
			}
		}//for
				
		//CICLO DI SALITA E PROPAGAZIONE VALORI EURISTICA

		for(int i = coda.size()-1; i>= 0; i--){
			//PRENDO L'ULTIMO NODO (INIZIALMENTE PADRE DI ALCUNE FOGLIE)
			Nodo testa = coda.get(i);
			if(i != 0){ //se non sono la radice
				if(testa.massimizzatore) { // sono massimizzatore
					List<Nodo> figli = testa.figli;
					for(Nodo figlio : figli) {
						if(figlio.euristica > testa.euristica) {
							testa.euristica = figlio.euristica;
						}
					}
				}
				else { //sono minimizatore
					List<Nodo> figli = testa.figli;
					for(Nodo figlio : figli) {
						if(figlio.euristica < testa.euristica) {
								testa.euristica = figlio.euristica;
						}
					}
				}
			}
			else {
				//SONO SULLA TESTA
				List<Nodo> figli = testa.figli;
				for(Nodo figlio : figli) {
					if(figlio.euristica > testa.euristica) {
						testa.euristica = figlio.euristica;
						nodo.mossa[0] = figlio.mossa[0];
						nodo.mossa[1] = figlio.mossa[1];
						radice = figlio.configurazione;
						}
					}
				}
				coda.remove(i); //rimuovo l'oggetto che ho appena finito di utilizzare, cosi di ciclo in ciclo la struttura dati si alleggerisce
			}
			//HO LA MOSSA NELLA ROOT (interno -> TESTA, esterno -> NODO)
		return MurusGallicus.convertToString(nodo.mossa[0], nodo.mossa[1]);
	}//valuta
	
		
	private class Nodo{		
			
		@SuppressWarnings("unused")
		Nodo padre = null;
		List<Nodo> figli = new LinkedList<>();
		MurusGallicus configurazione;
		ArrayList<int[]> listaMosse = new ArrayList<>(150);
		boolean massimizzatore;
		int euristica;
		int[] mossa = new int[2];//source - dest
		int livello;
		
		public int hashCode() {
			return this.configurazione.hashCode();
		}
		
	}//Nodo
	
}
