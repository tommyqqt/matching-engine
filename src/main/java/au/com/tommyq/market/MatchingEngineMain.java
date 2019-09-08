package au.com.tommyq.market;

import au.com.tommyq.market.cli.MatchingEngineCLI;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class MatchingEngineMain {

    public static void main(String[] args) {
        //Loading instruments configuration from properties file
        final Config config = new Config();
        config.loadConfig();
        System.out.println();

        final Queue<String> queue = new ConcurrentLinkedQueue<>();
        final MatchingEngineImpl market = new MatchingEngineImpl(config.instrumentConfigs(), newSingleThreadExecutor(), queue::offer);

        final MatchingEngineCLI cli = new MatchingEngineCLI(market, queue);
        market.start();
        cli.start();
        market.stop();
    }

}
