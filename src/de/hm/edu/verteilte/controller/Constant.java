package de.hm.edu.verteilte.controller;

public class Constant {
	
	public static final int PORT = 1099;
	
	public static final String IP_SERVER = "192.168.56.101";
	
	public static final String IP = "localhost";
	
	public static final int CLIENTS = 2;
	//TODO Ungerade zahl
	public static final int SEATS = 6;

	public static final long EAT_LENGTH = 1;
	
	public static final long SLEEP_LENGTH = 10;
	
	public static final long MEDITATE_LENGTH = 5;
	
	public static final int DIFFERENZ = 20;
	
	public static final int TRIES_TO_GET_FORK = 5;

	public static final long TIME_TO_GET_RIGHT_FORK = EAT_LENGTH/10;

	public static final long TIME_UNTIL_NEW_FORKTRY = EAT_LENGTH/2;

	public static final int EAT_MAX_HUNGRY = 10;

	public static final int EAT_MAX_NORMAL = 3;

	public static final long BAN_FACTOR = 15;
	
	
	private static int id = 0;
	
	public static int createId(){
		Constant.id++;
		return id;
	}
}
