package test;

import java.util.List;
import java.util.Scanner;

import core.Euristica;
import core.MinMaxAlg;
import core.MurusGallicus;
import euristica.EuristicaRandom;
import euristica.EuristicaTorri;
import euristica.EuristicaTorriCopy;
import versioni.VersioneMinMaxIter;
import versioni.VersioneOneQueueMultiThreaded;

//CLASSE IMPIEGATA PER EFFETTUARE TEST
@SuppressWarnings("unused")
public class TestInput {
	
	public static void stampaMosse(MurusGallicus tree, boolean who) {
		List<Integer> torriMie = tree.getTorriMie();
		List<Integer> torriAvversario = tree.getTorriAvversario();
		if(who==true) {
			//stampo le mosse mie
			for(Integer torre : torriMie) {
				List<Integer> mosseCur = MurusGallicus.calcolaMosse(tree.getBoard(), torre, who);
				for(Integer dest : mosseCur) {
					System.out.print(MurusGallicus.convertToString(torre, dest)+"\t");
				}
				System.out.println();
			}
		}
		else {
			//stampo mosse avversario
			for(Integer torre : torriAvversario) {
				List<Integer> mosseCur = MurusGallicus.calcolaMosse(tree.getBoard(), torre, who);
				for(Integer dest : mosseCur) {
					System.out.print(MurusGallicus.convertToString(torre, dest)+"\t");
				}
				System.out.println();
			}
		}
	}

	public static void test2giocatoriScanner() {
		Scanner sc = new Scanner(System.in);
		String colore = new String("white");
		MurusGallicus tree = new MurusGallicus(colore);
		MurusGallicus.stampaBoard(tree.getBoard());
		if(colore.equals("white")) {
			while(true) {
				System.out.println();
				stampaMosse(tree, true);
				System.out.println();
				System.out.print("INSERISCI MOSSA GIOCATORE BIANCO> ");
				String mossa = sc.nextLine().trim();
				int src= Integer.parseInt(mossa.split(",")[0].trim());
				int dest= Integer.parseInt(mossa.split(",")[1].trim());		
				tree.effettuaMossa(tree.getBoard(), true, src, dest);
				System.out.println();
				MurusGallicus.stampaBoard(tree.getBoard());
				System.out.println();
				
				if(tree.vittoria(true)==1) {
					System.out.println("GIOCATORE BIANCO HA VINTO");
					break;
				}
				if(tree.vittoria(false)==-1) {
					System.out.println("GIOCATORE NERO HA VINTO");
					break;
				}
				
				stampaMosse(tree, false);
				System.out.println();
				System.out.print("INSERISCI MOSSA GIOCATORE NERO> ");
				tree.effettuaMossaAvversaria(sc.nextLine().trim());
				System.out.println();
				MurusGallicus.stampaBoard(tree.getBoard());
				System.out.println();
				
				if(tree.vittoria(true)==1) {
					System.out.println("GIOCATORE BIANCO HA VINTO");
					break;
				}
				if(tree.vittoria(false)==-1) {
					System.out.println("GIOCATORE NERO HA VINTO");
					break;
				}
				
				
			}
		}
		
		else {
			while(true) {
				System.out.println();
				stampaMosse(tree, false);
				System.out.println();
				System.out.print("INSERISCI MOSSA GIOCATORE BIANCO> ");
				tree.effettuaMossaAvversaria(sc.nextLine().trim());
				System.out.println();
				MurusGallicus.stampaBoard(tree.getBoard());
				System.out.println();
				
				if(tree.vittoria(false)==-1) {
					System.out.println("GIOCATORE BIANCO HA VINTO");
					break;
				}
				if(tree.vittoria(true)==1) {
					System.out.println("GIOCATORE NERO HA VINTO");
					break;
				}
	
				stampaMosse(tree, true);
				System.out.println();
				System.out.print("INSERISCI MOSSA GIOCATORE NERO> ");
				String mossa = sc.nextLine().trim();
				int src= Integer.parseInt(mossa.split(",")[0].trim());
				int dest= Integer.parseInt(mossa.split(",")[1].trim());		
				tree.effettuaMossa(tree.getBoard(), true, src, dest);
				System.out.println();
				MurusGallicus.stampaBoard(tree.getBoard());
				System.out.println();
				
				if(tree.vittoria(false)==-1) {
					System.out.println("GIOCATORE BIANCO HA VINTO");
					break;
				}
				if(tree.vittoria(true)==1) {
					System.out.println("GIOCATORE NERO HA VINTO");
					break;
				}
			}
		}
		sc.close();
	}
	
