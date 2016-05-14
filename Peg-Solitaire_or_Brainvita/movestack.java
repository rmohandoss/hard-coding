public class movestack {

private move[] moves;
private int pos;

movestack()
{
	pos=-1;
	moves = new move[1000];
}

public int isEmpty()
{
	if(pos < 0)
		return(1);
	return(0);
}

public void push(move mv)
{
	moves[++pos]=mv;
}

public move pop()
{
	if(isEmpty() != 1)
		return(moves[pos--]);
	return(null);
}
}

