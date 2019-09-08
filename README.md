# CLOB-Matching-Engine
### A reference implementation in Java of a central limit order book - matching engine that is low latency and low gc 

Assumptions in this implementation of this CLOB-Matching Engine:
- Simple interactive command line interface to manually place orders and see execution reports
- Price time priority (order of arrival) order matching
- Each limit order book handles one specific instrument (ccypair)
- Tick value = $0.01
- For each instrument, its prices move within a minimum and maximum pre-configured values
- No auto adjust of min and max price
- Uncrossed orders are appended to the resting orders list of the respective price levels
- If an order is partially crossed, the order will be appended to the respective level with the unfilled quantity (FAS)

1. Build
    - cd matching-engine
    - mvn clean install package

2. Run the app against external properties file
    - cd matching-engine
    - java -DconfigFile=./external_config.properties -jar ./target/matching-engine-0.0.1-SNAPSHOT.jar

      InstrumentConfig{name='USDCAD', minPrice=2.8, maxPrice=3.1}    
      InstrumentConfig{name='EURUSD', minPrice=1.7, maxPrice=2.1}    
      InstrumentConfig{name='GBPEUR', minPrice=1.05, maxPrice=2.2}    

      Place your order
      
      Input Command: AbcCapital OFFER 10 USDCAD @ $2.99  
      Input Command: SuperInvestment BID 5 USDCAD @ $2.98  
      Input Command: Display USDCAD  
      5 $2.98 | $2.99 10  
      Input Command: OneBank BID 10 USDCAD @ $2.97  
      Input Command: TravelExchanger OFFER 7 USDCAD @ $2.97  
      SuperInvestment bought 5 USDCAD from TravelExchanger @ $2.98  
      OneBank bought 2 USDCAD from TravelExchanger @ $2.97  
      Input Command: Display USDCAD  
      8 $2.97 | $2.99 10  
      Input Command: AbcCapital BID 1 GBPEUR @ $1.1  
      Input Command: AbcCapital BID 2 GBPEUR @ $1.2  
      Input Command: AbcCapital BID 2 GBPEUR @ $1.3  
      Input Command: AbcCapital BID 5 GBPEUR @ $1.3  
      Input Command: AbcCapital OFFER 2 GBPEUR @ $1.5  
      Input Command: AbcCapital OFFER 17 GBPEUR @ $1.5  
      Input Command: AbcCapital OFFER 16 GBPEUR @ $1.6  
      Input Command: Display GBPEUR  
      7 $1.30 | $1.50 19  
      Input Command: OneBank OFFER 11 GBPEUR @ $1.15  
      AbcCapital bought 2 GBPEUR from OneBank @ $1.30  
      AbcCapital bought 5 GBPEUR from OneBank @ $1.30  
      AbcCapital bought 2 GBPEUR from OneBank @ $1.20  
      Input Command: Display GBPEUR  
      1 $1.10 | $1.15 2  
      Input Command: exit  
      Shutting down  
       
