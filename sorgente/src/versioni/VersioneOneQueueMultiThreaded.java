package versioni;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import core.Euristica;
import core.MinMaxAlg;
import core.MurusGallicus;

public class VersioneOneQueueMultiThreaded implements MinMaxAlg {
	
	private MurusGallicus radice;
	private Euristica euristica;
	
	//---------------COSTRUTTORE----------------
	public VersioneOneQueueMultiThreaded(Euristica euristica) {
		this.euristica = euristica;
	}
	
	//------------------ NODO ----------------------
	class Nodo{		
		
		Nodo padre = null;
		List<Nodo> figli = new LinkedList<>();
		MurusGallicus configurazione;
		boolean massimizzatore;
		int euristica;
		int[] mossa = new int[2];//source - dest
		int livello=0;
		
		public int hashCode() {
			return this.configurazione.hashCode();
		}
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
		this.radice = new MurusGallicus(colore);
	}
	
	public void mossaAvversario(String mossa) {
		this.radice.effettuaMossaAvversaria(mossa);
	}

	@Override
	public String valuta(int lvlMax) {
		//DALLA RADICE CREO I FIGLI
		ArrayList<Nodo> figli1 = new ArrayList<>();
		ArrayList<Nodo> figli2 = new ArrayList<>();
		
		Nodo nodo = new Nodo();
		nodo.configurazione = radice;
		nodo.massimizzatore = true;
		
		List<Integer> mossePerTorre;
		List<Integer> torri = nodo.configurazione.getTorriMie();
		int sizeTorri = torri.size();
		for(int i = 0; i < sizeTorri; i++) {
			int indexTorre = torri.get(i);
			mossePerTorre = MurusGallicus.calcolaMosse(nodo.configurazione.getBoard(), indexTorre, nodo.massimizzatore);
			for(Integer mossa : mossePerTorre) {
				//per ogni mossa devo creare un nodo
				Nodo figlio = new Nodo();
				figlio.massimizzatore = !nodo.massimizzatore;
				//MOSSA CHE MI GENERA IL FIGLIO LA SALVO NEL FIGLIO
				figlio.mossa[0] = indexTorre;
				figlio.mossa[1] = mossa;
				//EFFETTUO LA MOSSA PER MODIFICARE LA BOARD DEL FIGLIO
				figlio.configurazione = new MurusGallicus(nodo.configurazione);
				figlio.configurazione.effettuaMossa(figlio.configurazione.getBoard(), nodo.massimizzatore, indexTorre, mossa);
				if(sizeTorri%2==0) {
					if(i < sizeTorri/2) 
						figli1.add(figlio);
					else 
						figli2.add(figlio);
				}
				else {
					if(i < sizeTorri/2+1) 
						figli1.add(figlio);
					else 
						figli2.add(figlio);
				}
			}
		}
		//RICHIAMARE 2 THREAD
		ExecutorService e1 = Executors.newSingleThreadExecutor();
		ExecutorService e2 = Executors.newSingleThreadExecutor();
		
		Future<ArrayList<Integer>> thread1 = e1.submit(new Esecutore(figli1, lvlMax-1));
		Future<ArrayList<Integer>> thread2 = e2.submit(new Esecutore(figli2, lvlMax-1));
		
		//PRENDO MAX DEI DUE RISULTATI
		ArrayList<Integer> risultato1 = new ArrayList<>();
		ArrayList<Integer> risultato2 = new ArrayList<>();
		int[] mosseFinal = new int[2];
		
		try {
			risultato1 = thread1.get();
			risultato2 = thread2.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(risultato1.get(2) >= risultato2.get(2)) { 
			//RISULTATO1 MASSIMO
			mosseFinal[0] = risultato1.get(0);
			mosseFinal[1] = risultato1.get(1);
		} 
		else { 
			//RISULTATO2 MASSIMO
			mosseFinal[0] = risultato2.get(0);
			mosseFinal[1] = risultato2.get(1);
		}
		
		e1.shutdown();
		e2.shutdown();
		
		radice.effettuaMossa(radice.getBoard(), true, mosseFinal[0], mosseFinal[1]);
		return MurusGallicus.convertToString(mosseFinal[0], mosseFinal[1]);
	}
	
	//------------- THREAD ------------------
	
	class Esecutore implements Callable<ArrayList<Integer>>{
		
		private ArrayList<Nodo> nodi;
		private int lvlMax;
		
		public Esecutore(ArrayList<Nodo> nodi, int lvlMax) {
			this.nodi = nodi;
			this.lvlMax = lvlMax;
		}
		
		//MINMAX ALG restituisce la mossa da effettuare come String
		public int[] valuta(Nodo n, int lvlMax) {
			//inizializzo il Nodo root
			Nodo nodo = new Nodo();
			nodo.configurazione = n.configurazione;
			nodo.mossa = n.mossa;
			nodo.massimizzatore = false;
			//INSERIREMO SRC DEST E VALORE DI EURISTICA
			int[] risultato = new int[3];
			
			//inizializzo le strutture che mi serviranno
			ArrayList<Nodo> coda = new ArrayList<>();
			coda.add(nodo);
			int sizeCoda = coda.size();
			//CICLO DI DISCESA
			for(int i = 0; i < sizeCoda; i++) {
				//PRENDO IL NODO IN TESTA
				Nodo testa = coda.get(i);
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
							figlio.configurazione = new MurusGallicus(testa.configurazione);
							figlio.configurazione.effettuaMossa(figlio.configurazione.getBoard(), testa.massimizzatore, torre, mossa);
							testa.figli.add(figlio);
							coda.add(figlio);
							sizeCoda++;		
						}
					}		
				}
				if(testa.livello>=lvlMax || vittoria!= 0) {
					//SONO UNA FOGLIA
					//MECCANISMO EURISTICA DI A* CHE TIENE CONTO DEL LIVELLO DEL NODO
					testa.euristica = euristica.calcolaEuristica(testa.configurazione, testa.massimizzatore, vittoria, testa.livello);
				}
			}
					
			//CICLO DI SALITA E PROPAGAZIONE VALORI EURISTICA

			for(int i = sizeCoda-1; i>= 0; i--){
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
						if(figlio.euristica < testa.euristica) {
							testa.euristica = figlio.euristica;
							nodo.euristica = figlio.euristica;
						}
					}
				}
				coda.remove(i); //rimuovo l'oggetto che ho appena finito di utilizzare, cosi di ciclo in ciclo la struttura dati si alleggerisce
				sizeCoda--;
			}
			risultato[0] = nodo.mossa[0];
			risultato[1] = nodo.mossa[1];
			risultato[2] = nodo.euristica;
			return risultato;
		}//valuta
		
		@Override
		public ArrayList<Integer> call() throws Exception {
			
			int[] risultato = new int[] {0,0,Integer.MIN_VALUE};
			ArrayList<Integer> r = new ArrayList<>();
			
			for(Nodo nodo : nodi) {
				int [] risultatoParziale = valuta(nodo, lvlMax);
				
				if(risultatoParziale[2] >= risultato[2]) {
					risultato[0] = risultatoParziale[0];
					risultato[1] = risultatoParziale[1];
					risultato[2] = risultatoParziale[2];
				}
			}
			r.add(risultato[0]);
			r.add(risultato[1]);
			r.add(risultato[2]);
			return r;
		}
	}
}
