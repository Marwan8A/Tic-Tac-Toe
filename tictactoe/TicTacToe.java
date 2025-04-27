import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game.
 *
 * 
 * @author Marwan Alakhras
 * @version March 26, 2025
 * 
 */

public class TicTacToe extends JFrame implements ActionListener
{
   public static final String PLAYER_X = "X"; // player using "X"
   public static final String PLAYER_O = "O"; // player using "O"
   public static final String EMPTY = " ";  // empty cell
   public static final String TIE = "T"; // game ended in a tie
 
   private String player;   // current player (PLAYER_X or PLAYER_O)

   private String winner;   // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress

   private int numFreeSquares; // number of squares still free
   
   private JButton[][] board; // 3x3 array of JButtons representing the board

   private JFrame frame; //Game window

   private JMenuBar menuBar;//menubar part of the main frame

   private JMenu gameMenu;//Game button part of the menu bar

   private JMenuItem newGameItem;//menu button part of the gameMenu
   private JMenuItem resetScoreItem; // menu button part of the gameMenu. Resets the scoreboard.
   private JMenuItem quitItem;//menu button part of the gameMenu

   private JTextField gameStatus; //displays the current status of the game

   private int xWins = 0; //number of wins of player X
   private int oWins = 0; //number of wins of player O
   private int ties = 0; //number of ties
   private JLabel scoreBoard; //Displays the score between the 2 players



