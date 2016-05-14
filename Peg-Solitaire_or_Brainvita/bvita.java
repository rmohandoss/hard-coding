import java.util.*;
import java.io.*;

class bvita {

int[] board;
movestack move_list;
movestack move_list_srt;
Collection<String> unsolveable;

bvita() 
{
	// **************************************** DO NOT SKIP READING ME *************************************************	
	// The board is initialized as a 7x7 matrix. Each cell in the matrix can have one of the 3 values.
	// 2 - Non-legitimate cell position. Since the board is cross shaped and not square shaped, there are 16 positions in this matrix (4 in each corner)
        //     that are not valid positions to move a marble to.
        // 1 - Cell position which has a marble 
        // 0 - Cell position which does not have a marble. The initial state will have only one cell with a value 0
	// **************************************** DO NOT SKIP READING ME *************************************************	

	board = new int[49];
	unsolveable = new HashSet<String>();
	move_list = new movestack();	
	move_list_srt = new movestack();	
	int count=0;
	while (count<49) {
	// **************************************** DO NOT SKIP READING ME *************************************************	
	// Set the value in the below line to mark the initial cell position that is empty. Though the matrix is a 7x7 one, the values range from 0 to 48.
        // If you wish to have the center cell as empty, the 'count' in the below statement should be checked for 24
	// **************************************** DO NOT SKIP READING ME *************************************************	
	if (count == 14)
		board[count]=0;
	else
	{
		if (count == 0 || count == 1 || count == 7 || count == 8 || count == 5 || count == 6 || count == 12 || count == 13 || count == 35 || count == 36 || count == 42 || count == 43 || count == 40 || count == 41 || count == 47 || count == 48)
			board[count]=2;	
		else	
			board[count]=1;
	}
	count++;
	}	
}
public void show_moves()
{
	move mv = new move();
	while (move_list.isEmpty() != 1)
		move_list_srt.push(move_list.pop());
	while (move_list_srt.isEmpty() != 1)
	{
		mv=move_list_srt.pop();
		mv.show();
	}			
}
public void show_board()
{
	int count=0;
	while (count<49) {
	System.out.format("%d ",board[count]);
	if ((count+1)%7 == 0)
		System.out.format("\n");
	count++;
	}
}
public int issolved()
{
	int marble_cnt=0;
	int count=0;
	while (count<49) {
	if (board[count] == 1)
		marble_cnt++;
	count++;
	}
	if (marble_cnt == 1)
		return(1);
	else
		return(0);
}
public Collection<move> find_moves()
{
	int count = 0;
	int xval = 0;
	int yval = 0;
	Collection<move> poss_moves = new HashSet<move>();

	while (count < 49) {
	xval=(count/7)+1;
	yval=(count%7)+1;
	if (board[count] == 1)
	{
		if (xval>2) //Possibility of a top move
		{
			if ((board[count-7] == 1) && (board[count-14] == 0))
			{
				move poss_move = new move();
				poss_move.set(count,1);
				poss_moves.add(poss_move);
			}
		}	 
		if (yval<6) //Possibility of a right move
		{
			if ((board[count+1] == 1) && (board[count+2] == 0))
			{
				move poss_move = new move();
				poss_move.set(count,2);
				poss_moves.add(poss_move);
			}
		}	 
		if (xval<6) //Possibility of a down move
		{
			if ((board[count+7] == 1) && (board[count+14] == 0))
			{
				move poss_move = new move();
				poss_move.set(count,3);
				poss_moves.add(poss_move);
			}
		}	 
		if (yval>2) //Possibility of a left move
		{
			if ((board[count-1] == 1) && (board[count-2] == 0))
			{
				move poss_move = new move();
				poss_move.set(count,4);
				poss_moves.add(poss_move);
			}
		}	
	}
	count++;
	}
	return(poss_moves);
}

public void make_move(move mv) {
	if (mv.getdir() == 1)
	{
		board[mv.getpos()] = 0;
		board[mv.getpos()-7] = 0;
		board[mv.getpos()-14] = 1;
	}		
	if (mv.getdir() == 2)
	{
		board[mv.getpos()] = 0;
		board[mv.getpos()+1] = 0;
		board[mv.getpos()+2] = 1;
	}		
	if (mv.getdir() == 3)
	{
		board[mv.getpos()] = 0;
		board[mv.getpos()+7] = 0;
		board[mv.getpos()+14] = 1;
	}		
	if (mv.getdir() == 4)
	{
		board[mv.getpos()] = 0;
		board[mv.getpos()-1] = 0;
		board[mv.getpos()-2] = 1;
	}		
}

public void un_move(move mv) {
	board[mv.getpos()] = 1;
	if (mv.getdir() == 1)
	{
		board[mv.getpos()-7] = 1;
		board[mv.getpos()-14] = 0;
	}		
	if (mv.getdir() == 2)
	{
		board[mv.getpos()+1] = 1;
		board[mv.getpos()+2] = 0;
	}		
	if (mv.getdir() == 3)
	{
		board[mv.getpos()+7] = 1;
		board[mv.getpos()+14] = 0;
	}		
	if (mv.getdir() == 4)
	{
		board[mv.getpos()-1] = 1;
		board[mv.getpos()-2] = 0;
	}		

}
public int solve() {
	Collection<move> moves = new HashSet<move>();
	String context = null;
	move redo_move = new move();
	if(issolved() == 1)
		return(1);
	moves = find_moves();	
	context = new StringBuilder(board[0]).append(board[1]).append(board[2]).append(board[3]).append(board[4]).append(board[5]).append(board[6]).append(board[7]).append(board[8]).append(board[9]).append(board[10]).append(board[11]).append(board[12]).append(board[13]).append(board[14]).append(board[15]).append(board[16]).append(board[17]).append(board[18]).append(board[19]).append(board[20]).append(board[21]).append(board[22]).append(board[23]).append(board[24]).append(board[25]).append(board[26]).append(board[27]).append(board[28]).append(board[29]).append(board[30]).append(board[31]).append(board[32]).append(board[33]).append(board[34]).append(board[35]).append(board[36]).append(board[37]).append(board[38]).append(board[39]).append(board[40]).append(board[41]).append(board[42]).append(board[43]).append(board[44]).append(board[45]).append(board[46]).append(board[47]).append(board[48]).toString();	
	if (moves.isEmpty())
	{
	//	System.out.println("Dead End");
		return(0);
	}
	if (unsolveable.contains(context))
	{
	//	System.out.println("Prev Dead End");
		return(0);
	}
	for (move a : moves)
	{
		//System.out.println("Making move");
		make_move(a);
		//show_board();
		//try 
		//{
		//	System.in.read();
		//}
		//catch(IOException exe)
		//{
		//	System.err.println("Error");		
		//}
		move_list.push(a);
		if(solve() ==1)
			return(1);
		redo_move = move_list.pop();		
		//System.out.println("Unmoving");
		un_move(redo_move);
		//show_board();
	}
	context = new StringBuilder(board[6]).append(board[13]).append(board[20]).append(board[27]).append(board[34]).append(board[41]).append(board[48]).append(board[5]).append(board[12]).append(board[19]).append(board[26]).append(board[33]).append(board[40]).append(board[47]).append(board[4]).append(board[11]).append(board[18]).append(board[25]).append(board[32]).append(board[39]).append(board[46]).append(board[3]).append(board[10]).append(board[17]).append(board[24]).append(board[31]).append(board[38]).append(board[45]).append(board[2]).append(board[9]).append(board[16]).append(board[23]).append(board[30]).append(board[37]).append(board[44]).append(board[1]).append(board[8]).append(board[15]).append(board[22]).append(board[29]).append(board[36]).append(board[43]).append(board[0]).append(board[7]).append(board[14]).append(board[21]).append(board[28]).append(board[35]).append(board[42]).toString();
	unsolveable.add(context);
	context = new StringBuilder(board[0]).append(board[1]).append(board[2]).append(board[3]).append(board[4]).append(board[5]).append(board[6]).append(board[7]).append(board[8]).append(board[9]).append(board[10]).append(board[11]).append(board[12]).append(board[13]).append(board[14]).append(board[15]).append(board[16]).append(board[17]).append(board[18]).append(board[19]).append(board[20]).append(board[21]).append(board[22]).append(board[23]).append(board[24]).append(board[25]).append(board[26]).append(board[27]).append(board[28]).append(board[29]).append(board[30]).append(board[31]).append(board[32]).append(board[33]).append(board[34]).append(board[35]).append(board[36]).append(board[37]).append(board[38]).append(board[39]).append(board[40]).append(board[41]).append(board[42]).append(board[43]).append(board[44]).append(board[45]).append(board[46]).append(board[47]).append(board[48]).toString();
	unsolveable.add(context);
	context = new StringBuilder(board[42]).append(board[35]).append(board[28]).append(board[21]).append(board[14]).append(board[7]).append(board[0]).append(board[43]).append(board[36]).append(board[29]).append(board[22]).append(board[15]).append(board[8]).append(board[1]).append(board[44]).append(board[37]).append(board[30]).append(board[23]).append(board[16]).append(board[9]).append(board[2]).append(board[45]).append(board[38]).append(board[31]).append(board[24]).append(board[17]).append(board[10]).append(board[3]).append(board[46]).append(board[39]).append(board[32]).append(board[25]).append(board[18]).append(board[11]).append(board[4]).append(board[47]).append(board[40]).append(board[33]).append(board[26]).append(board[19]).append(board[12]).append(board[5]).append(board[48]).append(board[41]).append(board[34]).append(board[27]).append(board[20]).append(board[13]).append(board[6]).toString();
	unsolveable.add(context);
	context = new StringBuilder(board[48]).append(board[47]).append(board[46]).append(board[45]).append(board[44]).append(board[43]).append(board[42]).append(board[41]).append(board[40]).append(board[39]).append(board[38]).append(board[37]).append(board[36]).append(board[35]).append(board[34]).append(board[33]).append(board[32]).append(board[31]).append(board[30]).append(board[29]).append(board[28]).append(board[27]).append(board[26]).append(board[25]).append(board[24]).append(board[23]).append(board[22]).append(board[21]).append(board[20]).append(board[19]).append(board[18]).append(board[17]).append(board[16]).append(board[15]).append(board[14]).append(board[13]).append(board[12]).append(board[11]).append(board[10]).append(board[9]).append(board[8]).append(board[7]).append(board[6]).append(board[5]).append(board[4]).append(board[3]).append(board[2]).append(board[1]).append(board[0]).toString();
	unsolveable.add(context);
	context = new StringBuilder(board[6]).append(board[5]).append(board[4]).append(board[3]).append(board[2]).append(board[1]).append(board[0]).append(board[13]).append(board[12]).append(board[11]).append(board[10]).append(board[9]).append(board[8]).append(board[7]).append(board[20]).append(board[19]).append(board[18]).append(board[17]).append(board[16]).append(board[15]).append(board[14]).append(board[27]).append(board[26]).append(board[25]).append(board[24]).append(board[23]).append(board[22]).append(board[21]).append(board[34]).append(board[33]).append(board[32]).append(board[31]).append(board[30]).append(board[29]).append(board[28]).append(board[41]).append(board[40]).append(board[39]).append(board[38]).append(board[37]).append(board[36]).append(board[35]).append(board[48]).append(board[47]).append(board[46]).append(board[45]).append(board[44]).append(board[43]).append(board[42]).toString();
	unsolveable.add(context);
	context = new StringBuilder(board[42]).append(board[43]).append(board[44]).append(board[45]).append(board[46]).append(board[47]).append(board[48]).append(board[35]).append(board[36]).append(board[37]).append(board[38]).append(board[39]).append(board[40]).append(board[41]).append(board[28]).append(board[29]).append(board[30]).append(board[31]).append(board[32]).append(board[33]).append(board[34]).append(board[21]).append(board[22]).append(board[23]).append(board[24]).append(board[25]).append(board[26]).append(board[27]).append(board[14]).append(board[15]).append(board[16]).append(board[17]).append(board[18]).append(board[19]).append(board[20]).append(board[7]).append(board[8]).append(board[9]).append(board[10]).append(board[11]).append(board[12]).append(board[13]).append(board[0]).append(board[1]).append(board[2]).append(board[3]).append(board[4]).append(board[5]).append(board[6]).toString();	
	unsolveable.add(context);
	context = new StringBuilder(board[48]).append(board[41]).append(board[34]).append(board[27]).append(board[20]).append(board[13]).append(board[6]).append(board[47]).append(board[40]).append(board[33]).append(board[26]).append(board[19]).append(board[12]).append(board[5]).append(board[46]).append(board[39]).append(board[32]).append(board[25]).append(board[18]).append(board[11]).append(board[4]).append(board[45]).append(board[38]).append(board[31]).append(board[24]).append(board[17]).append(board[10]).append(board[3]).append(board[44]).append(board[37]).append(board[30]).append(board[23]).append(board[16]).append(board[9]).append(board[2]).append(board[43]).append(board[36]).append(board[29]).append(board[22]).append(board[15]).append(board[8]).append(board[1]).append(board[42]).append(board[35]).append(board[28]).append(board[21]).append(board[14]).append(board[7]).append(board[0]).toString();
	unsolveable.add(context);
	context = new StringBuilder(board[0]).append(board[7]).append(board[14]).append(board[21]).append(board[28]).append(board[35]).append(board[42]).append(board[1]).append(board[8]).append(board[15]).append(board[22]).append(board[29]).append(board[36]).append(board[43]).append(board[2]).append(board[9]).append(board[16]).append(board[23]).append(board[30]).append(board[37]).append(board[44]).append(board[3]).append(board[10]).append(board[17]).append(board[24]).append(board[31]).append(board[38]).append(board[45]).append(board[4]).append(board[11]).append(board[18]).append(board[25]).append(board[32]).append(board[39]).append(board[46]).append(board[5]).append(board[12]).append(board[19]).append(board[26]).append(board[33]).append(board[40]).append(board[47]).append(board[6]).append(board[13]).append(board[20]).append(board[27]).append(board[34]).append(board[41]).append(board[48]).toString();	
	unsolveable.add(context);
	return(0);
}

public static void main(String[] args)
{
	bvita chal = new bvita();
	System.out.println("Initial State");
	System.out.println("");
	chal.show_board();
	if (chal.solve() != 0)
	{
		System.out.println("");
		chal.show_moves();	
		System.out.println("");
		chal.show_board();
		System.out.println("");
		System.out.println("BRAINVITA SOLVED !!");
	}
	else
		System.out.println("Puzzle could not be solved");
}
}

