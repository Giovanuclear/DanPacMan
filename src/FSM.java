
public class FSM {
	State state;
	
	public void setState(State newState){
		//System.out.println("Inside setState");
		state = newState;
	}
	
	public State getState(){
		//System.out.println("Inside getState");
		return state;
	}
	
	public void removeState(){
		System.out.println("Inside removeState");
	}
}
