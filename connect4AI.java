import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Point;
import java.util.*;
import java.util.Random;



class State implements Cloneable
{


	int rows, cols;
	char[][] board;

	/* basic methods for constructing and proper hashing of State objects */
	public State(int n_rows, int n_cols){
		this.rows=n_rows;
		this.cols=n_cols;
		this.board=new char[n_rows][n_cols];
		
		//fill the board up with blanks
		for(int i=0; i<n_rows; i++)
			for(int j=0; j<n_cols; j++)
				this.board[i][j]='.';
	}
	
	public boolean equals(Object obj){
		
		State other=(State)obj;
		return Arrays.deepEquals(this.board, other.board);
	}
	
	public int hashCode(){
		String b="";
		for(int i=0; i<board.length; i++)
			b+=String.valueOf(board[0]);
		return b.hashCode();
	}

	public Object clone() throws CloneNotSupportedException {
        State new_state=new State(this.rows, this.cols);
		for (int i=0; i<this.rows; i++)
			new_state.board[i] = (char[]) this.board[i].clone();
		return new_state;
	}
	
	
	
	/* returns a list of actions that can be taken from the current state
	actions are integers representing the column where a coin can be dropped */
	public ArrayList<Integer> getLegalActions(){
		ArrayList<Integer> actions=new ArrayList<Integer>();
		for(int j=0; j<this.cols; j++)
			if(this.board[0][j]=='.')
				actions.add(j);
		return actions;
	}
	
	/* returns a State object that is obtained by the agent (parameter)
	performing an action (parameter) on the current state */
	public State generateSuccessor(char agent, int action, int d ,State st) throws CloneNotSupportedException{
		int row;
		for(row=0; row<this.rows && this.board[row][action]!='X' && this.board[row][action]!='O'; row++);
		State new_state=(State)this.clone();
         if(row!=0){
		new_state.board[row-1][action]=agent;
		return new_state;
         }
		return st;
	}
	
	/* Print's the current state's board in a nice pretty way */
	public void printBoard(){
		System.out.println(new String(new char[this.cols*2]).replace('\0', '-'));
		for(int i=0; i<this.rows; i++){
			for(int j=0; j<this.cols; j++){
				System.out.print(this.board[i][j]+" ");
			}
			System.out.println();
		}	
		System.out.println(new String(new char[this.cols*2]).replace('\0', '-'));
	}
	
	/* returns True/False if the agent(parameter) has won the game
	by checking all rows/columns/diagonals for a sequence of >=4 */
	public boolean isGoal(char agent){
	
		String find=""+agent+""+agent+""+agent+""+agent;
		
		//check rows
		for(int i=0; i<this.rows; i++)
			if(String.valueOf(this.board[i]).contains(find))
				return true;
		
		//check cols
		for(int j=0; j<this.cols; j++){
			String col="";
			for(int i=0; i<this.rows; i++)
				col+=this.board[i][j];
				
			if(col.contains(find))
				return true;
		}
		
		//check diags
		ArrayList<Point> pos_right=new ArrayList<Point>();
		ArrayList<Point> pos_left=new ArrayList<Point>();
		
		for(int j=0; j<this.cols-4+1; j++)
			pos_right.add(new Point(0,j));
		for(int j=4-1; j<this.cols; j++)
			pos_left.add(new Point(0,j));	
		for(int i=1; i<this.rows-4+1; i++){
			pos_right.add(new Point(i,0));
			pos_left.add(new Point(i,this.cols-1));
		}
	
		//check right diags
		for (Point p : pos_right) {
			String d="";
			int x=p.x, y=p.y;
			while(true){				
				if (x>=this.rows||y>=this.cols)
					break;
				d+=this.board[x][y];
				x+=1; y+=1;
			}
			if(d.contains(find))
				return true;
		}
		
		//check left diags
		for (Point p : pos_left) {
			String d="";
			int x=p.x, y=p.y;
			while(true){
				if(y<0||x>=this.rows||y>=this.cols)
					break;
				d+=this.board[x][y];
				x+=1; y-=1;
			}
			if(d.contains(find))
				return true;
		}
		
		return false;
		
	}
	
	

	/* returns the value of each state for minimax to min/max over at zero depth.  */
	public double evaluationFunction(){
	
		if (this.isGoal('O'))
			return 1000.0;
		if (this.isGoal('X'))
			return -1000.0;
		
		return 0.0;
	}
	
	
	
	
	
	
	


}


