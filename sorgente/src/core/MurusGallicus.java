package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MurusGallicus {

	/*
	 * MASSIMIZZATORE -> UPPERCASE;
	 * MINIMIZZATORE -> LOWERCASE;
	 * 2 ArrayList per le posizioni di torri mie e avversarie;
	 * board -> array char 56 elem;
	 * posizione torri -> indice classico all'interno dell'array
	 */
									
	private char[] board; 
	
	private char coloreMio,coloreAvversario;//1 bianco 0 nero
	

	private List<Integer> torriMie = new ArrayList<>();
	private List<Integer> torriAvversario = new ArrayList<>();
	
	//ALL'INIZIO NON HO PIETRE QUINDI NON DEVO INIZIALIZZARE TALI LISTE
	private List<Integer> pietreMie = new ArrayList<>();
	private List<Integer> pietreAvversario = new ArrayList<>();
	
	//---------------- COSTRUTTORE --------------------------------
	
	public MurusGallicus(String colore) {
		if(colore.equals("white")) {
			this.coloreMio='B';
			this.coloreAvversario='N';
								  // 1   2   3   4   5   6   7   8
			this.board= new char[] {'T','T','T','T','T','T','T','T', //G
					   				'o','o','o','o','o','o','o','o', //F
					   				'o','o','o','o','o','o','o','o', //E
					   				'o','o','o','o','o','o','o','o', //D
					   				'o','o','o','o','o','o','o','o', //C
					   				'o','o','o','o','o','o','o','o', //B
					   				't','t','t','t','t','t','t','t'};//A
		}
		else {
			this.coloreMio='N';	
			this.coloreAvversario='B';
								  // 1   2   3   4   5   6   7   8
			this.board= new char[] {'t','t','t','t','t','t','t','t', //G
					   				'o','o','o','o','o','o','o','o', //F
					   				'o','o','o','o','o','o','o','o', //E
					   				'o','o','o','o','o','o','o','o', //D
					   				'o','o','o','o','o','o','o','o', //C
					   				'o','o','o','o','o','o','o','o', //B
					   				'T','T','T','T','T','T','T','T'};//A
		}
		this.popolaListe(this.board);
	}
	
	public MurusGallicus(MurusGallicus m) {
		this.board = Arrays.copyOf(m.board,m.board.length);
		this.coloreAvversario = m.coloreAvversario;
		this.coloreMio = m.coloreMio;
		this.torriMie = new ArrayList<>();
		this.torriAvversario = new ArrayList<>();
		for(Integer torre : m.torriMie)
			this.torriMie.add(torre);
		for(Integer torre : m.torriAvversario)
			this.torriAvversario.add(torre);
		
		for(Integer pietra : m.pietreMie)
			this.pietreMie.add(pietra);
		for(Integer pietra : m.pietreAvversario)
			this.pietreAvversario.add(pietra);
	}
	
	//---------------- GETTERS AND SETTERS -------------------------------------
	
	
	public char[] getBoard() {
		return board;
	}

	public void setBoard(char[] board) {
		this.board = board;
	}

	public List<Integer> getTorriMie() {
		return torriMie;
	}

	public void setTorriMie(List<Integer> torriMie) {
		this.torriMie = torriMie;
	}

	public List<Integer> getTorriAvversario() {
		return torriAvversario;
	}

	public void setTorriAvversario(List<Integer> torriAvversario) {
		this.torriAvversario = torriAvversario;
	}
	
	public List<Integer> getPietreMie() {
		return pietreMie;
	}

	public void setPietreMie(List<Integer> pietreMie) {
		this.pietreMie = pietreMie;
	}

	public List<Integer> getPietreAvversario() {
		return pietreAvversario;
	}

	public void setPietreAvversario(List<Integer> pietreAvversario) {
		this.pietreAvversario = pietreAvversario;
	}

	public char getColoreMio() {
		return coloreMio;
	}

	public void setColoreMio(char coloreMio) {
		this.coloreMio = coloreMio;
	}

	public char getColoreAvversario() {
		return coloreAvversario;
	}

	public void setColoreAvversario(char coloreAvversario) {
		this.coloreAvversario = coloreAvversario;
	}
	
	public int getNumeroTorri() {
		return torriMie.size()+torriAvversario.size();
	}
	
	//---------------------- METODI DINAMICI ---------------------------

	public String toString() {
		return Arrays.toString(this.board);
	}
	
	//Questo metodo viene impiegato per modificare le scacchiere generate durante la generazione dell'albero
	public void effettuaMossa(char[] board, boolean massimizzatore, int src, int dest) {
		
		int diff = dest - src;
		int absoluteDiff = Math.abs(diff);
		Integer valueSrc = Integer.valueOf(src);
		if(massimizzatore) {
			torriMie.remove(valueSrc);
			if(absoluteDiff<=10 && absoluteDiff!=2) {
				//mi muovo di una posizione -> MANGIO
				board[src] = 'P';
				board[dest] = 'o';
				pietreMie.add(src);
				return;
			}
			else {
				//mi muovo di due posizioni -> SPOSTAMENTO TORRE
				int destIntermedia = diff/2 + src;
				board[src] = 'o';
				//controllo le possibili modifiche delle celle su cui effettuo movimenti
				if(board[destIntermedia]=='o' && board[dest]=='o') {//ENTRAMBE O
					board[destIntermedia] = 'P';
					board[dest] = 'P';
					pietreMie.add(destIntermedia);
					pietreMie.add(dest);
					return;
				}
				if(board[destIntermedia]=='P' && board[dest]=='o') {//UNA P ED UNA O
					board[destIntermedia] = 'T';
					torriMie.add(destIntermedia);
					board[dest] = 'P';
					pietreMie.add(dest);
					pietreMie.remove(Integer.valueOf(destIntermedia));
					return;
				}
				if(board[destIntermedia]=='o' && board[dest]=='P') {//UNA O ED UNA P
					board[destIntermedia] = 'P';
					board[dest] = 'T';
					torriMie.add(dest);
					pietreMie.remove(Integer.valueOf(dest));
					pietreMie.add(destIntermedia);
					return;
				}
				if(board[destIntermedia]=='P' && board[dest]=='P') {//ENTRAMBE P
					board[destIntermedia] = 'T';
					torriMie.add(destIntermedia);
					board[dest] = 'T';
					torriMie.add(dest);
					pietreMie.remove(Integer.valueOf(dest));
					pietreMie.remove(Integer.valueOf(destIntermedia));
					return;
				}
			}
		}
		else {
			torriAvversario.remove(valueSrc);
			if(absoluteDiff<=10 && absoluteDiff!=2) {
				//mi muovo di una posizione -> MANGIO
				board[src] = 'p';
				board[dest] = 'o';
				pietreAvversario.add(src);
				return;
			}
			else {
				//mi muovo di due posizioni -> SPOSTAMENTO TORRE
				int destIntermedia =  src + diff/2;
				board[src] = 'o';
				//controllo le possibili modifiche delle celle su cui effettuo movimenti
				if(board[destIntermedia]=='o' && board[dest]=='o') {//ENTRAMBE O
					board[destIntermedia] = 'p';
					board[dest] = 'p';
					pietreAvversario.add(destIntermedia);
					pietreAvversario.add(dest);
					return;
				}
				if(board[destIntermedia]=='p' && board[dest]=='o') {//UNA p ED UNA O
					board[destIntermedia] = 't';
					torriAvversario.add(destIntermedia);
					board[dest] = 'p';
					pietreAvversario.add(dest);
					pietreAvversario.remove(Integer.valueOf(destIntermedia));
					return;
				}
				if(board[destIntermedia]=='o' && board[dest]=='p') {//UNA O ED UNA p
					board[destIntermedia] = 'p';
					board[dest] = 't';
					torriAvversario.add(dest);
					pietreAvversario.add(destIntermedia);
					pietreAvversario.remove(Integer.valueOf(dest));
					return;
				}
				if(board[destIntermedia]=='p' && board[dest]=='p') {//ENTRAMBE p
					board[destIntermedia] = 't';
					torriAvversario.add(destIntermedia);
					board[dest] = 't';
					torriAvversario.add(dest);
					pietreAvversario.remove(Integer.valueOf(destIntermedia));
					pietreAvversario.remove(Integer.valueOf(dest));
					return;
				}
				
			}
			
		}
		
	}

	private void popolaListe(char[]board) {
		for(int i=0;i<56;i++) {
			if(board[i]=='T')
				torriMie.add(i);
			if(board[i]=='t')
				torriAvversario.add(i);
		}
	}
	
	public void effettuaMossaAvversaria(String move) {
		
		String righe = "GFEDCBA";
		
		String start = move.split(",")[0];
		char lettera = start.charAt(0);
		int col = Integer.parseInt(""+start.charAt(1))-1;
		int riga = righe.indexOf(lettera);
		int src = riga*8 +col;
		int dest = src;
		String spostamento = move.split(",")[1];
		if(spostamento.equals("N")) {//posso sommare 8 o 16
			if(board[src+8]=='P') {//l'avversario sta mangiando
				dest+=8;
				effettuaMossa(board,false,src,dest);
				return;
			}
			else {//l'avversario si sposta di 2 caselle
				dest+=16;
				effettuaMossa(board,false,src,dest);
				return;
			}
		}
		if(spostamento.equals("S")) {//posso sottrarre 8 o 16
			if(board[src-8]=='P') {//l'avversario sta mangiando
				dest-=8;
				effettuaMossa(board,false,src,dest);
				return;
			}
			else {//l'avversario si sposta di 2 caselle
				dest-=16;
				effettuaMossa(board,false,src,dest);
				return;
			}
		}
		if(spostamento.equals("E")) {//posso sommare 1 o 2
			if(board[src+1]=='P') {//l'avversario sta mangiando
				dest+=1;
				effettuaMossa(board,false,src,dest);
				return;
			}
			else {//l'avversario si sposta di 2 caselle
				dest+=2;
				effettuaMossa(board,false,src,dest);
				return;
			}
		}
		if(spostamento.equals("W")) {//posso sottrarre 1 o 2
			if(board[src-1]=='P') {//l'avversario sta mangiando
				dest-=1;
				effettuaMossa(board,false,src,dest);
				return;
			}
			else {//l'avversario si sposta di 2 caselle
				dest-=2;
				effettuaMossa(board,false,src,dest);
				return;
			}
		}
		if(spostamento.equals("NE")) {//posso sommare 9 o 18
			if(board[src+9]=='P') {//l'avversario sta mangiando
				dest+=9;
				effettuaMossa(board,false,src,dest);
				return;
			}
			else {//l'avversario si sposta di 2 caselle
				dest+=18;
				effettuaMossa(board,false,src,dest);
				return;
			}
		}
		if(spostamento.equals("NW")) {//posso sommare 7 o 14
			if(board[src+7]=='P') {//l'avversario sta mangiando
				dest+=7;
				effettuaMossa(board,false,src,dest);
				return;
			}
			else {//l'avversario si sposta di 2 caselle
				dest+=14;
				effettuaMossa(board,false,src,dest);
				return;
			}
		}
		if(spostamento.equals("SE")) {//posso sottrarre 7 o 14
			if(board[src-7]=='P') {//l'avversario sta mangiando
				dest-=7;
				effettuaMossa(board,false,src,dest);
				return;
			}
			else {//l'avversario si sposta di 2 caselle
				dest-=14;
				effettuaMossa(board,false,src,dest);
				return;
			}
		}
		if(spostamento.equals("SW")) {//posso sottrarre 9 o 18
			if(board[src-9]=='P') {//l'avversario sta mangiando
				dest-=9;
				effettuaMossa(board,false,src,dest);
				return;
			}
			else {//l'avversario si sposta di 2 caselle
				dest-=18;
				effettuaMossa(board,false,src,dest);
				return;
			}
		}
	
	}
	
	public int vittoria(boolean massimizzatore) {
		
		if(massimizzatore) {
			//massimizzatore vince -> ritorno 1
			//bianco vince se raggiunge celle iniziali nere o se il nero non ha torri
			if(coloreMio == 'B') {
				if(board[48]=='P'|| board[49]=='P'|| board[50]=='P'|| board[51]=='P'|| board[52]=='P'||
						board[53]=='P'|| board[54]=='P'|| board[55]=='P')
					return 1;
				if(board[0]=='p'|| board[1]=='p'|| board[2]=='p'|| board[3]=='p'|| board[4]=='p'||
						board[5]=='p'|| board[6]=='p'|| board[7]=='p')
					return -1;
				
				if(torriAvversario.size()==0)
					return 1;
				if(torriMie.size()==0)
					return -1;
			}
			//nero vince se raggiunge celle iniziali bianche o se il bianco non ha torri
			if(coloreMio == 'N') {
				if(board[0]=='P'|| board[1]=='P'|| board[2]=='P'|| board[3]=='P'|| board[4]=='P'||
						board[5]=='P'|| board[6]=='P'|| board[7]=='P')
					return 1;
				if(board[48]=='p'|| board[49]=='p'|| board[50]=='p'|| board[51]=='p'|| board[52]=='p'||
						board[53]=='p'|| board[54]=='p'|| board[55]=='p')
					return -1;
				
				if(torriAvversario.size()==0)
					return 1;
				if(torriMie.size()==0)
					return -1;
			}
		}
		else {
			//minimizzatore vince -> ritorno -1
			//bianco vince se raggiunge celle iniziali nere o se il nero non ha torri
			if(coloreAvversario == 'B') {
				if(board[48]=='p'|| board[49]=='p'|| board[50]=='p'|| board[51]=='p'|| board[52]=='p'||
						board[53]=='p'|| board[54]=='p'|| board[55]=='p')
					return -1;
				if(board[0]=='P'|| board[1]=='P'|| board[2]=='P'|| board[3]=='P'|| board[4]=='P'||
						board[5]=='P'|| board[6]=='P'|| board[7]=='P')
					return 1;
				
				if(torriMie.size()==0)
					return -1;
				if(torriAvversario.size()==0)
					return 1;
			}
			//nero vince se raggiunge celle iniziali bianche o se il bianco non ha torri
			if(coloreAvversario == 'N') {
				if(board[0]=='p'|| board[1]=='p'|| board[2]=='p'|| board[3]=='p'|| board[4]=='p'||
						board[5]=='p'|| board[6]=='p'|| board[7]=='p')
					return -1;
				if(board[48]=='P'|| board[49]=='P'|| board[50]=='P'|| board[51]=='P'|| board[52]=='P'||
						board[53]=='P'|| board[54]=='P'|| board[55]=='P')
					return 1;
				
				if(torriMie.size()==0)
					return -1;
				if(torriAvversario.size()==0)
					return 1;
			}
		}
		return 0;
	}

	
	//--------------------- METODI STATICI -------------------------------------
	
	
	public static List<Integer> calcolaMosse (char[] board, int torre, boolean massimizzatore){

		int rigaTorre = torre / 8;
		int colTorre = torre % 8;
		
		ArrayList<Integer> ris = new ArrayList<>(32);
		
		if(massimizzatore) {
			/*
			 * Caselle bloccate LOWER-CASE
			 */
			if(rigaTorre>=2 && rigaTorre<=4 && colTorre>=2 && colTorre<=5) {
				//tutti i movimenti disponibili 
				
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='P' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='P')||(board[torre+8]=='P' && board[torre+16]=='P')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='p') ris.add(torre+8);
				//NE spostamento
				if((board[torre+9]=='o' && board[torre+18]=='o')||(board[torre+9]=='P' && board[torre+18]=='o')||(board[torre+9]=='o' && board[torre+18]=='P')||(board[torre+9]=='P' && board[torre+18]=='P')) ris.add(torre+18);
				//NE mangia
				if(board[torre+9]=='p') ris.add(torre+9);
				//NW spostamento
				if((board[torre+7]=='o' && board[torre+14]=='o')||(board[torre+7]=='P' && board[torre+14]=='o')||(board[torre+7]=='o' && board[torre+14]=='P')||(board[torre+7]=='P' && board[torre+14]=='P')) ris.add(torre+14);
				//NW mangia
				if(board[torre+7]=='p') ris.add(torre+7);
				
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='P' && board[torre-16]=='o')||(board[torre-8]=='o' && board[torre-16]=='P')||(board[torre-8]=='P' && board[torre-16]=='P')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='p') ris.add(torre-8);
				//SE spostamento
				if((board[torre-7]=='o' && board[torre-14]=='o')||(board[torre-7]=='P' && board[torre-14]=='o')||(board[torre-7]=='o' && board[torre-14]=='P')||(board[torre-7]=='P' && board[torre-14]=='P')) ris.add(torre-14);
				//SE mangia
				if(board[torre-7]=='p') ris.add(torre-7);
				//SW spostamento
				if((board[torre-9]=='o' && board[torre-18]=='o')||(board[torre-9]=='P' && board[torre-18]=='o')||(board[torre-9]=='o' && board[torre-18]=='P')||(board[torre-9]=='P' && board[torre-18]=='P')) ris.add(torre-18);
				//SW mangia
				if(board[torre-9]=='p') ris.add(torre-9);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='P' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='P')||(board[torre+1]=='P' && board[torre+2]=='P')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='p') ris.add(torre+1);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='P' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='P')||(board[torre-1]=='P' && board[torre-2]=='P')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='p') ris.add(torre-1);
				
				return ris;
			}
			
			if(rigaTorre < 2 && colTorre < 2) {//N,E,NE 
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='P' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='P')||(board[torre+8]=='P' && board[torre+16]=='P')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='p') ris.add(torre+8);
				//NE spostamento
				if((board[torre+9]=='o' && board[torre+18]=='o')||(board[torre+9]=='P' && board[torre+18]=='o')||(board[torre+9]=='o' && board[torre+18]=='P')||(board[torre+9]=='P' && board[torre+18]=='P')) ris.add(torre+18);
				//NE mangia
				if(board[torre+9]=='p') ris.add(torre+9);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='P' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='P')||(board[torre+1]=='P' && board[torre+2]=='P')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='p') ris.add(torre+1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==1) {
					//NW mangia
					if(board[torre+7]=='p') ris.add(torre+7);
					//W mangia
					if(board[torre-1]=='p') ris.add(torre-1);
					
				}
				if(torre==8) {
					//S mangia
					if(board[torre-8]=='p') ris.add(torre-8);
					//SE mangia
					if(board[torre-7]=='p') ris.add(torre-7);
					
				}
				if(torre==9) {
					//S mangia
					if(board[torre-8]=='p') ris.add(torre-8);
					//SE mangia
					if(board[torre-7]=='p') ris.add(torre-7);
					//SW mangia
					if(board[torre-9]=='p') ris.add(torre-9);
					//W mangia
					if(board[torre-1]=='p') ris.add(torre-1);
					//NW mangia
					if(board[torre+7]=='p') ris.add(torre+7);
					
				}
				return ris;
			}
				
			if(rigaTorre <= 4 && rigaTorre >=2 && colTorre < 2) {//S,SE,N,E,NE
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='P' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='P')||(board[torre+8]=='P' && board[torre+16]=='P')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='p') ris.add(torre+8);
				//NE spostamento
				if((board[torre+9]=='o' && board[torre+18]=='o')||(board[torre+9]=='P' && board[torre+18]=='o')||(board[torre+9]=='o' && board[torre+18]=='P')||(board[torre+9]=='P' && board[torre+18]=='P')) ris.add(torre+18);
				//NE mangia
				if(board[torre+9]=='p') ris.add(torre+9);
				
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='P' && board[torre-16]=='o')||(board[torre-8]=='o' && board[torre-16]=='P')||(board[torre-8]=='P' && board[torre-16]=='P')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='p') ris.add(torre-8);
				//SE spostamento
				if((board[torre-7]=='o' && board[torre-14]=='o')||(board[torre-7]=='P' && board[torre-14]=='o')||(board[torre-7]=='o' && board[torre-14]=='P')||(board[torre-7]=='P' && board[torre-14]=='P')) ris.add(torre-14);
				//SE mangia
				if(board[torre-7]=='p') ris.add(torre-7);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='P' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='P')||(board[torre+1]=='P' && board[torre+2]=='P')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='p') ris.add(torre+1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==33 || torre==25 || torre==17) {
					//NW mangia
					if(board[torre+7]=='p') ris.add(torre+7);
					//W mangia
					if(board[torre-1]=='p') ris.add(torre-1);
					//SW mangia
					if(board[torre-9]=='p') ris.add(torre-9);
					
				}
				
				return ris;
			}
				
			if(rigaTorre > 4 && colTorre < 2) {//S,E,SE
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='P' && board[torre-16]=='o')||(board[torre-8]=='o' && board[torre-16]=='P')||(board[torre-8]=='P' && board[torre-16]=='P')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='p') ris.add(torre-8);
				//SE spostamento
				if((board[torre-7]=='o' && board[torre-14]=='o')||(board[torre-7]=='P' && board[torre-14]=='o')||(board[torre-7]=='o' && board[torre-14]=='P')||(board[torre-7]=='P' && board[torre-14]=='P')) ris.add(torre-14);
				//SE mangia
				if(board[torre-7]=='p') ris.add(torre-7);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='P' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='P')||(board[torre+1]=='P' && board[torre+2]=='P')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='p') ris.add(torre+1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==49) {
					//W mangia
					if(board[torre-1]=='p') ris.add(torre-1);
					//SW mangia
					if(board[torre-9]=='p') ris.add(torre-9);
				}
				if(torre==40) {
					//N mangia
					if(board[torre+8]=='p') ris.add(torre+8);
					//NE mangia
					if(board[torre+9]=='p') ris.add(torre+9);
					
				}
				if(torre==41) {
					//NE mangia
					if(board[torre+9]=='p') ris.add(torre+9);
					//N mangia
					if(board[torre+8]=='p') ris.add(torre+8);
					//NW mangia
					if(board[torre+7]=='p') ris.add(torre+7);
					//W mangia
					if(board[torre-1]=='p') ris.add(torre-1);
					//SW mangia
					if(board[torre-9]=='p') ris.add(torre-9);
					
				}
				
				return ris;
			}
				
			if(rigaTorre < 2 && colTorre >= 2 && colTorre <= 5) {//N,E,W,NW,NE
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='P' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='P')||(board[torre+8]=='P' && board[torre+16]=='P')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='p') ris.add(torre+8);
				//NE spostamento
				if((board[torre+9]=='o' && board[torre+18]=='o')||(board[torre+9]=='P' && board[torre+18]=='o')||(board[torre+9]=='o' && board[torre+18]=='P')||(board[torre+9]=='P' && board[torre+18]=='P')) ris.add(torre+18);
				//NE mangia
				if(board[torre+9]=='p') ris.add(torre+9);
				//NW spostamento
				if((board[torre+7]=='o' && board[torre+14]=='o')||(board[torre+7]=='P' && board[torre+14]=='o')||(board[torre+7]=='o' && board[torre+14]=='P')||(board[torre+7]=='P' && board[torre+14]=='P')) ris.add(torre+14);
				//NW mangia
				if(board[torre+7]=='p') ris.add(torre+7);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='P' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='P')||(board[torre+1]=='P' && board[torre+2]=='P')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='p') ris.add(torre+1);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='P' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='P')||(board[torre-1]=='P' && board[torre-2]=='P')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='p') ris.add(torre-1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if( torre>=10 && torre <=13) {
					//S mangia
					if(board[torre-8]=='p') ris.add(torre-8);
					//SE mangia
					if(board[torre-7]=='p') ris.add(torre-7);
					//SW mangia
					if(board[torre-9]=='p') ris.add(torre-9);
				}
				
				return ris;
			}
				
			if(rigaTorre < 2 && colTorre > 5) {//N,W,NW
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='P' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='P')||(board[torre+8]=='P' && board[torre+16]=='P')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='p') ris.add(torre+8);
				//NW spostamento
				if((board[torre+7]=='o' && board[torre+14]=='o')||(board[torre+7]=='P' && board[torre+14]=='o')||(board[torre+7]=='o' && board[torre+14]=='P')||(board[torre+7]=='P' && board[torre+14]=='P')) ris.add(torre+14);
				//NW mangia
				if(board[torre+7]=='p') ris.add(torre+7);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='P' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='P')||(board[torre-1]=='P' && board[torre-2]=='P')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='p') ris.add(torre-1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==6) {
					//NE mangia
					if(board[torre+9]=='p') ris.add(torre+9);
					//E mangia
					if(board[torre+1]=='p') ris.add(torre+1);
					
				}
				if(torre==15) {
					//S mangia
					if(board[torre-8]=='p') ris.add(torre-8);
					//SW mangia
					if(board[torre-9]=='p') ris.add(torre-9);
					
				}
				if(torre==14) {
					//S mangia
					if(board[torre-8]=='p') ris.add(torre-8);
					//SE mangia
					if(board[torre-7]=='p') ris.add(torre-7);
					//SW mangia
					if(board[torre-9]=='p') ris.add(torre-9);
					//NE mangia
					if(board[torre+9]=='p') ris.add(torre+9);
					//E mangia
					if(board[torre+1]=='p') ris.add(torre+1);
					
				}
				
				return ris;	
			}
				
			if(rigaTorre >= 2 && rigaTorre <= 4 && colTorre > 5) {//S,N,W,SW,NW
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='P' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='P')||(board[torre+8]=='P' && board[torre+16]=='P')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='p') ris.add(torre+8);
				//NW spostamento
				if((board[torre+7]=='o' && board[torre+14]=='o')||(board[torre+7]=='P' && board[torre+14]=='o')||(board[torre+7]=='o' && board[torre+14]=='P')||(board[torre+7]=='P' && board[torre+14]=='P')) ris.add(torre+14);
				//NW mangia
				if(board[torre+7]=='p') ris.add(torre+7);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='P' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='P')||(board[torre-1]=='P' && board[torre-2]=='P')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='p') ris.add(torre-1);
				
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='P' && board[torre-16]=='o')||(board[torre-8]=='o' && board[torre-16]=='P')||(board[torre-8]=='P' && board[torre-16]=='P')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='p') ris.add(torre-8);
				//SW spostamento
				if((board[torre-9]=='o' && board[torre-18]=='o')||(board[torre-9]=='P' && board[torre-18]=='o')||(board[torre-9]=='o' && board[torre-18]=='P')||(board[torre-9]=='P' && board[torre-18]=='P')) ris.add(torre-18);
				//SW mangia
				if(board[torre-9]=='p') ris.add(torre-9);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==38 || torre==30 || torre==22) {
					//NE mangia
					if(board[torre+9]=='p') ris.add(torre+9);
					//E mangia
					if(board[torre+1]=='p') ris.add(torre+1);
					//SE mangia
					if(board[torre-7]=='p') ris.add(torre-7);
				}
				
				return ris;
			}
				
			if(rigaTorre > 4 && colTorre > 5) {//S,W,SW
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='P' && board[torre-16]=='o')||(board[torre-8]=='o' && board[torre-16]=='P')||(board[torre-8]=='P' && board[torre-16]=='P')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='p') ris.add(torre-8);
				//SW spostamento
				if((board[torre-9]=='o' && board[torre-18]=='o')||(board[torre-9]=='P' && board[torre-18]=='o')||(board[torre-9]=='o' && board[torre-18]=='P')||(board[torre-9]=='P' && board[torre-18]=='P')) ris.add(torre-18);
				//SW mangia
				if(board[torre-9]=='p') ris.add(torre-9);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='P' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='P')||(board[torre-1]=='P' && board[torre-2]=='P')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='p') ris.add(torre-1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==54) {
					//SE mangia
					if(board[torre-7]=='p') ris.add(torre-7);
					//E mangia
					if(board[torre+1]=='p') ris.add(torre+1);
				}
				if(torre==47) {
					//N mangia
					if(board[torre+8]=='p') ris.add(torre+8);
					//NW mangia
					if(board[torre+7]=='p') ris.add(torre+7);
					
				}
				if(torre==46) {
					//SE mangia
					if(board[torre-7]=='p') ris.add(torre-7);
					//NE mangia
					if(board[torre+9]=='p') ris.add(torre+9);
					//N mangia
					if(board[torre+8]=='p') ris.add(torre+8);
					//NW mangia
					if(board[torre+7]=='p') ris.add(torre+7);
					//E mangia
					if(board[torre+1]=='p') ris.add(torre+1);
				}
				
				return ris;
			}
				
			if(rigaTorre > 4 && colTorre >= 2 && colTorre <= 5) {//S,E,W,SE,SW
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='P' && board[torre-16]=='o')||(board[torre-8]=='o' && board[torre-16]=='P')||(board[torre-8]=='P' && board[torre-16]=='P')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='p') ris.add(torre-8);
				//SE spostamento
				if((board[torre-7]=='o' && board[torre-14]=='o')||(board[torre-7]=='P' && board[torre-14]=='o')||(board[torre-7]=='o' && board[torre-14]=='P')||(board[torre-7]=='P' && board[torre-14]=='P')) ris.add(torre-14);
				//SE mangia
				if(board[torre-7]=='p') ris.add(torre-7);
				//SW spostamento
				if((board[torre-9]=='o' && board[torre-18]=='o')||(board[torre-9]=='P' && board[torre-18]=='o')||(board[torre-9]=='o' && board[torre-18]=='P')||(board[torre-9]=='P' && board[torre-18]=='P')) ris.add(torre-18);
				//SW mangia
				if(board[torre-9]=='p') ris.add(torre-9);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='P' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='P')||(board[torre+1]=='P' && board[torre+2]=='P')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='p') ris.add(torre+1);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='P' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='P')||(board[torre-1]=='P' && board[torre-2]=='P')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='p') ris.add(torre-1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre>=42 && torre<=45) {
					//NE mangia
					if(board[torre+9]=='p') ris.add(torre+9);
					//N mangia
					if(board[torre+8]=='p') ris.add(torre+8);
					//NW mangia
					if(board[torre+7]=='p') ris.add(torre+7);
				}
				
				return ris;
			}
	
		}
		else {
			/*
			 * Caselle bloccate UPPER-CASE
			 */
			if(rigaTorre>=2 && rigaTorre<=4 && colTorre>=2 && colTorre<=5) {
				//tutti i movimenti disponibili 
				
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='p' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='p')||(board[torre+8]=='p' && board[torre+16]=='p')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='P') ris.add(torre+8);
				//NE spostamento
				if((board[torre+9]=='o' && board[torre+18]=='o')||(board[torre+9]=='p' && board[torre+18]=='o')||(board[torre+9]=='o' && board[torre+18]=='p')||(board[torre+9]=='p' && board[torre+18]=='p')) ris.add(torre+18);
				//NE mangia
				if(board[torre+9]=='P') ris.add(torre+9);
				//NW spostamento
				if((board[torre+7]=='o' && board[torre+14]=='o')||(board[torre+7]=='p' && board[torre+14]=='o')||(board[torre+7]=='o' && board[torre+14]=='p')||(board[torre+7]=='p' && board[torre+14]=='p')) ris.add(torre+14);
				//NW mangia
				if(board[torre+7]=='P') ris.add(torre+7);
				
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='p' && board[torre-16]=='o')||(board[torre-8]=='o' && board[torre-16]=='p')||(board[torre-8]=='p' && board[torre-16]=='p')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='P') ris.add(torre-8);
				//SE spostamento
				if((board[torre-7]=='o' && board[torre-14]=='o')||(board[torre-7]=='p' && board[torre-14]=='o')||(board[torre-7]=='o' && board[torre-14]=='p')||(board[torre-7]=='p' && board[torre-14]=='p')) ris.add(torre-14);
				//SE mangia
				if(board[torre-7]=='P') ris.add(torre-7);
				//SW spostamento
				if((board[torre-9]=='o' && board[torre-18]=='o')||(board[torre-9]=='p' && board[torre-18]=='o')||(board[torre-9]=='o' && board[torre-18]=='p')||(board[torre-9]=='p' && board[torre-18]=='p')) ris.add(torre-18);
				//SW mangia
				if(board[torre-9]=='P') ris.add(torre-9);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='p' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='p')||(board[torre+1]=='p' && board[torre+2]=='p')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='P') ris.add(torre+1);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='p' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='p')||(board[torre-1]=='p' && board[torre-2]=='p')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='P') ris.add(torre-1);
				
				return ris;
			}
			
			
			if(rigaTorre < 2 && colTorre < 2) {//N,E,NE 
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='p' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='p')||(board[torre+8]=='p' && board[torre+16]=='p')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='P') ris.add(torre+8);
				//NE spostamento
				if((board[torre+9]=='o' && board[torre+18]=='o')||(board[torre+9]=='p' && board[torre+18]=='o')||(board[torre+9]=='o' && board[torre+18]=='p')||(board[torre+9]=='p' && board[torre+18]=='p')) ris.add(torre+18);
				//NE mangia
				if(board[torre+9]=='P') ris.add(torre+9);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='p' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='p')||(board[torre+1]=='p' && board[torre+2]=='p')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='P') ris.add(torre+1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==1) {
					//NW mangia
					if(board[torre+7]=='P') ris.add(torre+7);
					//W mangia
					if(board[torre-1]=='P') ris.add(torre-1);
					
				}
				if(torre==8) {
					//S mangia
					if(board[torre-8]=='P') ris.add(torre-8);
					//SE mangia
					if(board[torre-7]=='P') ris.add(torre-7);
					
				}
				if(torre==9) {
					//S mangia
					if(board[torre-8]=='P') ris.add(torre-8);
					//SE mangia
					if(board[torre-7]=='P') ris.add(torre-7);
					//SW mangia
					if(board[torre-9]=='P') ris.add(torre-9);
					//W mangia
					if(board[torre-1]=='P') ris.add(torre-1);
					//NW mangia
					if(board[torre+7]=='P') ris.add(torre+7);
					
				}
				
				return ris;
			}
				
			if(rigaTorre <= 4 && rigaTorre >=2 && colTorre < 2) {//S,SE,N,E,NE
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='p' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='p')||(board[torre+8]=='p' && board[torre+16]=='p')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='P') ris.add(torre+8);
				//NE spostamento
				if((board[torre+9]=='o' && board[torre+18]=='o')||(board[torre+9]=='p' && board[torre+18]=='o')||(board[torre+9]=='o' && board[torre+18]=='p')||(board[torre+9]=='p' && board[torre+18]=='p')) ris.add(torre+18);
				//NE mangia
				if(board[torre+9]=='P') ris.add(torre+9);
				
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='p' && board[torre-16]=='p')||(board[torre-8]=='o' && board[torre-16]=='p')||(board[torre-8]=='p' && board[torre-16]=='p')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='P') ris.add(torre-8);
				//SE spostamento
				if((board[torre-7]=='o' && board[torre-14]=='o')||(board[torre-7]=='p' && board[torre-14]=='o')||(board[torre-7]=='o' && board[torre-14]=='p')||(board[torre-7]=='p' && board[torre-14]=='p')) ris.add(torre-14);
				//SE mangia
				if(board[torre-7]=='P') ris.add(torre-7);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='p' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='p')||(board[torre+1]=='p' && board[torre+2]=='p')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='P') ris.add(torre+1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==33 || torre==25 || torre==17) {
					//NW mangia
					if(board[torre+7]=='P') ris.add(torre+7);
					//W mangia
					if(board[torre-1]=='P') ris.add(torre-1);
					//SW mangia
					if(board[torre-9]=='P') ris.add(torre-9);
					
				}
				
				return ris;
			}
				
			if(rigaTorre > 4 && colTorre < 2) {//S,E,SE
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='p' && board[torre-16]=='o')||(board[torre-8]=='o' && board[torre-16]=='p')||(board[torre-8]=='p' && board[torre-16]=='p')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='P') ris.add(torre-8);
				//SE spostamento
				if((board[torre-7]=='o' && board[torre-14]=='o')||(board[torre-7]=='p' && board[torre-14]=='o')||(board[torre-7]=='o' && board[torre-14]=='p')||(board[torre-7]=='p' && board[torre-14]=='p')) ris.add(torre-14);
				//SE mangia
				if(board[torre-7]=='P') ris.add(torre-7);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='p' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='p')||(board[torre+1]=='p' && board[torre+2]=='p')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='P') ris.add(torre+1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==49) {
					//W mangia
					if(board[torre-1]=='P') ris.add(torre-1);
					//SW mangia
					if(board[torre-9]=='P') ris.add(torre-9);
				}
				if(torre==40) {
					//N mangia
					if(board[torre+8]=='P') ris.add(torre+8);
					//NE mangia
					if(board[torre+9]=='P') ris.add(torre+9);
					
				}
				if(torre==41) {
					//NE mangia
					if(board[torre+9]=='P') ris.add(torre+9);
					//N mangia
					if(board[torre+8]=='P') ris.add(torre+8);
					//NW mangia
					if(board[torre+7]=='P') ris.add(torre+7);
					//W mangia
					if(board[torre-1]=='P') ris.add(torre-1);
					//SW mangia
					if(board[torre-9]=='P') ris.add(torre-9);
					
				}
				
				return ris;
			}
				
			if(rigaTorre < 2 && colTorre >= 2 && colTorre <= 5) {//N,E,W,NW,NE
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='p' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='p')||(board[torre+8]=='p' && board[torre+16]=='p')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='P') ris.add(torre+8);
				//NE spostamento
				if((board[torre+9]=='o' && board[torre+18]=='o')||(board[torre+9]=='p' && board[torre+18]=='o')||(board[torre+9]=='o' && board[torre+18]=='p')||(board[torre+9]=='p' && board[torre+18]=='p')) ris.add(torre+18);
				//NE mangia
				if(board[torre+9]=='P') ris.add(torre+9);
				//NW spostamento
				if((board[torre+7]=='o' && board[torre+14]=='o')||(board[torre+7]=='p' && board[torre+14]=='o')||(board[torre+7]=='o' && board[torre+14]=='p')||(board[torre+7]=='p' && board[torre+14]=='p')) ris.add(torre+14);
				//NW mangia
				if(board[torre+7]=='P') ris.add(torre+7);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='p' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='p')||(board[torre+1]=='p' && board[torre+2]=='p')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='P') ris.add(torre+1);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='p' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='p')||(board[torre-1]=='p' && board[torre-2]=='p')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='P') ris.add(torre-1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if( torre>=10 && torre <=13) {
					//S mangia
					if(board[torre-8]=='P') ris.add(torre-8);
					//SE mangia
					if(board[torre-7]=='P') ris.add(torre-7);
					//SW mangia
					if(board[torre-9]=='P') ris.add(torre-9);
				}
				
				return ris;
			}
				
			if(rigaTorre < 2 && colTorre > 5) {//N,W,NW
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='p' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='p')||(board[torre+8]=='p' && board[torre+16]=='p')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='P') ris.add(torre+8);
				//NW spostamento
				if((board[torre+7]=='o' && board[torre+14]=='o')||(board[torre+7]=='p' && board[torre+14]=='o')||(board[torre+7]=='o' && board[torre+14]=='p')||(board[torre+7]=='p' && board[torre+14]=='p')) ris.add(torre+14);
				//NW mangia
				if(board[torre+7]=='P') ris.add(torre+7);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='p' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='p')||(board[torre-1]=='p' && board[torre-2]=='p')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='P') ris.add(torre-1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==6) {
					//NE mangia
					if(board[torre+9]=='P') ris.add(torre+9);
					//E mangia
					if(board[torre+1]=='P') ris.add(torre+1);
					
				}
				if(torre==15) {
					//S mangia
					if(board[torre-8]=='P') ris.add(torre-8);
					//SW mangia
					if(board[torre-9]=='P') ris.add(torre-9);
					
				}
				if(torre==14) {
					//S mangia
					if(board[torre-8]=='P') ris.add(torre-8);
					//SE mangia
					if(board[torre-7]=='P') ris.add(torre-7);
					//SW mangia
					if(board[torre-9]=='P') ris.add(torre-9);
					//NE mangia
					if(board[torre+9]=='P') ris.add(torre+9);
					//E mangia
					if(board[torre+1]=='P') ris.add(torre+1);
					
				}
				return ris;	
			}
				
			if(rigaTorre >= 2 && rigaTorre <= 4 && colTorre > 5) {//S,N,W,SW,NW
				//NORD
				//N spostamento
				if((board[torre+8]=='o' && board[torre+16]=='o')||(board[torre+8]=='p' && board[torre+16]=='o')||(board[torre+8]=='o' && board[torre+16]=='p')||(board[torre+8]=='p' && board[torre+16]=='p')) ris.add(torre+16);
				//N mangia
				if(board[torre+8]=='P') ris.add(torre+8);
				//NW spostamento
				if((board[torre+7]=='o' && board[torre+14]=='o')||(board[torre+7]=='p' && board[torre+14]=='o')||(board[torre+7]=='o' && board[torre+14]=='p')||(board[torre+7]=='p' && board[torre+14]=='p')) ris.add(torre+14);
				//NW mangia
				if(board[torre+7]=='P') ris.add(torre+7);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='p' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='p')||(board[torre-1]=='p' && board[torre-2]=='p')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='P') ris.add(torre-1);
				
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='p' && board[torre-16]=='o')||(board[torre-8]=='o' && board[torre-16]=='p')||(board[torre-8]=='p' && board[torre-16]=='p')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='P') ris.add(torre-8);
				//SW spostamento
				if((board[torre-9]=='o' && board[torre-18]=='o')||(board[torre-9]=='p' && board[torre-18]=='o')||(board[torre-9]=='o' && board[torre-18]=='p')||(board[torre-9]=='p' && board[torre-18]=='p')) ris.add(torre-18);
				//SW mangia
				if(board[torre-9]=='P') ris.add(torre-9);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==38 || torre==30 || torre==22) {
					//NE mangia
					if(board[torre+9]=='P') ris.add(torre+9);
					//E mangia
					if(board[torre+1]=='P') ris.add(torre+1);
					//SE mangia
					if(board[torre-7]=='P') ris.add(torre-7);
				}
				
				return ris;
			}
				
			if(rigaTorre > 4 && colTorre > 5) {//S,W,SW
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='p' && board[torre-16]=='o')||(board[torre-8]=='o' && board[torre-16]=='p')||(board[torre-8]=='p' && board[torre-16]=='p')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='P') ris.add(torre-8);
				//SW spostamento
				if((board[torre-9]=='o' && board[torre-18]=='o')||(board[torre-9]=='p' && board[torre-18]=='o')||(board[torre-9]=='o' && board[torre-18]=='p')||(board[torre-9]=='p' && board[torre-18]=='p')) ris.add(torre-18);
				//SW mangia
				if(board[torre-9]=='P') ris.add(torre-9);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='p' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='p')||(board[torre-1]=='p' && board[torre-2]=='p')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='P') ris.add(torre-1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre==54) {
					//SE mangia
					if(board[torre-7]=='P') ris.add(torre-7);
					//E mangia
					if(board[torre+1]=='P') ris.add(torre+1);
				}
				if(torre==47) {
					//N mangia
					if(board[torre+8]=='P') ris.add(torre+8);
					//NW mangia
					if(board[torre+7]=='P') ris.add(torre+7);
					
				}
				if(torre==46) {
					//SE mangia
					if(board[torre-7]=='P') ris.add(torre-7);
					//NE mangia
					if(board[torre+9]=='P') ris.add(torre+9);
					//N mangia
					if(board[torre+8]=='P') ris.add(torre+8);
					//NW mangia
					if(board[torre+7]=='P') ris.add(torre+7);
					//E mangia
					if(board[torre+1]=='P') ris.add(torre+1);
				}
				
				return ris;
			}
				
			if(rigaTorre > 4 && colTorre >= 2 && colTorre <= 5) {//S,E,W,SE,SW
				//SUD
				//S spostamento
				if((board[torre-8]=='o' && board[torre-16]=='o')||(board[torre-8]=='p' && board[torre-16]=='o')||(board[torre-8]=='o' && board[torre-16]=='p')||(board[torre-8]=='p' && board[torre-16]=='p')) ris.add(torre-16);
				//S mangia
				if(board[torre-8]=='P') ris.add(torre-8);
				//SE spostamento
				if((board[torre-7]=='o' && board[torre-14]=='o')||(board[torre-7]=='p' && board[torre-14]=='o')||(board[torre-7]=='o' && board[torre-14]=='p')||(board[torre-7]=='p' && board[torre-14]=='p')) ris.add(torre-14);
				//SE mangia
				if(board[torre-7]=='P') ris.add(torre-7);
				//SW spostamento
				if((board[torre-9]=='o' && board[torre-18]=='o')||(board[torre-9]=='p' && board[torre-18]=='o')||(board[torre-9]=='o' && board[torre-18]=='p')||(board[torre-9]=='p' && board[torre-18]=='p')) ris.add(torre-18);
				//SW mangia
				if(board[torre-9]=='P') ris.add(torre-9);
				
				//EST
				//E spostamento
				if((board[torre+1]=='o' && board[torre+2]=='o')||(board[torre+1]=='p' && board[torre+2]=='o')||(board[torre+1]=='o' && board[torre+2]=='p')||(board[torre+1]=='p' && board[torre+2]=='p')) ris.add(torre+2);
				//E mangia
				if(board[torre+1]=='P') ris.add(torre+1);
				
				//WEST
				//W spostamento
				if((board[torre-1]=='o' && board[torre-2]=='o')||(board[torre-1]=='p' && board[torre-2]=='o')||(board[torre-1]=='o' && board[torre-2]=='p')||(board[torre-1]=='p' && board[torre-2]=='p')) ris.add(torre-2);
				//W mangia
				if(board[torre-1]=='P') ris.add(torre-1);
				
				//MOVIMENTI DA 1 (solo se si mangia)
				if(torre>=42 && torre<=45) {
					//NE mangia
					if(board[torre+9]=='P') ris.add(torre+9);
					//N mangia
					if(board[torre+8]=='P') ris.add(torre+8);
					//NW mangia
					if(board[torre+7]=='P') ris.add(torre+7);
				}
				
				return ris;
			}
		}
		
		return ris;
	}
	
	public static String convertToString(int src, int dest) {
		
		String[] righe = new String[]{"G","F","E","D","C","B","A"};
		String ris = "";
		
		int rSrc = src/8;
		int rDest = dest/8;
		int cSrc = src%8;
		int cDest = dest%8;
		
		ris += (righe[rSrc]+""+(cSrc+1)+",");
		
		int diffR = rDest-rSrc;
		int diffC = cDest-cSrc;
		
		if(diffR>0 && diffC==0)
			ris+="N";
		if(diffR>0 && diffC>0)
			ris+="NE";
		if(diffR>0 && diffC<0)
			ris+="NW";
		if(diffR==0 && diffC<0)
			ris+="W";
		if(diffR==0 && diffC>0)
			ris+="E";
		if(diffR<0 && diffC==0)
			ris+="S";
		if(diffR<0 && diffC>0)
			ris+="SE";
		if(diffR<0 && diffC<0)
			ris+="SW";
		
		return ris;
	}

	public static void stampaBoard(char[]board) {
		
		System.out.println("------------------------------------------------------------------");
		
		System.out.println(" 	1	2	3	4	5	6	7	8\n");
		System.out.println("A	"+board[48]+"	"+board[49]+"	"+board[50]+"	"+board[51]
				+"	"+board[52]+"	"+board[53]+"	"+board[54]+"	"+board[55]);
		System.out.println();
		System.out.println("B	"+board[40]+"	"+board[41]+"	"+board[42]+"	"+board[43]
				+"	"+board[44]+"	"+board[45]+"	"+board[46]+"	"+board[47]);
		System.out.println();
		System.out.println("C	"+board[32]+"	"+board[33]+"	"+board[34]+"	"+board[35]
				+"	"+board[36]+"	"+board[37]+"	"+board[38]+"	"+board[39]);
		System.out.println();
		System.out.println("D	"+board[24]+"	"+board[25]+"	"+board[26]+"	"+board[27]
				+"	"+board[28]+"	"+board[29]+"	"+board[30]+"	"+board[31]);
		System.out.println();
		System.out.println("E	"+board[16]+"	"+board[17]+"	"+board[18]+"	"+board[19]
				+"	"+board[20]+"	"+board[21]+"	"+board[22]+"	"+board[23]);
		System.out.println();
		System.out.println("F	"+board[8]+"	"+board[9]+"	"+board[10]+"	"+board[11]
				+"	"+board[12]+"	"+board[13]+"	"+board[14]+"	"+board[15]);
		System.out.println();
		System.out.println("G	"+board[0]+"	"+board[1]+"	"+board[2]+"	"+board[3]
				+"	"+board[4]+"	"+board[5]+"	"+board[6]+"	"+board[7]);
		
		System.out.println("------------------------------------------------------------------");
	}
	

	
}//class
