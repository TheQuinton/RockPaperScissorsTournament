package tournament;
/*	Group: Foxtrot
 *  Group Members:  Quinton Schafer, Homare Takase
 *	Instructions: save files will be saved as 'playername'.txt, to load existing players place save files in the containing folder
 */	
import java.util.ArrayList;
import java.util.Scanner;

public class Tournament {
	static int numberOfPlayers, gamesPerMatch;
	static ArrayList<Integer> playersLeft = new ArrayList<Integer>();
	static ArrayList<Player> player = new ArrayList<Player>();

	public static void main(String[] args) {	// no explanation required.
		gameIntro();
		createPlayers();
		setGamesPerMatch();
		playTournament();
		endGameResults();
	}
	public static void gameIntro(){
		System.out.println("Welcome to the Rock Paper Scissors Tournament!");
		while (true) {	//set the number of number of players
			try {
				@SuppressWarnings("resource")
				Scanner input = new Scanner(System.in);		
				System.out.println("Please enter the number of players: ");
				numberOfPlayers = Integer.parseInt(input.nextLine());
				break;
			}
			catch(Exception e){ //only happens if input is not an integer
				System.out.println("Invalid Input: Must be an integer");
			}
		}
	}
	public static void createPlayers(){
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		for (int i = 0; i < numberOfPlayers; i++) {
			System.out.println("Enter the name of player "+ (i+1) + ": ");	// i+1 displays the 'proper' player numbers
			String name = input.nextLine();
			player.add(new Player(name,i));
			player.get(i).load();	//loads player save data from file
			playersLeft.add(i);	//adds all of the player numbers to a list
			player.get(i).setTournamentsPlayed();	//adds 1 to the number of tournaments a player has been in 
		}
	}
	public static void setGamesPerMatch(){
		while (true) {	//set the number of games per match
			try {
				@SuppressWarnings("resource")
				Scanner input = new Scanner(System.in);
				System.out.println("Enter the number of games for each match, it must be odd: ");
				gamesPerMatch = Integer.parseInt(input.nextLine());
				if (gamesPerMatch%2 != 0) {	//makes sure the number of games is odd
					break;
				} else {	//only happens if input is not odd
					System.out.println("Invalid Input: Number must be odd");
				}
			}
			catch(Exception e){ //only happens if input is not an integer
				System.out.println("Invalid Input: Must be an integer and odd");
			}
		}
	}
	public static void playTournament(){	//simple tournament logic
		int numberOfRounds;	//creates the numberOfRounds variable which will determine the number of rounds in each set
		while (playersLeft.size() != 1) {	//runs the tournament until there is only one player left (one loop through is one set of rounds)
			boolean hasBy = false;	//resets the boolean that indicates if a player has a by
			if (playersLeft.size()%2 != 0) {	//if odd number of players left --> indicates a by
				hasBy = true;	
				numberOfRounds = (playersLeft.size()+1)/2;	//sets the number of rounds to a whole number in order to prevent an error
			}else{
				numberOfRounds = playersLeft.size()/2;	//sets the number of rounds to half that of the players
			}
			for (int i = 0; i < numberOfRounds; i++) {	//each round loop
				if (i+1 == numberOfRounds && hasBy == true) {	//if the loop is on its last loop through and there is a by to occur
					System.out.println(player.get(i+1).getName() + " has a by this round");	//last player in the list gets a by
				} else{	//normal game round
					Integer loser = new Integer(playRound(player.get(playersLeft.get(i)).getNumber(),player.get(playersLeft.get(i+1)).getNumber()));
					playersLeft.remove(loser);
					continue; //removes the loser from the remaining players
				}
			}
		}
		int winner = playersLeft.get(0);
		System.out.println(player.get(winner).getName() + " WON THE TOURNAMENT!");
		player.get(winner).setTournamentsWon();
	}
	public static int playRound(int playerA, int playerB){	//creates a round which two selected players play in, and returns the winner's player number
		Round round = new Round(playerA, playerB,gamesPerMatch,player);
		player = round.getNewPlayerStats();
		return round.getLoser();	//returns the winner of the round
	}
	public static void endGameResults(){	//table of stats
		//For spacing use: String.format("%1$-" + 15 + "s", text); where 15 is the number of spaces
		System.out.println("Names          | Games Won | Games Played | Matches Won | Matches Played | Forfeits | Tournaments Won | Tournaments Played");
		for (int i = 0; i < numberOfPlayers; i++) {
			player.get(i).save();
			System.out.print(String.format("%1$-" + 15 + "s", player.get(i).getName()));
			System.out.print(String.format("%1$-" + 12 + "s", ("|   " + player.get(i).getGamesWon())));
			System.out.print(String.format("%1$-" + 15 + "s", ("|   " + player.get(i).getGamesPlayed())));
			System.out.print(String.format("%1$-" + 14 + "s", ("|   " + player.get(i).getMatchesWon())));
			System.out.print(String.format("%1$-" + 17 + "s", ("|   " + player.get(i).getMatchesPlayed())));
			System.out.print(String.format("%1$-" + 11 + "s", ("|   " + player.get(i).getForfeits())));
			System.out.print(String.format("%1$-" + 18 + "s", ("|   " + player.get(i).getTournamentsWon())));
			System.out.print(String.format("%1$-" + 15 + "s", ("|   " + player.get(i).getTournamentsPlayed())));
			System.out.println();
		}
	}

}
