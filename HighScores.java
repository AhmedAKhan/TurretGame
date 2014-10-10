// The "HighScores" class.
import java.awt.*;
import java.io.*; // io stands for input output
import java.applet.Applet;

public class HighScores
{
    static FileWriter fw;
    static PrintWriter output;
    static FileReader fr; //object f of class FileReader
    static BufferedReader input; // object input of class BufferedReader

    private String[] stats = new String [400];//creates a new array that holds all the stats of all the players
    private int counter;//counter used for the loop. This is not declared in the method because it is used in both methods and is used outside the loop
    public int[] scores;//creates a new array called scores which holds all the scores
    public String[] names;//holds all the names
    
    //creates the score and name textFields 
    private TextField[] scoreTextFields = new TextField[200];
    private TextField[] nameTextFields = new TextField[200];
    
    //this is the constructor code. There is nothing in it
    public HighScores () throws IOException
    {
	//constuctor code
    } // main method

    
    //this method is called when you write the score. it writes the new score that the player recieved
    public void writeScore (String name, int score) throws IOException
    {
	//creates the fileReader and input object
	fr = new FileReader ("highScores.txt");
	input = new BufferedReader (fr);

	for (counter = 0 ; true ; counter++)
	{
	    stats [counter] = input.readLine ();//reads the next line in the txt file
	    
	    if (stats [counter] == null)
	    { // if the end of the file is reached (the null character) break
		stats [counter] = name;
		stats [counter + 1] = score + "";
		break;
	    }
	} //end for

	input.close ();//closes the input. it is not use after this point so it is necessary to close it

	//creates a new fileWriter and output object
	fw = new FileWriter ("highScores.txt");
	output = new PrintWriter (fw);

	counter += 2;//increases the conuter by two

	//starts the loop which write all the scores in the txt file
	for (int counter2 = 0 ; counter2 < counter ; counter2++)
	{   
	    output.println (stats [counter2]);
	} //end for

	output.close ();
    }

    //this method is called when you go to the highscores page. this arranges it from biggest to smallest
    public void getHighscores (Applet theApplet) throws IOException
    {
	//creates the fileReader and input object
	fr = new FileReader ("highScores.txt");
	input = new BufferedReader (fr);

	//creates a new
	scores = new int [Math.round (stats.length / 2)];
	int swapNum, currentNum;//these are the intigers needed to sort the array
	
	String[] names = new String[Math.round (stats.length / 2)];
	String swapName, currentName;
	
	//sorts the scores array and does the exact samething with the names
	for (counter = 0 ; true ; counter++)
	{
	    stats [counter] = input.readLine ();
	     //sorts the scores array and does the exact samething with the names
	    if(counter%2 != 0){
		currentNum = Integer.parseInt (stats[counter]);
		currentName = stats[counter-1];
		for(int i =0; i < scores.length; i++){
		    //sorts the scores array and does the exact samething with the names
		   if(currentNum > scores[i]){
			swapNum = scores[i];
			scores[i] = currentNum;
			currentNum = swapNum; 
			
			swapName = names[i];
			names[i] = currentName;
			currentName = swapName;
		    }
		}
	    }   
	    if (stats [counter] == null)
		break;//breaks if stats doesnt equal anything
		
	} //end for
	     
	for(int x = 0; x < 10; x++){
	    
	    if(names[x] == null)
		break;//breaks the loop if its not equal to anything
	
	    //this creates a new textfield with the players name
	    nameTextFields[x] = new TextField(names[x]);
	    nameTextFields[x].setBounds(150 - 70, 120 + x * 25, 200, 20);
	    nameTextFields[x].setEditable(false);
	    theApplet.add(nameTextFields[x]);
	    
	    //this creates a new textfield with the players score
	    scoreTextFields[x] = new TextField(scores[x] + "");
	    scoreTextFields[x].setBounds(380 - 70, 120 + x * 25, 150, 20);
	    scoreTextFields[x].setEditable(false);
	    theApplet.add(scoreTextFields[x]);
	
	}
	
	input.close ();//closes the input
    }//add text fields 
    
    public void removeHighscores (Applet theApplet)
    {
       for(int x = 0; x < 10; x++){
	    
	    if(nameTextFields[x] == null)
		break;//if the textfield does not exist it breaks the loop
		
	    //removes all the text fields
	    theApplet.remove(nameTextFields[x]);
	    theApplet.remove(scoreTextFields[x]);
	
	}
    }
} // HighScores class
