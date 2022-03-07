package com.ableto.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private LogBuilder logBuilder = new LogBuilder();
    private static ConfigManager manager;
	private static final Properties prop = new Properties();
    protected String env;

	private ConfigManager() throws IOException {

        String gcpUsername = System.getenv("uid");
        String gcpPassword = System.getenv("pwd");

        this.setProperty("gcpUsername", gcpUsername);
        this.setProperty("gcpPassword", gcpPassword);
    }

	public static ConfigManager getInstance() {
		if (manager == null) {
			synchronized (ConfigManager.class) {
				try {
					manager = new ConfigManager();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return manager;	
	}
	
	public String getString(String key) {
		return System.getProperty(key, prop.getProperty(key));
	}

	public static String readConfig(String url){
        return getInstance().getString(url);
    }

	public Object setProperty(String key, String value) {
		return prop.setProperty(key, value);
    }

    public void initializeProperties(String env) {
	    String path = null;

        logBuilder.info("env value is--->" + env);
        switch (env) {
            case "dev":
                path = "./src/resources/dev.properties";
                break;
            case "rc":
                path = "./src/resources/rc.properties";
                break;
            case "stg":
                path = "./src/resources/stg.properties";
                break;
            case "prod":
                path = "./src/resources/prod.properties";
                break;
            default:
                path = "./src/resources/stg.properties";
                logBuilder.info("Please pass a valid env value----> " + env);
                break;
        }

        try (FileInputStream stream = new FileInputStream(path)) {
            prop.load(stream);    
		} catch (IOException e) {
			e.printStackTrace();
			logBuilder.error("Please pass a valid env value----> " + e.getMessage());
        } 
        
		logBuilder.info("Environment-based properties initialized");
	}
}

