package core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import euristica.EuristicaTorri;
import versioni.VersioneOneQueueMultiThreaded;

public class Giocatore {

	private int port;
	private String host;
	private static final int LVLMAX = 4;
	private static final int LOWERBOUND = 4;
	private static final int MIDBOUND = 5;
	private static final int UPPERBOUND = 8;

	private Euristica euristica;
	private MinMaxAlg alg;
	
	//---------COSTRUTTORE---------------
	
	public Giocatore() {
		this.port = 8901;
		this.host = "localhost";
	}
	
	public Giocatore(String host, int port) {
		
		this.port = port;
		this.host = host;
	}
	
	
	private void gioca() {
		euristica = new EuristicaTorri();
		alg = new VersioneOneQueueMultiThreaded(euristica);
		try {
			Socket s = new Socket(host, port);
			PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String line = br.readLine().trim();
			String colore = line.split(" ")[1].toLowerCase();
			alg.inizializza(colore);
			euristica.setColore(colore);
			while(true) {
				line = br.readLine().trim();
				
				if(line.equals("ILLEGAL_MOVE") || line.equals("TIMEOUT") || line.equals("VICTORY") || line.equals("TIE") || line.equals("DEFEAT")) 
					break;
				
				if(line.equals("YOUR_TURN")){
					String mossa = "MOVE ";
					int numTorriTot = alg.getNumeroTorri();
					
					if(numTorriTot <= UPPERBOUND && numTorriTot > MIDBOUND) { // Ci entriamo con 10, 9, 8, 7  TORRI
						mossa += alg.valuta(LVLMAX+1);
					}
					else if(numTorriTot <= MIDBOUND && numTorriTot >= LOWERBOUND) { // Ci entriamo con 5 e 4 TORRI
						mossa += alg.valuta(LVLMAX+2);
					}
					else if(numTorriTot < LOWERBOUND) { //Ci entriamo con 3, 2, 1 TORRI
						mossa += alg.valuta(LVLMAX+3);
					}
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
		Giocatore giocatore;
		if(args.length>0) 
			giocatore = new Giocatore(args[0], Integer.parseInt(args[1]));
		
		else
			giocatore = new Giocatore();
			
		giocatore.gioca();
	}
}
