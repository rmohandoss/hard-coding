import java.util.*;
import java.io.*;
import java.lang.*;

class sudoku {

int[] board;
int[] ind_board;
int sq_length;
int cubrt;
// ***************************************
// *** Set the below value to 81 or 16 ***
// ***  81 for a 9x9 sudoku boardset   ***
// ***  16 for a 4x6 sudoku boardset   ***
// ***************************************
int board_elements=81;

sudoku() 
{
	Scanner s=new Scanner(System.in);
	board = new int[board_elements];
	ind_board = new int[board_elements];
	sq_length= (int)Math.sqrt((double)board_elements);
	cubrt= (int)Math.sqrt((double)sq_length);
	int count=0;
	int poss_value=0;
	int mx_row=0;
	int mx_column=0;	
	int row=0;
	int column=0;	

	count=0;

	while (count<board_elements) {

	mx_row=count/sq_length;
	mx_column=count%sq_length;

	row=mx_row+1;
	column=mx_column+1;

	System.out.println("Enter the value for position " + row + " " + column + " (Enter 0 if the cell needs to be solved) ");
	board[count]=s.nextInt();
	
	if (board[count] > 0)
		ind_board[count]=1;
	else	
		ind_board[count]=0;
	
	count++;
	}	

}

public void fill_board()
{
	// **************************************** DO NOT SKIP READING ME *************************************************	
	// This is a placeholder function if you wih to hardcode a sudoku board. Keep repeating the below pair for all cells
	// **************************************** DO NOT SKIP READING ME *************************************************	
	board[0]=0;
	ind_board[0]=0;	
}

public void show_board(int board_type)
{
	// **************************************** DO NOT SKIP READING ME *************************************************	
	// Pass 1 to display the actual board. Pass 2 to display the indicative board
	// **************************************** DO NOT SKIP READING ME *************************************************	
	int count=0;
	while (count<board_elements) {
	if (board_type == 1)
		System.out.format("%d ",board[count]);
	if (board_type == 2)
		System.out.format("%d ",ind_board[count]);
	if ((count+1)%9 == 0)
		System.out.format("\n");
	count++;
	}
}

public int issolved()
{
	int row=0;
	int column=0;
	int board_pos=0;
	int legit_sum=0;
	int check_sum=0;
	int box_row=0;
	int box_column=0;
	int end_row=0;
	int end_column=0;

	legit_sum=(sq_length*(sq_length+1))/2;

	// **** Check for the various row sum
	
	while (row<sq_length)
	{
		while (column<sq_length)
		{
			board_pos=(row*sq_length)+column;
			check_sum=check_sum+board[board_pos];
			column++;
		}
		if (check_sum != legit_sum)		
			return(0);
		column=0;
		check_sum=0;
		row++;
	}

	// **** Check for the various column sum

	row=0;
	column=0;
	check_sum=0;

	while (row<sq_length)
	{
		while (column<(sq_length*sq_length))
		{
			board_pos=row+column;
			check_sum=check_sum+board[board_pos];
			column=column+sq_length;
		}
		if (check_sum != legit_sum)		
			return(0);
		column=0;
		check_sum=0;
		row++;
	}
	
	// *** Check for the various sub boxes

	cubrt = (int)Math.sqrt((double)sq_length);
	
	box_row=0;
	box_column=0;
	row=0;
	column=0;
	check_sum=0;

	while (box_row<cubrt)
	{
		while (box_column<cubrt)
		{
			check_sum=0;
			row=box_row*cubrt;
			column=box_column*cubrt;
			end_row=row+cubrt;
			end_column=column+cubrt;
			while (row<end_row)
			{
				while (column<end_column)
				{
					board_pos=row*sq_length+column;
					check_sum=check_sum+board[board_pos];						
					column++;
				}
				row++;
				column=box_column*cubrt;
			}
			if (check_sum != legit_sum)		
				return(0);
			box_column++;
		}
		box_row++;
	}
					
	return(1);
}

public int solve(int curr_pos) {

	int row_ss=-1;
	int col_ss=-1;
	int box_ss=-1;
	int ss_potnl=-1;	
		
	Scanner ss=new Scanner(System.in);
	int value=1;
	if(issolved() == 1)
		return(1);
	if(curr_pos >= board_elements)
		return(0); 	
	if(ind_board[curr_pos] ==1)
		return(solve(curr_pos+1));
	else
	{
		while (value<=sq_length)	
		{
			board[curr_pos]=value;
			row_ss=row_conf(value,curr_pos);
			col_ss=col_conf(value,curr_pos);
			box_ss=box_conf(value,curr_pos);
	
			if((row_ss == -1)  && (col_ss == -1) && (box_ss == -1))
			{
				if(solve(curr_pos+1) == 1)
					return(1);
			}
			value++;	
		}
	board[curr_pos]=0;
	}
	return(0);
}

public int box_conf(int value, int curr_pos) {
	int end_row=0;
	int end_column=0;
	int curr_row=0;
	int curr_column=0;
	int row=0;
	int column=0;
	int cube_row=0;
	int cube_col=0;
	int board_pos=0;
	cubrt = (int)Math.sqrt((double)sq_length);

	curr_row=curr_pos/sq_length;
	curr_column=curr_pos%sq_length;

	cube_row=curr_row/cubrt;
	cube_col=curr_column/cubrt;

	cube_row=cube_row*cubrt;
	cube_col=cube_col*cubrt;

	row=cube_row;
	column=cube_col;
	end_row=cube_row+cubrt;
	end_column=cube_col+cubrt;

	while (row<end_row)
	{
		while (column<end_column)
		{
			board_pos=row*sq_length+column;
			if ((board_pos != curr_pos) && (value==board[board_pos]))
			{
				//System.out.println("Current Position "+curr_pos);
				//System.out.println("Box Conflict Yes");
				return(board_pos);
			}
			column++;
		}
		row++;
		column=cube_col;
	}
	return(-1);	
}

public int row_conf(int value, int curr_pos) {
	int start_pos=0;
	int end_pos=0;

	start_pos=(curr_pos/sq_length)*sq_length;
	end_pos=start_pos+sq_length;

	while (start_pos<end_pos)
	{
		if ((Math.abs(start_pos-curr_pos) <=2) && (start_pos/cubrt == curr_pos/cubrt))
		{
			start_pos++;
			continue;
		}
		if ((start_pos != curr_pos) && (value==board[start_pos]))
			return(start_pos);
		start_pos++;
	}
	return(-1);	
}

public int col_conf(int value, int curr_pos) {
	int start_pos=0;
	int end_pos=0;

	start_pos=curr_pos%sq_length;
	end_pos=start_pos+(sq_length*sq_length);

	while (start_pos<end_pos)
	{
		if ((Math.abs(start_pos-curr_pos) <=2*sq_length) && (start_pos/(cubrt*sq_length) == curr_pos/(cubrt*sq_length)))
		{
			start_pos=start_pos+sq_length;
			continue;
		}
		if ((start_pos != curr_pos) && (value==board[start_pos]))
			return(start_pos);
		start_pos=start_pos+sq_length;
	}
	return(-1);	
}

public static void main(String[] args)
{
	Scanner ss=new Scanner(System.in);
	sudoku chal = new sudoku();
	System.out.println("Initial State");
	System.out.println("");
	// chal.fill_board();
	// *** Pass 1 to display the actual board. Pass 2 to display the indicative board ***
	chal.show_board(1);
	System.out.println("");
	System.out.println("Solving ...");
	if (chal.solve(0) ==1)
	{
		System.out.println("");
		System.out.println("SUDOKU SOLVED !!");
		System.out.println("Press any character and hit enter to view the solution ..");
		char ch = ss.next().charAt(0);
		chal.show_board(1);
		System.out.println("");
	}
	else
	{
		chal.show_board(1);
		System.out.println("Puzzle could not be solved");
	}
}
}