	public static void testGiocatoreIA(String coloreIA, int lvl, Euristica e) {
		System.out.println("COLORE IA:> "+coloreIA);
		Scanner sc = new Scanner(System.in);
		MinMaxAlg alg = new VersioneMinMaxIter(e);
		alg.inizializza(coloreIA);
		if(coloreIA.equals("black")) {
			System.out.print("INSERISCI MOSSA GIOCATORE UMANO:> ");
			String mossa = sc.nextLine().trim();
			alg.mossaAvversario(mossa);
			MurusGallicus.stampaBoard(alg.getRadice().getBoard());
		}
		while(true) {
			
			String mossaIA = alg.valuta(lvl);
			System.out.println("INSERISCI MOSSA GIOCATORE IA:> "+mossaIA);
			MurusGallicus.stampaBoard(alg.getRadice().getBoard());
			if(alg.getRadice().vittoria(true)==1) {
				System.out.println("IA HA VINTO");
				break;
			}
			if(alg.getRadice().vittoria(false)==-1) {
				System.out.println("UMANO HA VINTO");
				break;
			}
			
			System.out.print("INSERISCI MOSSA GIOCATORE UMANO:> ");
			String mossa = sc.nextLine().trim();
			alg.mossaAvversario(mossa);
			MurusGallicus.stampaBoard(alg.getRadice().getBoard());
			if(alg.getRadice().vittoria(false)==-1) {
				System.out.println("UMANO HA VINTO");
				break;
			}
			if(alg.getRadice().vittoria(true)==1) {
				System.out.println("IA HA VINTO");
				break;
			}
		}
		sc.close();
	}
	
	public static void testIAvsIA(MinMaxAlg i1, int lvlIA1, MinMaxAlg i2, int lvlIA2) {
		MinMaxAlg ia1 =  i1;
		MinMaxAlg ia2 =  i2;
		ia1.inizializza("white");
		ia2.inizializza("black");
		MurusGallicus.stampaBoard(ia1.getRadice().getBoard());
		while(true) {
			
			String mossaIA1 = ia1.valuta(lvlIA1);
			System.out.println("MOSSA IA_1 :> "+mossaIA1);
			ia2.mossaAvversario(mossaIA1);
			MurusGallicus.stampaBoard(ia1.getRadice().getBoard());
			
			if(ia1.getRadice().vittoria(true)==1) {
				System.out.println("IA_1 HA VINTO");
				break;
			}
			if(ia1.getRadice().vittoria(true)==-1) {
				System.out.println("IA_2 HA VINTO");
				break;
			}
			
			if(ia2.getRadice().vittoria(true)==1) {
				System.out.println("IA_2 HA VINTO");
				break;
			}
			if(ia2.getRadice().vittoria(true)==-1) {
				System.out.println("IA_1 HA VINTO");
				break;
			}
			
			String mossaIA2 = ia2.valuta(lvlIA2);
			System.out.println("MOSSA IA_2 :> "+mossaIA2);
			ia1.mossaAvversario(mossaIA2);
			MurusGallicus.stampaBoard(ia2.getRadice().getBoard());
			if(ia2.getRadice().vittoria(true)==1) {
				System.out.println("IA_2 HA VINTO");
				break;
			}
			if(ia2.getRadice().vittoria(true)==-1) {
				System.out.println("IA_1 HA VINTO");
				break;
			}
			if(ia1.getRadice().vittoria(true)==1) {
				System.out.println("IA_1 HA VINTO");
				break;
			}
			if(ia1.getRadice().vittoria(true)==-1) {
				System.out.println("IA_2 HA VINTO");
				break;
			}
		}
	}

	public static void testMinMaxIter(MinMaxAlg t, int lvl) {
		MinMaxAlg alg =  t;
		alg.inizializza("black");
		
		alg.mossaAvversario("G1,N");
		
		MurusGallicus.stampaBoard(alg.getRadice().getBoard());
		long start = System.currentTimeMillis();
		System.out.println(alg.valuta(lvl));
		long diff = System.currentTimeMillis()-start;
		System.out.println((double) diff/1000);
		MurusGallicus.stampaBoard(alg.getRadice().getBoard());
		
	}
	
