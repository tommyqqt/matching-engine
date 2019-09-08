package au.com.tommyq.market.cli;

import au.com.tommyq.market.MatchingEngine;
import au.com.tommyq.market.engine.Side;
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
        final String[] tokens = commandStr.split(command_delimiter);
        if(tokens.length == 2){
            if(tokens[0].equals(DISPLAY)){
                matchingEngine.display(tokens[1]);
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

    private void pollForResponses(){
        final int maxRetry = 10;
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
