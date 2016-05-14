public class move {

int position;
int direction;
// 1-top, 2-right, 3-down, 4-left

move() 
{
	position=0;
	direction=0;
}
public void set(int pos, int dir)
{
	position=pos;
	direction=dir;
}
public int getpos()
{
	return(position);
}
public int getdir()
{
	return(direction);
}
public void show()
{
	int xval, yval;
	xval=(position/7) + 1;
	yval=(position%7) + 1;

	System.out.format("Position %d %d : ",xval,yval);
	if(direction == 1)
		System.out.println("Top");
	if(direction == 2)
		System.out.println("Right");
	if(direction == 3)
		System.out.println("Down");
	if(direction == 4)
		System.out.println("Left");
}
}