	public static void testIAvsIADinamico(MinMaxAlg i1, int lvlIA1, MinMaxAlg i2, int lvlIA2) {
		MinMaxAlg ia1 =  i1;
		MinMaxAlg ia2 =  i2;
		ia1.inizializza("white");
		ia2.inizializza("black");
		MurusGallicus.stampaBoard(ia1.getRadice().getBoard());
		while(true) {
			
			String mossaIA1 = "";
			long start = System.currentTimeMillis();
			int numTorriTot = ia1.getNumeroTorri();
			System.out.println("NUMERO TORRI COMPLESSIVE:> "+numTorriTot);
			if(numTorriTot<8 && numTorriTot>4)
				mossaIA1 = ia1.valuta(lvlIA1+1);
			else if(numTorriTot<=4)
				mossaIA1 = ia1.valuta(lvlIA1+2);
			else
				mossaIA1 = ia1.valuta(lvlIA1);
			long diff = (System.currentTimeMillis() - start);
			System.out.print("MOSSA IA_1 :> "+mossaIA1+" ");
			System.out.println("("+(double) diff/1000+")");
			ia2.mossaAvversario(mossaIA1);
			MurusGallicus.stampaBoard(ia1.getRadice().getBoard());
			System.out.println("PIETRE IA1> "+ia1.getRadice().getPietreMie());
			System.out.println("PIETRE IA2> "+ia2.getRadice().getPietreMie());
			System.out.println("TORRI IA1> "+ia1.getRadice().getTorriMie());
			System.out.println("TORRI IA2> "+ia2.getRadice().getTorriMie());
			if(ia1.getRadice().vittoria(true)==1) {
				System.out.println("IA_1 HA VINTO");
				break;
			}
			if(ia1.getRadice().vittoria(true)==-1) {
				System.out.println("IA_2 HA VINTO");
				break;
			}
			
			if(ia2.getRadice().vittoria(true)==1) {
				System.out.println("IA_2 HA VINTO");
				break;
			}
			if(ia2.getRadice().vittoria(true)==-1) {
				System.out.println("IA_1 HA VINTO");
				break;
			}
			
			String mossaIA2 = ia2.valuta(lvlIA2);
			System.out.println("MOSSA IA_2 :> "+mossaIA2);
			ia1.mossaAvversario(mossaIA2);
			MurusGallicus.stampaBoard(ia2.getRadice().getBoard());
			System.out.println("PIETRE IA1> "+ia1.getRadice().getPietreMie());
			System.out.println("PIETRE IA2> "+ia2.getRadice().getPietreMie());
			System.out.println("TORRI IA1> "+ia1.getRadice().getTorriMie());
			System.out.println("TORRI IA2> "+ia2.getRadice().getTorriMie());
			if(ia2.getRadice().vittoria(true)==1) {
				System.out.println("IA_2 HA VINTO");
				break;
			}
			if(ia2.getRadice().vittoria(true)==-1) {
				System.out.println("IA_1 HA VINTO");
				break;
			}
			if(ia1.getRadice().vittoria(true)==1) {
				System.out.println("IA_1 HA VINTO");
				break;
			}
			if(ia1.getRadice().vittoria(true)==-1) {
				System.out.println("IA_2 HA VINTO");
				break;
			}
		}
	}
	
//	public static void main(String[] args) {
//		Euristica e1 = new EuristicaTorri();
//		e1.setColore("white");
//		Euristica e2 = new EuristicaTorriCopy();
//		e2.setColore("black");
//		VersioneOneQueueMultiThreaded ia = new VersioneOneQueueMultiThreaded(e2);
//		//VersioneMinMaxIterOneQueue ia = new VersioneMinMaxIterOneQueue();
//		//VersioneMinMaxIterOneQueue ia2 = new VersioneMinMaxIterOneQueue(e1);
//		VersioneOneQueueMultiThreaded ia2 = new VersioneOneQueueMultiThreaded(e1);
//		//System.out.println("------------------------VERSIONE UNA CODA-------------------------");
//		//TestInput.testMinMaxIter(ia,4);
//		//System.out.println("------------------------VERSIONE UNA CODA 2--------------------------");
//		//TestInput.testMinMaxIter(ia2,6);
//		//TestInput.testGiocatoreIA("white", 4);
//		//TestInput.testIAvsIA(ia,4,ia2,4);
//		TestInput.testIAvsIADinamico(ia,4,ia2,4);
//	}
	

}
