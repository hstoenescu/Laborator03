package ro.pub.cs.systems.eim.lab03.testguiandroid;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.util.Log;

import java.util.Date;
import java.util.Random;

public class ProcessingThread extends Thread{

    public Context context = null;
    private boolean isRunning = true;

    public Random random = new Random();

    private double arithmeticMean;
    private double geomericMean;

    // context of the thread => calculate arithmetic and geometric mean
    public ProcessingThread (Context context, int firstNumber, int secondNumber) {
        this.context = context;

        arithmeticMean = (firstNumber + secondNumber) /2;
        geomericMean = Math.sqrt (firstNumber * secondNumber);
    }

    @Override
    public void run () {
        Log.d("[ProcessingThread]", "Thread has started!");
        while (isRunning) {
            sendMessage();
            sleep();
        }
        Log.d("[ProcessingThread]","Thread has stopped!");
    }

    // implement sendMessage()
    private void sendMessage() {
        // create new intent in thread
        Intent intent = new Intent();
        intent.setAction(Constants.actionTypes[random.nextInt(Constants.actionTypes.length)]);
        intent.putExtra("message", new Date(System.currentTimeMillis()) + " " + arithmeticMean + " " + geomericMean);
        context.sendBroadcast(intent);
    }

    private void sleep () {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
