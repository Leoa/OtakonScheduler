package com.convention.notification.app;


import android.app.Application;

public class Singleton extends Application{
	
	private  volatile static Singleton uniqueInstance;
	//volatile assures one instance of a thread to run
	// static means this member can in initiated without the method being defined before it.
	private  Boolean goToServer= true;
	private Boolean x;

	
	//singleton
	
	public static Singleton getInstance(){
		if (uniqueInstance== null){
			synchronized(Singleton.class){
				if(uniqueInstance == null){
					uniqueInstance = new Singleton();
				}
			}
			
		}
		return uniqueInstance;
	}
	
	@SuppressWarnings("unused")
	private Boolean Singleton (){
		
		return goToServer;
	}

	public Boolean getgoToServer(){
		
		return goToServer;
		
	}
	
	public void setgotoServer(Boolean x){
		this.goToServer=x;
		
	}
}
