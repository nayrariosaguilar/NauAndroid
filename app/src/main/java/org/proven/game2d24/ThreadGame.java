package org.proven.game2d24;

public class ThreadGame extends Thread {
    PilotesView pilotesView;

    public static final int delayForGame = 2000;

    public ThreadGame(PilotesView pilotesView) {
        this.pilotesView = pilotesView;
    }

    @Override
    public void run() {
        super.run();
        try {
            sleep(delayForGame);
            while (true) {
                sleep(30);
                pilotesView.update();
                pilotesView.checkCollisions();
                pilotesView.postInvalidate();
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }
        //prueba numero 1
    }
}