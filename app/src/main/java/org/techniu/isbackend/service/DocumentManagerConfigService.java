package org.techniu.isbackend.service;

import java.util.HashMap;
import java.util.Map;

public interface DocumentManagerConfigService {

    /**
     * Returns the configuration of the document management module
     *
     * @return  HashMap
     */
    HashMap getConfiguration();


    /**
     * update the configuration parameters
     *
     * @param  data
     *
     */
    void updateConfiguration(HashMap data);


}