   /** 
    * Constructs a new Tic-Tac-Toe GUI.
    */
   public TicTacToe()
   {
       //Initializing board and its buttons
       board = new JButton[3][3];
       JPanel boardPanel = new JPanel(new GridLayout(3, 3));

       for (int i = 0; i < 3; i++){
           for (int j = 0; j < 3; j++){
               board[i][j] = new JButton(EMPTY);
               board[i][j].setFont(new Font(null, Font.BOLD, 50 ));
               board[i][j].setFocusPainted(false);
               board[i][j].addActionListener(this);
               boardPanel.add(board[i][j]);
           }
       }




      //Initializing the menu
      menuBar = new JMenuBar();
      gameMenu = new JMenu("Game");
      newGameItem = new JMenuItem("New Game");
      resetScoreItem = new JMenuItem("Reset Score");
      quitItem = new JMenuItem("Quit");

      newGameItem.addActionListener(this);
      resetScoreItem.addActionListener(this);
      quitItem.addActionListener(this);

      gameMenu.add(newGameItem);
      gameMenu.add(resetScoreItem);
      gameMenu.add(quitItem);
      menuBar.add(gameMenu);


      //Initializing the status display to display current game status
      gameStatus = new JTextField(player +"'s turn.");
      gameStatus.setEditable(false);
      gameStatus.setHorizontalAlignment(JTextField.CENTER);


       //Initializing the main frame
       frame = new JFrame("Tic Tac Toe");
       frame.setSize(500,500);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setResizable(false);
       frame.setLayout(new BorderLayout());
       frame.setJMenuBar(menuBar);
       frame.add(gameStatus, BorderLayout.NORTH);
       frame.add(boardPanel, BorderLayout.CENTER);
       frame.setVisible(true);
       clearBoard();

      //Initializing the keyboard shortcuts
      final int  SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(); //to save typing
      newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK)); // ctrl-N to start new game
      resetScoreItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK)); // ctrl-R to reset scoreboard
      quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK)); // ctrl-N to start new game


      //Initializing the scoreboard
       scoreBoard = new JLabel("X wins: 0 | O wins: 0 | Ties: 0", SwingConstants.CENTER);
       frame.add(scoreBoard, BorderLayout.SOUTH);

   }

   /**
    * Sets everything up for a new game.  Marks all squares in the Tic Tac Toe board as empty,
    * and indicates no winner yet, 9 free squares and the current player is player X. Updates
    * the game status display for a new game. Does not reset the score.
    */
   private void clearBoard()
   {
      for (int i = 0; i < 3; i++) {
         for (int j = 0; j < 3; j++) {
            board[i][j].setText(EMPTY);
            board[i][j].setBackground(null);
         }
      }
      winner = EMPTY;
      numFreeSquares = 9;
      player = PLAYER_X;     // Player X always has the first turn.
      gameStatus.setText(player +"'s turn.");
   }


   /**
    * Returns true if filling the given square gives us a winner, and false
    * otherwise.
    *
    * @param  row of square just set
    * @param  col of square just set
    * 
    * @return true if we have a winner, false otherwise
    */
   private boolean haveWinner(int row, int col) 
   {
       // unless at least 5 squares have been filled, we don't need to go any further
       // (the earliest we can have a winner is after player X's 3rd move).

       if (numFreeSquares>4) return false;

       // Note: We don't need to check all rows, columns, and diagonals, only those
       // that contain the latest filled square.  We know that we have a winner 
       // if all 3 squares are the same, as they can't all be blank (as the latest
       // filled square is one of them).

       // check row "row"
       if ( board[row][0].getText().equals(board[row][1].getText()) &&
            board[row][0].getText().equals(board[row][2].getText())) return true;
       
       // check column "col"
       if ( board[0][col].getText().equals(board[1][col].getText()) &&
            board[0][col].getText().equals(board[2][col].getText()))  return true;

       // if row=col check one diagonal
       if (row==col)
          if ( board[0][0].getText().equals(board[1][1].getText()) &&
               board[0][0].getText().equals(board[2][2].getText())) return true;

       // if row=2-col check other diagonal
       if (row==2-col)
          if ( board[0][2].getText().equals(board[1][1].getText()) &&
               board[0][2].getText().equals(board[2][0].getText())) return true;

       // no winner yet
       return false;
   }


    /**
     * This action listener is called when the user clicks on any of the GUI's buttons.
     *
     * @param e the event to be processed
     */
    public void actionPerformed(ActionEvent e){
       Object o = e.getSource();

       if(o instanceof JButton){ //check if it's a JButton

           if(!winner.equals(EMPTY)){ //game over. Does nothing
               return;
           }

           JButton button = (JButton) o;

           for (int i = 0; i < 3; i++){
               for (int j = 0; j < 3; j++){
                   if(button == board[i][j] && board[i][j].getText().equals(EMPTY)){
                       board[i][j].setText(player); //player takes turn


                       // Set color based on CURRENT player
                       if(player.equals(PLAYER_X)){
                           board[i][j].setBackground(Color.RED);
                       }
                       else{
                           board[i][j].setBackground(Color.BLUE);
                       }



                       numFreeSquares--;

                       if(haveWinner(i,j)){ //there is a winner
                           winner = player;
                           gameStatus.setText(player +" wins!!!");

                           if(player.equals(PLAYER_X)){//adjust scoreboard
                               xWins++;
                           }
                           else{
                               oWins++;
                           }
                           updateScoreBoard();


                       }
                       else if(numFreeSquares == 0){// game over. Tie
                           winner = TIE;
                           gameStatus.setText("It's a tie");
                           ties++;
                           updateScoreBoard();


                       }
                       else{
                           //pass turn to opponent
                           if(player.equals(PLAYER_X)){
                               player = PLAYER_O;
                               gameStatus.setText(player + "'s turn");
                           }
                           else{
                               player = PLAYER_X;
                               gameStatus.setText(player + "'s turn");
                           }

                       }
                       return;
                   }





               }
           }

       }
       else{ // it's a JMenuItem
           JMenuItem item = (JMenuItem) o;

           if(item == newGameItem){ //creates new game
               clearBoard();
           }
           else if(item == quitItem){
               System.exit(0); //quits
           }
           else if(item == resetScoreItem){
               xWins = 0;
               oWins = 0;
               ties = 0;
               updateScoreBoard();
           }
       }

   }

    /**
     * updates the scoreboard with the current values.
     */
    private void updateScoreBoard() {
        scoreBoard.setText("X wins: " + xWins + " | O wins: " + oWins + " | Ties: " + ties);
    }


    /**
     * Creates an instance of the TicTacToe class, which sets up the game and the GUI.
     * @param args The command line arguments
     */
   public static void main (String[] args){
       new TicTacToe(); //Creates TicTacToe game
   }


}

