package au.com.tommyq.market;

import org.apache.logging.log4j.util.Strings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Config {
    private List<InstrumentConfig> instrumentConfigs = null;
    public static final String CONFIG_FILE_PATH = "configFile";

    private static final String defaultPropFile = "config.properties";

    public void loadConfig(){
        final String configPath = System.getProperty(CONFIG_FILE_PATH);
        final Properties props = new Properties();

        if(Strings.isNotBlank(configPath)){
            try(final InputStream input = new FileInputStream(configPath)){
                props.load(input);
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            try(final InputStream input = getClass().getClassLoader().getResourceAsStream(defaultPropFile)){
                props.load(input);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        loadInstrumentConfig((String)props.get("instruments"));
        instrumentConfigs.stream().forEach(System.out::println);
    }

    private void loadInstrumentConfig(final String instrumentConfigString){
        String[] instrumentStr = instrumentConfigString.split(";");
        instrumentConfigs = Arrays.stream(instrumentStr).map(s -> {
            String[] tokens = s.split(",");
            return new InstrumentConfig(tokens[0],
                    Double.parseDouble(tokens[1]),
                    Double.parseDouble(tokens[2]));
        }).collect(Collectors.toList());
    }

    public List<InstrumentConfig> instrumentConfigs() {
        return this.instrumentConfigs;
    }
}
