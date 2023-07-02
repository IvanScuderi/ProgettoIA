package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import core.Euristica;
import core.MinMaxAlg;
//import euristica.EuristicaPond;
import euristica.EuristicaTorriCopy;
import versioni.VersioneOneQueueMultiThreaded;

//Classe che ci serve per poter far giocare tra loro 2 istanze dello
//stesso giocatore ma con due euristiche differenti
//CLASSE IMPIEGATA PER EFFETTUARE TEST
public class GiocatoreAutomatico {
	
	private Euristica euristica;
	private int port;
	private String host;
	private static final int LVLMAX = 4;
	private static final int LOWERBOUND = 4;
	private static final int UPPERBOUND = 8;


	//---------COSTRUTTORE---------------
	
	public GiocatoreAutomatico() {
		this.port = 8901;
		this.host = "localhost";
		this.euristica = new EuristicaTorriCopy();
		//this.euristica = new EuristicaPond();
	}
	
	public GiocatoreAutomatico(Euristica e) {
		this.port = 8901;
		this.host = "localhost";
		this.euristica = e;
	}
	
	public GiocatoreAutomatico(String host, int port, Euristica e) {
		
		this.euristica = e;
		this.port = port;
		this.host = host;
	}
	
	
	private void gioca() {
		MinMaxAlg alg = new VersioneOneQueueMultiThreaded(this.euristica);
		try {
			Socket s = new Socket(host, port);
			PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String line = br.readLine().trim();
			String colore = line.split(" ")[1].toLowerCase();
			alg.inizializza(colore);
			while(true) {
				line = br.readLine().trim();
				
				if(line.equals("ILLEGAL_MOVE") || line.equals("TIMEOUT") || line.equals("VICTORY") || line.equals("TIE") || line.equals("DEFEAT")) 
					break;
				
				if(line.equals("YOUR_TURN")){
					String mossa = "MOVE ";
					int numTorriTot = alg.getNumeroTorri();
					
					if(numTorriTot < UPPERBOUND && numTorriTot > LOWERBOUND)
						mossa += alg.valuta(LVLMAX+1);
					
					else if(numTorriTot <= LOWERBOUND)
						mossa += alg.valuta(LVLMAX+2);
					
					else
						mossa += alg.valuta(LVLMAX);	
					
					pw.println(mossa);
				}
				String[] msg = line.split(" ");
				
				if(msg[0].equals("MESSAGE"))
					continue;
				
				if(msg[0].equals("OPPONENT_MOVE")) 
					alg.mossaAvversario(msg[1]);
				
			}
			br.close();
			pw.close();
			s.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//0 IP - 1 Port
		GiocatoreAutomatico giocatore;
		Euristica e = new EuristicaTorriCopy();
		if(args.length>0) 
			giocatore = new GiocatoreAutomatico(args[0], Integer.parseInt(args[1]),e);
		
		else
			giocatore = new GiocatoreAutomatico(e);
			
		giocatore.gioca();
	}
}
