package de.hm.edu.verteilte.controller;

public class Constant {

	public static final int PORT = 1099;

	public static final String IP_SERVER = "127.0.0.1";

	public static final String IP = "localhost";

	public static final int CLIENTS = 2;
	// TODO Ungerade zahl
	public static final int SEATS = 6;

	public static final long EAT_LENGTH = 10;

	public static final long SLEEP_LENGTH = 50;

	public static final long MEDITATE_LENGTH = 7;

	public static final int DIFFERENZ = 20;

	public static final int TRIES_TO_GET_FORK = 5;

	public static final long TIME_TO_GET_RIGHT_FORK = EAT_LENGTH / 10;

	public static final long TIME_UNTIL_NEW_FORKTRY = EAT_LENGTH / 2;

	public static final int EAT_MAX_HUNGRY = 10;

	public static final int EAT_MAX_NORMAL = 3;

	public static final long BAN_FACTOR = 15;

	public static final int PHILOSOPHS = 6;

	public static final int HUNGRY_PHILOSOPHS = 4;
	
	public static final long GET_PHILLIST_RYTHM_INITIAL = 10;

	public static final long BACKUP_RYTHM = 500;

	public static final long TIME_UNTIL_NEW_PHILS_ARE_ADDED = 5000;
	
	private static int NEWID = PHILOSOPHS - 1;

	/**
	 * Berechnet die jeweiligen id des Philosophen
	 * 
	 * @return
	 */
	public static int createId() {
		Constant.NEWID++;
		return NEWID;
	}
}
