package com.cst.spaceinvaders.game.enumerable;

public enum GameThreadState
{
	// Default state - when the thread is created
	THREAD_INIT,
	
	// The thread is actively running
	THREAD_RUN,
	
	// The thread is waiting
	THREAD_WAIT,
	
	// The thread is exiting
	THREAD_EXIT
}