public class connect4AI{
// Initial values of
// Alpha and Beta
public static  double Al = 10000000;
public static  double Be = -10000000;
	public static void main(String[] args) throws CloneNotSupportedException{
	
	System.out.println("Enter the depth:");
	Scanner in = new Scanner(System.in);
	int depth = in.nextInt();

    char ch;
	System.out.println("choose [a] to play with Algorithm , [b] to make two agents play(random vs algorithm) ");
    ch = in.next().charAt(0);  
    switch(ch){
		case 'a':
		System.out.println("choose [m] for minimax , [p] for AlphaBetaProuning :");
		ch = in.next().charAt(0);  
		switch(ch){
        case 'm':
        long startTime = System.nanoTime();
        minimaxAgent mma = new minimaxAgent(depth);
        State s=new State(6,7);
        while(true){
         int action = mma.getAction(s);
         Random ran = new Random();
         s = s.generateSuccessor('O', action ,depth ,s );
         int rand = ran.nextInt(7);
         System.out.println(rand);
         
           s.printBoard();
           //check if O won?
           if(s.isGoal('O'))
           break;
         System.out.println("choose your move");
         
         int enemy_move = in.nextInt();
         s = s.generateSuccessor('X', enemy_move , depth , s);
           s.printBoard();

           //check if X won? break
           if(s.isGoal('X'))
           break;
           //pause
             }
            long endTime   = System.nanoTime();
            long totalTime = endTime - startTime;
            System.out.println("minimax Running time =  "+totalTime +"ms");

        break;

        case 'p':
        long sTime = System.nanoTime();
        AlphaBetaAgent m = new AlphaBetaAgent(depth);
        State st=new State(6,7);
	    while(true){
        int action = m.APSearch(st);
        Random ran = new Random();
        st = st.generateSuccessor('O', action , depth ,st );
        int rand = ran.nextInt(7);
    
      System.out.println(rand);
      
		st.printBoard();
		//check if O won?
		if(st.isGoal('O'))
		break;
		System.out.println("choose your move");
         
		int enemy_move = in.nextInt();
		st = st.generateSuccessor('X', enemy_move , depth , st);
		st.printBoard();

		//check if X won? break
		if(st.isGoal('X'))
		break;
		//pause
	      }
         long eTime   = System.nanoTime();
         long totTime = eTime - sTime;
         System.out.println("alphabeta pruning Running time =  "+totTime+"ms");

        break;
        }//switch 

		break;   

		case'b':
		System.out.println("choose [m] for minimax , [p] for AlphaBetaProuning :");
		ch = in.next().charAt(0);  
		switch(ch){
			case 'm':
			long startTime = System.nanoTime();
			minimaxAgent mma = new minimaxAgent(depth);
			State s=new State(6,7);
			while(true){
			 int action = mma.getAction(s);
			 Random ran = new Random();
			 s = s.generateSuccessor('O', action ,depth ,s );
			 int rand = ran.nextInt(7);
			 System.out.println(rand);
			 
			   s.printBoard();
			   //check if O won?
			   if(s.isGoal('O'))
			   break;
			   s = s.generateSuccessor('X', rand , depth ,s);
	   
			   s.printBoard();
			   //check if X won? break
			   if(s.isGoal('X'))
			   break;
			   //pause
				 }
				long endTime   = System.nanoTime();
				long totalTime = endTime - startTime;
				System.out.println("minimax Running time =  "+totalTime +"ms");
	
			break;
	
			case 'p':
			long sTime = System.nanoTime();
			AlphaBetaAgent m = new AlphaBetaAgent(depth);
			State st=new State(6,7);
			while(true){
			int action = m.APSearch(st);
			Random ran = new Random();
			st = st.generateSuccessor('O', action , depth ,st );
			int rand = ran.nextInt(7);
		
		    System.out.println(rand);
		  
			st.printBoard();
			//check if O won?
			if(st.isGoal('O'))
			break;
		    st = st.generateSuccessor('X', rand , depth, st);
			st.printBoard();
			
			//check if X won? break
			if(st.isGoal('X'))
			break;
			//pause
			  }
			 long eTime   = System.nanoTime();
			 long totTime = eTime - sTime;
			 System.out.println("alphabeta pruning Running time =  "+totTime+"ms");
	
			break;
			}//switch 
			
		}
	}
   
}
