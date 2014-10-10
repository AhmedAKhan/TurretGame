/*
Ahmed Khan 
440598

Firday, June 15th 2012

purpose 
    This is a game where enemies try to destroy you. they come from all directions and you have to kill then before they can hurt you. 
    survive as long as you without dieing. beat your friends highscore and earn unlimited bragging rights.
 
*/

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class AsteroidsMainClass extends Applet//a subclass of Applet
    implements MouseListener, MouseMotionListener, Runnable, ActionListener 
{
    private Image menu;//the normal menu
    private Image menu1;//when play game is highlighted
    private Image menu2;//when instructions is highlighted
    private Image menu3;//when highscores is highlighted
    private Image instructionPg;//the image of the instruction page
    private Image gameOver;//the image of the game over page
    private Image highscoresPG;//the background image for the highscores page
    
    private int width, height;

    private Thread th;//variable the stores the thread
    private Shot[] shots;//array that stores the refrence to the shots objects
    private Player player;//variable to the refrence to the player
    private Enemy[] enemies;//array that stores the refrences to the enemies
    private double playerRotation;//the player rotation
    
    private int enemyStatsTimer = 0; //im gonna use this as a timer. when it equals 100 then it will spawn an enemy
    private int enemySpawnTimer =0;//controls the spawning of the enemies
    
    private String frame = "mainMenu";//controls which frame the game is on ex, menu, instruction
    private String mouseHit = "nothing";//checks to see what the mouse is hitting in the main menu
    private String mouseTouching = "nothing";//checks what the mouse is touching
    
    int BULLET_SPEED = 7; //the speed of the bullet
    int ENEMY_SPEED = 3;//speed of the enemy
    int health = 100;//health of the player
    int score = 0;//score the user earned
    int ENEMY_STRENGTH = 10;//strength of the enemy. increases by 1 each time. maximum 20
    int numEnemiesSpawned = 0;//number of enemies that spawned
    int maxNumToSpawn = 1;//num of enemies that will spawn in total at this time.
    
    private TextField name;//the user enters there name
    private Button start;//lets you start the game
    private Button pause;//the refrence variable for the pause button
    private TextField healthText;//the textfield that displays the score and health
    
    private HighScores highscoresList;
    
    boolean gamePaused = false;//checks if the game is paused
    
    public void init ()
    {
	//setBackground (Color.black);
	width = getSize ().width;
	height = getSize ().height;
	
	enemies = new Enemy [50];//creates an empty array with 50 things inside them
	shots = new Shot [30];//creates an empty array with 30 elements
	player = new Player (width / 2, height / 2);//creates the player
		    
	if(frame.equals("mainMenu")){
	    //gets and prepares all the images for the main menu. so the paint method can run it faster
	    menu = getToolkit().getImage("mainScreen.png");
	    menu1 = getToolkit().getImage("mainScreen_PlayGame_Pressed.png");
	    menu2 = getToolkit().getImage("mainScreen_Instructions_Pressed.png");
	    menu3 = getToolkit().getImage("mainScreen_HighScores_Pressed.png");
	    instructionPg = getToolkit().getImage("instructions.png");
	    highscoresPG = getToolkit().getImage("highscoresPage.png");
	    gameOver = getToolkit().getImage("gameOver.png");
	    prepareImage(instructionPg, this);
	    prepareImage(menu, this);
	    prepareImage(menu1, this);
	    prepareImage(menu2, this);
	    prepareImage(menu3, this);
	    prepareImage(highscoresPG, this);
	    prepareImage(gameOver, this);
	    
	}else if(frame.equals("game")){
	
	    pause = new Button("pause");//creates the pause button
	    this.add(pause);//adds it to the stage
	    pause.setBounds(210, 450, 70, 20);
	    pause.addActionListener(this); //adds a listener to the button
	    
	    //creates the healthText, makes it uneditable and adds it to the applet
	    healthText = new TextField("health: 100 Score: 0");
	    healthText.setEditable(false);
	    healthText.setBounds( 300, 450, 140, 20);
	    this.add(healthText);
	    
	    th = new Thread (this);//creates a new thread
	}
	
	//adds all the listeners
	addMouseListener (this);
	addMouseMotionListener (this);
    }

    public void mousePressed (MouseEvent e){}
    public void mouseReleased (MouseEvent e){}
    public void mouseMoved (MouseEvent e){ 
	if(frame.equals("mainMenu")){
	    //controls which picture the main menu will have depending on where your mouse is
	    mainMenuMouseMoved(e);
	}//end frame if
	else if(frame.equals("game")){
	    //rotates the player
	    player.rotation = Math.atan2 (e.getY () - height / 2, e.getX () - width / 2) * 180 / Math.PI;
	}//end frame if
	else if(frame.equals("instructions")){
	    //checks to see if you are at the instructions page if you are then it checks if your mouse is touching the main menu text
	    //if it is then it changes it to black
	    if(e.getX() > 121 && e.getX() < 330 && e.getY() > 400 && e.getY() < 430){
		if(mouseTouching.equals("nothing")){
		    instructionPg = getToolkit().getImage("instructions_mainMenu.png");
		    prepareImage(instructionPg, this);
		    repaint();
		    mouseTouching = "mainMenuButton";
		}
	    }else{
		if(mouseTouching.equals("mainMenuButton")){
		    instructionPg = getToolkit().getImage("instructions.png");
		    prepareImage(instructionPg, this);
		    repaint();
		    mouseTouching = "nothing";
		}
	    }
	}//end frame if
	else if(frame.equals("gameOver")){
	    //checks to see if you are at the gameOver page if you are then it checks if your mouse is touching the main menu text
	    //if it is then it changes it to black
	    if(e.getX() > 131 && e.getX() < 346 && e.getY() > 371 && e.getY() < 400){
		if(mouseHit.equals("nothing")){
		    gameOver = getToolkit().getImage("gameOver_mainMenu.png");
		    prepareImage(gameOver, this);
		    mouseHit = "mainMenu";
		    repaint();
		}
	    }else if(mouseHit.equals("mainMenu")){
		gameOver = getToolkit().getImage("gameOver.png");
		prepareImage(gameOver, this);
		mouseHit = "nothing";
		repaint();
	    }
	}//end frame if
	else if(frame.equals("highscores")){
	    if(e.getX() > 131 && e.getX() < 346 && e.getY() > 371 && e.getY() < 430){
		if(mouseHit.equals("nothing")){
		    highscoresPG = getToolkit().getImage("highscoresPage_mainMenu_pressed.png");
		    prepareImage(highscoresPG, this);
		    mouseHit = "mainMenu";
		    repaint();
		}//end if
	    }else if(mouseHit.equals("mainMenu")){
		highscoresPG = getToolkit().getImage("highscoresPage.png");
		prepareImage(highscoresPG, this);
		mouseHit = "nothing";
		repaint();
	    }//end if
	}//end frame if statements
    }//end method
    private void mainMenuMouseMoved(MouseEvent e){
	//checks if ur playing any of the buttons, such as start game or instructions
	if(e.getX() > 150 && e.getX() < 360){
	    if(e.getY () > 100 && e.getY () < 150){//start game
		if(!mouseHit.equals("PlayGame")){
		    mouseHit = "PlayGame";//tells the game your mouse is touching the playgame text making it turn black
		    repaint();//if i put repaint after the brackets it is constantly refreshing casuing the page to flicker
		}
	    }//end if
	    else if(e.getY() > 200 && e.getY() < 250){//instructions
		if(!mouseHit.equals("instructions")){
		    mouseHit = "instructions";
		    repaint();
		}
	    }//end if
	    else if(e.getY() > 300 && e.getY() < 350){//highscores
		if(!mouseHit.equals("highscores")){
		    mouseHit = "highscores";
		    repaint();
		}
	    }//end if
	    else if(!mouseHit.equals("nothing")){//not touching anything
		mouseHit = "nothing";//tells the computer your mouse is not touching anything making it turn all the text back to blue
		repaint();
	    }//end second if
	}// end first if
    }
    
    //these are the methods that are called when the mouse is dragged, or entered, etc
    //even though im not using them i need them to be there or else it will give me an error
    public void mouseDragged (MouseEvent e){}
    public void mouseEntered (MouseEvent e){}
    public void mouseExited (MouseEvent e){}
    public void mouseClicked (MouseEvent e)
    {
	 if(frame.equals("mainMenu")){
	    if(e.getX() > 150 && e.getX() < 360){
		if(e.getY () > 100 && e.getY () < 150){//start game
		    frame = "getName";//makes it go to the getName page. (which is blank);
		    resetStats(); //resets the stats incase if this is your second time playing
		    getPlayersName(); //frame = "game";//goes to the game frame
		    init();//does the init() method
		    
		    //th.start();//starts the thread
		    repaint();//redraws everything
		}
		else if(e.getY() > 200 && e.getY() < 250){//instructions
		    frame = "instructions";//goes to the instructions frame
		    mouseTouching = "nothing";
		    repaint();
		}
		else if(e.getY() > 300 && e.getY() < 350){//highscores
		    frame = "highscores";//goes to the highscores frame
		    mouseHit = "nothing";
		    getHighscores();
		    repaint();
		}
	    }
	}//end frame if
	else if(frame.equals("game")){
	    for (int i = 0 ; i < shots.length ; i++)//loop that goes through everything inside the shots array
	    {
		if (shots [i] == null)//only creates a new bullet if there is no previous bullet there
		{
		    playerRotation = Math.atan2 (e.getY () - height / 2, e.getX () - width / 2) * 180 / Math.PI;
		    shots [i] = player.generateShot (playerRotation);
		    break;//breaks the loop because u dont want to add another bullet
		}
	    }
	}//end frame if
	else if(frame.equals("instructions")){
	    if(e.getX() > 121 && e.getX() < 330 && e.getY() > 400 && e.getY() < 430){
		frame = "mainMenu";
		mouseTouching = "nothing";
	    }
	}//end frame if
	else if(frame.equals("gameOver")){
	    if(e.getX() > 131 && e.getX() < 346 && e.getY() > 371 && e.getY() < 400){
		frame = "mainMenu";
		remove(name);
		remove(healthText);//removed the text field
		repaint();
	    }
	}//end frame if
	else if(frame.equals("highscores")){
	    if(e.getX() > 131 && e.getX() < 346 && e.getY() > 371 && e.getY() < 430){
		frame = "mainMenu";
		highscoresList.removeHighscores(this);
	    }
	}//end frame if
    }
    
    public void actionPerformed(ActionEvent evt){
	//it only runs this code when button is pressed
	if(evt.getSource() == pause){
	    if(gamePaused == false){//if game is not paused then it stops the thread
		gamePaused = true;
		th.stop(); 
	    }else{//if the game is paused then it starts the thread
		th = new Thread(this);
		th.start();
		gamePaused = false;
	    }
	}
	if(evt.getSource() == start){
	    frame = "game";//makes u go to the game frame
	    init();//does the init() method
	    remove(start);//removes the start button
	    
	    //sets name textfield 
	    name.setBounds( 50, 450, 140, 20);
	    //name.setForeground(Color.red);
	    name.setEditable(false);
	    name.setBackground(Color.white);
	    
	    //starts the thread
	    th = new Thread(this);
	    th.start();//starts the thread
		
	    //repaints everything. draws everything out
	    repaint();//redraws everything
	}
    }
    
    //this code runs every 40ms. it controls all the game movements. such as player, bullet, and enemy movements.
    public void run ()
    {
	Thread.currentThread ().setPriority (Thread.MIN_PRIORITY);
   
	while(true){
	    // do operations on shots in shots array
	    for (int i = 0 ; i < shots.length ; i++)
	    {
		if (shots [i] != null)
		{
		    // move shot
		    shots [i].moveShot (BULLET_SPEED);

		    if (shots [i].getYPos () < 0 || shots [i].getYPos () > height || shots [i].getXPos () < 0 || shots [i].getXPos () > width)
		    {
			//remove shot from array
			shots [i] = null;
		    }

		    for (int enemyIndex = 0 ; enemyIndex < enemies.length ; enemyIndex++)
		    {
			if (enemies [enemyIndex] != null && shots [i] != null)
			{
			    //System.out.println("enemy : " + enemies[0] + "shots: " + shots[0]);
			    //System.out.println("distance: " + distanceBetween(enemyIndex, i) + " i: " + i + " EI: " + enemyIndex);
			    if (distanceBetween (enemyIndex, i) < 30)
			    { //20 is the combined radius of the bullet and the enemy
				score += 2;;
				healthText.setText("health: " + health + " score: " + score);
				this.add(healthText);
				shots [i] = null;
				enemies [enemyIndex] = null;
			    }
			}
		    }
		    // test if shot is out
		}
	    }
	    
	   enemySpawnTimer++;//makes the timer increase by one
	    for (int counter = 0 ; counter < enemies.length ; counter++)//loop that goes through everything in the enemies array
	    {
		if (enemies [counter] != null){
		    enemies [counter].moveEnemy ();//moves the enemy only if the enemy exists
		    if(distanceBetween(counter, -1) < 40){//checks if the distance between you and the enemy is greater then 50
			health -= ENEMY_STRENGTH;// lowers ur health by the strength of the enemy
			enemies[counter] = null;
			healthText.setText("health: " + health + " score: " + score);
		    }
		}else if (enemySpawnTimer % 100 == 0){//if enemySpawnTimer is divisible by 100 then it creates a new enemy
		    numEnemiesSpawned++;//increases the number of enemies spawned at this instance
		    if( numEnemiesSpawned == maxNumToSpawn){
			//maxNumToSpawn = 1;//this is the number of enemies it will spawn at this instance
			enemySpawnTimer = 1;//makes it stop spawning
		    }
		    double rnd = Math.random () * 2 * Math.PI;
		    int x = (int) Math.round (width / 2 + Math.cos (rnd) * 360);
		    int y = (int) Math.round (height / 2 + Math.sin (rnd) * 360);
		    if (x < 0)
			x = 20;
		    enemies [counter] = new Enemy (x, y, (int)Math.round((Math.random()+1.5)*ENEMY_SPEED), width / 2, height / 2);
		}
	    }
	    //when the loop ends make numEnemiesSpawned = 0;
	    numEnemiesSpawned = 0;
	    
	    enemyStatsTimer++;
	    if(enemyStatsTimer%300 == 0){//if the enemyStatsTimer is divisible by 300 then it increases the number of enemies that will spawn and increases there strength
		maxNumToSpawn++;
		enemyStatsTimer = 1;
		
		if(ENEMY_STRENGTH < 20)
		    ENEMY_STRENGTH++;//increases the strength of the enemy
	     }
	     
	     if(health <= 0){//if health is 0 then 
		frame = "gameOver";//makes it go on the game over screen
		repaint();//redraws everything
		mouseHit = "nothing";//makes it go to the original picture not the picture of main menu being highlighted
		
		healthText.setBounds(width/2 - 70, height/2 - 10, 140, 20);//move the health button to the middle of the screen
		remove(pause);//remove the pause button
		
		try{
		    //makes the high scores
		    HighScores h = new HighScores();
		    h.writeScore(name.getText(), score);
		}catch(IOException ex){
		
		}
		th.stop();//stop the thread
	     }
	    
 
	    // repaint applet
	    repaint ();

	    try
	    {
		Thread.sleep (40);//stops running this thread for 40ms
	    }
	    catch (InterruptedException ex)
	    {
		// do nothing
	    }

	    Thread.currentThread ().setPriority (Thread.MAX_PRIORITY);
	}
    }
   
    //find the distance between enemy and bullet or player. if the bullet index is -1 then its the player. if its not then its the bullets element number
    public double distanceBetween (int enemyIndex, int bulletIndex)
    {
	int xDis, yDis;
	if(bulletIndex != -1){
	    xDis = enemies [enemyIndex].x_pos - (shots [bulletIndex].x_pos);
	    yDis = enemies [enemyIndex].y_pos - (shots [bulletIndex].y_pos);
	}else{
	    xDis = enemies [enemyIndex].x_pos - (width/2);
	    yDis = enemies [enemyIndex].y_pos - (height/2);
	}
	return Math.sqrt ((xDis * xDis) + (yDis * yDis));
    }
    public void getPlayersName(){
	//creates a new textfield and adds it to the stage
	name = new TextField("enter Your Name");
	name.setBounds(width/2 - 70,height/2 - 10, 140, 20);
	this.add(name);
	
	//creates a start button. which allows you to start your game
	start = new Button("start Game");
	start.setBounds(width/2 - 70,height/2  + 40, 140, 20);
	start.addActionListener(this);
	this.add(start);
    }
    public void resetStats(){
	enemyStatsTimer = 0; //im gonna use this as a timer. when it equals 100 then it will spawn an enemy
	enemySpawnTimer =0;//controls the spawning of the enemies
	BULLET_SPEED = 7; //the speed of the bullet
	ENEMY_SPEED = 3;//speed of the enemy
	health = 100;//health of the player
	score = 0;//score the user earned
	ENEMY_STRENGTH = 10;//strength of the enemy. increases by 1 each time. maximum 20
	numEnemiesSpawned = 0;//number of enemies that spawned
	maxNumToSpawn = 1;//num of enemies that will spawn in total at this time.
    }
    public void getHighscores(){
       try{
	    highscoresList = new HighScores();
	    highscoresList.getHighscores(this);
	}catch(IOException ex){
	    //do nothing
	}
    }
    
    public void paint (Graphics g)//paints everything on the applet
    {
	//draws the appropriate picture depending on which frame the game is on
	if(frame.equals("game")){
	    //g.fillRect (mx - 20, my - 20, 40, 40);
	    for (int i = 0 ; i < shots.length ; i++)
	    {
		if (shots [i] != null)
		{
		    shots [i].drawShot (g);//draws the shot if the shot exists
		}
	    }
	    for (int counter = 0 ; counter < enemies.length ; counter++)
	    {
		if (enemies [counter] != null)
		    enemies [counter].drawEnemy (g);//draws the enemy if the enemy object exists
	    }
	    player.drawPlayer (g);
	}else if(frame.equals("mainMenu")){
	    
	    //draws the rite picture depending on where the mouse is on the main menu
	    if(mouseHit.equals("nothing"))
		g.drawImage(menu, 0, -10, null);
	    else if(mouseHit.equals("PlayGame"))
		g.drawImage(menu1, 0, -10, null);
	    else if(mouseHit.equals("instructions"))
		g.drawImage(menu2, 0, -10, null);
	    else if(mouseHit.equals("highscores"))
		g.drawImage(menu3, 0, -10, null);
	}else if(frame.equals("instructions")){
	    g.drawImage(instructionPg, 0 ,-10, null);
	}else if(frame.equals("gameOver")){
	    g.drawImage(gameOver, 0, -10, null);
	}else if(frame.equals("highscores")){
	    g.drawImage(highscoresPG, 0, -10, null);
	}
    }
}
