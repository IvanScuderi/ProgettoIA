package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//Classe che permette di giocare contro IA da tastiera
//CLASSE IMPIEGATA PER EFFETTUARE TEST
public class GiocatoreManuale {

	private int port;
	private String host;


	//---------COSTRUTTORE---------------
	
	public GiocatoreManuale() {
		this.port = 8901;
		this.host = "localhost";
	}
	
	public GiocatoreManuale(String host, int port) {
		
		this.port = port;
		this.host = host;
	}
	
	private void gioca() {
		try {
			Scanner sc = new Scanner(System.in);
			Socket s = new Socket(host, port);
			PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println("COLORE> " + br.readLine());
			while(true) {
				String line = br.readLine().trim();
				
				if(line.equals("VICTORY")) {
					System.out.println("HAI VINTO");
					break;
				}
				
				if(line.equals("TIE")) {
					System.out.println("PAREGGIO");
					break;
				}
				
				if(line.equals("DEFEAT")) {
					System.out.println("HAI PERSO");
					break;
				}
				
				if(line.equals("VALID_MOVE")) 
					System.out.println("MOSSA VALIDA");
				
				if(line.equals("YOUR_TURN")) { 
					System.out.print("MOSSA> ");
					String mossa = "MOVE "+sc.nextLine().trim();
					pw.println(mossa);
				}
				
				String msg = line.split(" ")[0].trim();
				if(msg.equals("MESSAGE")||msg.equals("OPPONENT_MOVE"))
					System.out.println(line);
				
			}
			pw.close();
			sc.close();
			br.close();
			s.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		//0 IP - 1 Port
		GiocatoreManuale giocatore;
		if(args.length>0) 
			giocatore = new GiocatoreManuale(args[0], Integer.parseInt(args[1]));
		
		else
			giocatore = new GiocatoreManuale();
			
		giocatore.gioca();
	}

	
}
