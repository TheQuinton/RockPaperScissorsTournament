package tournament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Round {
	int loser,firstPlayerWins,secondPlayerWins,firstPlayerNumber,secondPlayerNumber;
	String firstPlayerInput,secondPlayerInput;
	ArrayList<String> rps = new ArrayList<String>();
	ArrayList<Player> allPlayers;
	HashMap<String, String> inputConnector = new HashMap<String, String>();
	@SuppressWarnings("serial")
	List<String> firstPlayerValid = new ArrayList<String>() {{add("s");add("d");add("f");}};
	@SuppressWarnings("serial")
	List<String> secondPlayerValid = new ArrayList<String>() {{add("j");add("k");add("l");}};
	public Round(int playerA, int playerB, int games, ArrayList<Player> player) {
		allPlayers = player;
		firstPlayerNumber = playerA;
		secondPlayerNumber = playerB;
		firstPlayerWins = 0;
		secondPlayerWins = 0;
		for (int i = 0; i < games; i++) {
			allPlayers.get(firstPlayerNumber).setGamesPlayed();	//adds to the number of games that the player has played
			allPlayers.get(secondPlayerNumber).setGamesPlayed();
			inputConnector.clear();	//clears the hashtable to allow for new randomized inputs
			System.out.print(allPlayers.get(firstPlayerNumber).getName() + " > ");
			randomizeInputs(firstPlayerValid);
			System.out.print(allPlayers.get(secondPlayerNumber).getName() + " > ");
			randomizeInputs(secondPlayerValid);
			analyzeInputs(getPlayerInputs());
			humanhuman();	//had to keep something from the other group --> most descriptive name for a method
		}
		allPlayers.get(firstPlayerNumber).setMatchesPlayed();	//adds to the number of matches that the player has played
		allPlayers.get(secondPlayerNumber).setMatchesPlayed();
		setLoser();
	}
	public void setLoser(){
		if (firstPlayerWins > secondPlayerWins) {
			loser = secondPlayerNumber;
			System.out.println(allPlayers.get(firstPlayerNumber).getName() + " won the match!");
			allPlayers.get(firstPlayerNumber).setMatchesWon();
		} else if (secondPlayerWins > firstPlayerWins){
			loser = firstPlayerNumber;
			System.out.println(allPlayers.get(secondPlayerNumber).getName() + " won the match!");
			allPlayers.get(secondPlayerNumber).setMatchesWon();
		} else {
			System.out.println("Tie game, so one player randomly loses");
			Random r = new Random();
			int random = r.nextInt(((2-1) - 0) + 1) + 1;
			if (random == 0) {
				loser = firstPlayerNumber;
				System.out.println(allPlayers.get(secondPlayerNumber).getName() + " won the match!");
				allPlayers.get(secondPlayerNumber).setMatchesWon();
			} else {
				loser = secondPlayerNumber;
				System.out.println(allPlayers.get(firstPlayerNumber).getName() + " won the match!");
				allPlayers.get(firstPlayerNumber).setMatchesWon();
			}
		}
	}
	public int getLoser(){
		return loser;
	}
	public ArrayList<Player> getNewPlayerStats(){
		return allPlayers;
	}
	public void randomizeInputs(List<String> valid){	
		rps.add("rock");
		rps.add("paper");
		rps.add("scissors");
		for (int i = 0; i < valid.size(); i++) {
			Random r = new Random();
			int random = r.nextInt(((rps.size()-1) - 0) + 1) + 1;	//finds random integer from 0 and the size of rps, or the number of objects it contains
			String output = String.format("%1$-" + 15 + "s", valid.get(i) + " - "+ rps.get(random-1));
			System.out.print(output);
			String x = rps.get(random-1);
			inputConnector.put(valid.get(i), x);	//holds the string s,d,f,j,k,l and connects them to the randomly set string from rps
			rps.remove(x);
		}
		System.out.println();
	}
	public String getPlayerInputs(){
		String choices;
		System.out.println("ROCK PAPER SCISSORS! SHOOT!... tap your keys! and then press ENTER");
		while (true) {
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			choices = input.nextLine().toLowerCase();	//gets the input from players, makes it lower case
			if (choices.length() == 2) {
				break;
			} else{
				System.out.println("Invalid Input: Only one input per person allowed, so only two letters");
				System.out.println("Try Again > ");
				continue;
			}
		}
		return choices;
	}
	public void analyzeInputs(String choices){
		String first = choices.substring(0,1);	//first character of input
		String second = choices.substring(1);	//second character of input
		if (firstPlayerValid.contains(first)) {	//checks if first character is in first player's choice of keys
			firstPlayerInput = inputConnector.get(first);	// if first character is in first player's choice of keys, sets first player's input as first character
			if (secondPlayerValid.contains(second)) {	//checks if second character is in second player's choice of keys
				secondPlayerInput = inputConnector.get(second);	//if second character is in second player's choice of keys, sets second player's input as second character
			} else {
				secondPlayerInput = "forfeit";	
			}
		} else if (secondPlayerValid.contains(first)) {	//checks if first character is in second player's choice of keys
			secondPlayerInput = inputConnector.get(first);	//if first character is in second player's choice of keys, sets second player's input as first character
			if (firstPlayerValid.contains(second)) {	//checks if second character is in first player's choice of keys
				firstPlayerInput = inputConnector.get(second);	//if second character is in first player's choice of keys, sets first player's input as second character
			} else {
				firstPlayerInput = "forfeit";
			}
		} else {	// invalid first character input
			if (firstPlayerValid.contains(second)) {	//checks if second character is in first player's choice of keys, while first character is invalid
				firstPlayerInput = inputConnector.get(second);	//if second character is in first player's choice of keys, sets first player's input as the second character
				secondPlayerInput = "forfeit";	// and thus second player's input is invalid
			} else if (secondPlayerValid.contains(second)) {	//checks if second character is in second player's choice of keys, while first character is invalid
				secondPlayerInput = inputConnector.get(second);	//if second character is in second player's choice of keys, sets second player's input as the second character
				firstPlayerInput = "forfeit";	//and thus first player's input is invalid
			} else {
				firstPlayerInput = "forfeit";
				secondPlayerInput = "forfeit";
			}
		}
	}
	public void humanhuman(){
		//check for time game
		if (firstPlayerInput.equals(secondPlayerInput)) {	//tie game scenario
			System.out.println("TIE GAME");
			System.out.println("You both picked: "+ firstPlayerInput);
			if (firstPlayerInput.equals("forfeit")) {	//saves forfeits if both forfeited 
				allPlayers.get(firstPlayerNumber).setForfeits();
				allPlayers.get(secondPlayerNumber).setForfeits();
			}
			return;	//exits method if tie game
		}
		//handle forfeits
		if (firstPlayerInput.equals("forfeit")) {	//player 1 forfeits
			System.out.println(allPlayers.get(secondPlayerNumber).getName() + " won this game! "+ allPlayers.get(firstPlayerNumber).getName() + " forfeited the game!");
			allPlayers.get(secondPlayerNumber).setGamesWon();
			secondPlayerWins++;
			allPlayers.get(firstPlayerNumber).setForfeits();
			return;	//exits method if player 1 forfeited
		} else if (secondPlayerInput.equals("forfeit")) {	//player 2 forfeits
			System.out.println(allPlayers.get(firstPlayerNumber).getName() + " won this game! "+ allPlayers.get(secondPlayerNumber).getName() + " forfeited the game!");
			allPlayers.get(firstPlayerNumber).setGamesWon();
			firstPlayerWins++;
			allPlayers.get(secondPlayerNumber).setForfeits();
			return;	//exits method if player 2 forfeited
		} 
		//if both inputs are valid then handle them
		if (firstPlayerInput.equals("rock")) {	//if first player picked rock
			if (secondPlayerInput.equals("paper")) {
				System.out.println(allPlayers.get(secondPlayerNumber).getName() + " WINS! PAPER COVERS ROCK!");
				allPlayers.get(secondPlayerNumber).setGamesWon();
				secondPlayerWins++;
				return;
			} else if (secondPlayerInput.equals("scissors")) {
				System.out.println(allPlayers.get(firstPlayerNumber).getName() + " WINS! ROCK CRUSHES SCISSORS!");
				allPlayers.get(firstPlayerNumber).setGamesWon();
				firstPlayerWins++;
				return;
			}
		} else if (firstPlayerInput.equals("paper")) {	//if first player picked paper
			if (secondPlayerInput.equals("scissors")) {
				System.out.println(allPlayers.get(secondPlayerNumber).getName() + " WINS! SCISSORS CUTS PAPER!");
				allPlayers.get(secondPlayerNumber).setGamesWon();
				secondPlayerWins++;
				return;
			} else if (secondPlayerInput.equals("rock")) {
				System.out.println(allPlayers.get(firstPlayerNumber).getName() + " WINS! PAPER COVERS ROCK!");
				allPlayers.get(firstPlayerNumber).setGamesWon();
				firstPlayerWins++;
				return;
			}
		} else if (firstPlayerInput.equals("scissors")) {	//if first player picked scissors
			if (secondPlayerInput.equals("paper")) {
				System.out.println(allPlayers.get(firstPlayerNumber).getName() + " WINS! SCISSORS CUTS PAPER!");
				allPlayers.get(firstPlayerNumber).setGamesWon();
				firstPlayerWins++;
				return;
			} else if (secondPlayerInput.equals("rock")) {
				System.out.println(allPlayers.get(secondPlayerNumber).getName() + " WINS! ROCK CRUSHES SCISSORS!");
				allPlayers.get(secondPlayerNumber).setGamesWon();
				secondPlayerWins++;
				return;
			}
		}
	}
}