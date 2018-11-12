package br.unip.pandora;

import br.unip.pandora.box.Pandora;
import br.unip.pandora.engine.Engine;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Launcher {

    private static final File CONFIG_FILE = new File("config.properties");
    
    public static void main(String[] args) {
	Properties p = readConfig();
	new Engine(new Pandora(p), "PANDORA'S BOX", Integer.parseInt((String)p.get("zoom"))).start();
    }
    
    private static Properties readConfig(){
	Properties p = new Properties();
	try(FileInputStream in = new FileInputStream(CONFIG_FILE)){
	    p.load(in);
	    return p;
	} catch (IOException ex) {
	    p.setProperty("zoom", "2");
	    p.setProperty("width", "640");
	    p.setProperty("height", "480");
	    p.setProperty("dayHours", "24");
	    p.setProperty("yearDays", "365");
	    p.setProperty("pauseKey", String.valueOf(KeyEvent.VK_SPACE));
	    p.setProperty("speedUpKey", String.valueOf(KeyEvent.VK_PAGE_UP));
	    p.setProperty("speedDownKey", String.valueOf(KeyEvent.VK_PAGE_DOWN));
	    p.setProperty("volumeUpKey", String.valueOf(KeyEvent.VK_ADD));
	    p.setProperty("volumeDownKey", String.valueOf(KeyEvent.VK_SUBTRACT));
	    p.setProperty("saveQValueKey", String.valueOf(KeyEvent.VK_Q));
	    p.setProperty("followKey", String.valueOf(KeyEvent.VK_F));
	    p.setProperty("worldRows", "100");
	    p.setProperty("worldCols", "100");
	    saveConfig(p);
	    return p;
	}
    }
    
    private static void saveConfig(Properties p){
	try(FileOutputStream out = new FileOutputStream(CONFIG_FILE)){
	    p.store(out, null);
	} catch (IOException ex) {}
    }
    
}
