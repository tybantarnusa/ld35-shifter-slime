package com.tybprojekt.ld35.game.states;

import java.util.Stack;

public class GameStateManager {
	private Stack<State> states;
	
	public GameStateManager() {
		states = new Stack<State>();
	}
	
	public State current() {
		return states.peek();
	}
	
	public State pop() {
		return states.pop();
	}
	
	public void push(State state) {
		states.push(state);
	}
	
	public void change(State state) {
		states.pop();
		states.push(state);
	}
}
