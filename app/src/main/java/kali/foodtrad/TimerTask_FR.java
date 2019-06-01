package kali.foodtrad;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by elliotching on 15-Mar-17.
 */

class TimerTask_FR extends TimerTask {

    private final Handler handler;
    private final Timer timer;
    private final int endingTime;
    private int runningTime;
    private final int intervalTime;
    private int time = 0;
    private final TimerTask timerTask = this;

    private InterfaceMyTimerTask myTask;

    TimerTask_FR(int doFor_Seconds, InterfaceMyTimerTask myTask) {
        handler = new Handler();
        timer = new Timer();
        runningTime = 0;
        endingTime = doFor_Seconds * 1000;
        intervalTime = 1000;

        this.myTask = myTask;

        timer.schedule(this, 0, intervalTime);
    }

    @Override
    public void run() {
        handler.post(new FRRunnable());
    }


    class FRRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Log.d(timerTask.getClass().getSimpleName(), "timer counting = "+runningTime);
                myTask.doTask();

                if(( runningTime % 1000 ) == 0){
                    myTask.secondsPassed(runningTime / 1000);
                }

                runningTime += intervalTime;
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
        }
    }

    @Override
    public boolean cancel() {
        return super.cancel();
    }

    private void checkProgDialog(){
//        if (AsyncRequestData.progressDialog.isShowing()) {
//            if (time >= 100 && time < 3000 && AsyncRequestData.progressDialog.isShowing()) {
//                AsyncRequestData.progressDialog.dismiss();
//                AsyncRequestData.cancelRequest();
//            } else {
////                        startCountPressAgainExit(3);
//                new TimerTask_FR(3).start();
//            }
//        }
    }

    private void runSplashwithDelayed(Context c, int milliseconds) {
        final int splashTimeOut = milliseconds;
        final Context context = c;
        Thread splashThread = new Thread() {
            int wait = 0;

            @Override
            public void run() {
                try {
                    super.run();
                    while (wait < splashTimeOut) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                } finally {

                }
            }
        };
        splashThread.start();
    }
}