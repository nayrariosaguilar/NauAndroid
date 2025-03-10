package org.proven.game2d24;

public class ThreadGame extends Thread {
    PilotesView pilotesView;

    public ThreadGame(PilotesView pilotesView) {
        this.pilotesView = pilotesView;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                sleep(200);
                pilotesView.update();
                pilotesView.checkCollisions();
                pilotesView.postInvalidate();
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }
    }
}
