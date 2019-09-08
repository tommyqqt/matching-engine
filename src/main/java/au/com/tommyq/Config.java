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

package au.com.tommyq;

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
