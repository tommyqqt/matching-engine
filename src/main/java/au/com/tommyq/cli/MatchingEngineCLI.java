/*
 * MIT License
 *
 * Copyright (c) 2019 tommyqqt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package au.com.tommyq.cli;

import au.com.tommyq.MatchingEngine;
import au.com.tommyq.engine.Side;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;

public class MatchingEngineCLI {
    private static final Logger logger = LoggerFactory.getLogger(MatchingEngineCLI.class);

    private static final String command_delimiter = "\\s+";

    private static final String EXIT = "exit";
    private static final String DISPLAY = "Display";
    private static final String SNAPSHOT = "Snapshot";

    private final Queue<String> queue;
    private final MatchingEngine matchingEngine;

    public MatchingEngineCLI(final MatchingEngine matchingEngine, final Queue<String> queue) {
        this.matchingEngine = Objects.requireNonNull(matchingEngine);
        this.queue = Objects.requireNonNull(queue);
    }

    public void start() {
        try(final Scanner in = new Scanner(System.in)) {
            String command = "";
            System.out.println("Place your order");
            for(;;){
                System.out.print("Input Command: ");
                try {
                    command = in.nextLine();
                    if (EXIT.equalsIgnoreCase(command)) {
                        return;
                    }
                    processCommandString(command);
                    pollForResponses();
                } catch (Exception e) {
                    logger.error("Error processing command", e);
                    System.out.println("Bad command: " + command);
                }
            }
        }
    }

    private void processCommandString(final String commandStr) {
        if(Strings.isBlank(commandStr)){
            return;
        }
        final String[] tokens = commandStr.trim().split(command_delimiter);
        if(tokens.length == 2){
            if(tokens[0].equals(DISPLAY)){
                matchingEngine.display(tokens[1]);
            } else if (tokens[0].equals(SNAPSHOT)){
                processSnapshotCommand(tokens);
            } else {
                System.out.println("Bad command: " + commandStr);
            }
        } else if (tokens.length == 3) {
            if(tokens[0].equals(SNAPSHOT)){
                processSnapshotCommand(tokens);
            } else {
                System.out.println("Bad command: " + commandStr);
            }
        } else if (tokens.length == 6){
            final String user = tokens[0];
            final Side side = Side.valueOf(tokens[1]);
            final int qty = Integer.parseInt(tokens[2]);
            final String instrument = tokens[3];
            final double price = Double.parseDouble(tokens[5].replace("$",""));
            switch(side){
                case BID:
                    matchingEngine.placeBid(user, instrument, qty, price);
                    break;
                case OFFER:
                    matchingEngine.placeOffer(user, instrument, qty, price);
                    break;
            }
        } else {
            System.out.println("Bad command: " + commandStr);
        }
    }

    private void processSnapshotCommand(final String[] tokens){
        final String instrument = tokens[1];
        final int depth;
        if(tokens.length == 2){
            depth = -1;
        } else {
            depth = Integer.parseInt(tokens[2]);
        }
        matchingEngine.requestOrderBookSnapshot(instrument, depth);
    }

    private void pollForResponses(){
        final int maxRetry = 100;
        int retry = 0;
        while(!Thread.currentThread().isInterrupted()){
            final String commandResult = queue.poll();
            if(Strings.isNotBlank(commandResult)){
                System.out.println(commandResult);
            } else {
                try {
                    //wait 1ms and retry
                    Thread.sleep(1);
                    if(retry == maxRetry){
                        return;
                    }
                    retry++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
