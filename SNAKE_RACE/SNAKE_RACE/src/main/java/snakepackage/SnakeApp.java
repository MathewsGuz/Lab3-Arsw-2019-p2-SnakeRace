package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author jd-
 *
 */
public class SnakeApp {

    private static SnakeApp app;
    public final static Object it =new Object();
    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private static Board board;
    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];
    boolean isPause=false;
    boolean gameOn=false;
    ArrayList<Integer> firstDead=new ArrayList<Integer>();

    public SnakeApp() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();
        
        
        frame.add(board,BorderLayout.CENTER);
        
        JPanel actionsBPabel=new JPanel();
        JButton resume=new JButton("Resume ");
        JButton pause=new JButton("Pause ");
        final JButton play=new JButton("Play ");
        actionsBPabel.setLayout(new FlowLayout());
        actionsBPabel.add(play);
        actionsBPabel.add(pause);
        actionsBPabel.add(resume);
        frame.add(actionsBPabel,BorderLayout.SOUTH);
        
        resume.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    System.out.print("resumiendo");
                    isPause=false;
            }  
        }); 
        
        play.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    System.out.print("Play");
                    gameOn=true;
                    play.setEnabled(false);
            }  
       });
        
        pause.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    
                    isPause=true;
                    System.out.print("paused: "+isPause);
                    System.out.println("La primera serpiente en morir fue la numero: "+firstDead.get(0));
            }  
        });
    }

    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {
        
        for (int i = 0; i != MAX_THREADS; i++) {
            
            snakes[i] = new Snake(i + 1, spawn[i], i + 1);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
            thread[i].start();
        }

        frame.setVisible(true);

        
        while (true) {
            if(gameOn){
                if(!isPause){
                    //                    synchronized(it){
    //                        it.notifyAll();
                    for (int i = 0; i != MAX_THREADS; i++) {
                        //no se cual hilo detener , y falta reanudarlos
                        snakes[i].play();

    //                    thread[i].
    //                    }
                        }                   
                    int x = 0;
                    for (int i = 0; i != MAX_THREADS; i++) {
                        if (snakes[i].isSnakeEnd() == true) {
                            x++;
                            firstDead.add(i+1);
                        }
                    }
                    if (x == MAX_THREADS) {
                        break;
                    }

                }else{

                    for (int i = 0; i != MAX_THREADS; i++) {
                        //no se cual hilo detener , y falta reanudarlos
                        snakes[i].pause();
    //                    thread[i].
                    }
                    
                }
            }else{
                System.out.println("El juego no a iniciado");    
                for (int i = 0; i != MAX_THREADS; i++) {
                        snakes[i].pause();
                    } 
            }
        }


        System.out.println("Thread (snake) status:");
        for (int i = 0; i != MAX_THREADS; i++) {
            System.out.println("["+i+"] :"+thread[i].getState());
        }
        

    }

    public static SnakeApp getApp() {
        return app;
    }

}
