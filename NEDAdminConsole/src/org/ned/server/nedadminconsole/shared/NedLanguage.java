package org.ned.server.nedadminconsole.shared;

import java.io.Serializable;

public class NedLanguage implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -2006499114898693646L;

    public String name;
    public String locale_string;
    public String translation_file;

    public NedLanguage()
    {
        
    }
    
    public NedLanguage(String name, String locale_string, String translation_file)
    {
        this.name = name;
        this.locale_string = locale_string;
        this.translation_file = translation_file;
    }

}
