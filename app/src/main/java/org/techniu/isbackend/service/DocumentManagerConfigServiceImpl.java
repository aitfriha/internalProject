package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.service.DocumentManagerConfigService;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class DocumentManagerConfigServiceImpl implements DocumentManagerConfigService {

    private final String CONFIGURATION_FILE = "DOCUMENT MANAGER CONFIG" + System.getProperty("file.separator") + "config.properties";

    public DocumentManagerConfigServiceImpl() {
    }

    @Override
    public HashMap getConfiguration() {
        HashMap data = new HashMap();
        try {
            Properties config = new Properties();
            try (InputStream configInput = new FileInputStream(CONFIGURATION_FILE)) {
                config.load(configInput);
                Enumeration<Object> keys = config.keys();
                while (keys.hasMoreElements()) {
                    Object key = keys.nextElement();
                    switch ((String) key) {
                        case "nuxeourl": {
                            data.put("nuxeourl", config.getProperty("nuxeourl"));
                            break;
                        }
                        case "user": {
                            data.put("user", config.getProperty("user"));
                            break;
                        }
                        case "password": {
                            data.put("password", config.getProperty("password"));
                            break;
                        }
                        case "dominio": {
                            data.put("dominio", config.getProperty("dominio"));
                            break;
                        }
                        case "onlyoffice": {
                            data.put("onlyoffice", config.getProperty("onlyoffice"));
                            break;
                        }
                        case "storage": {
                            data.put("storage", Double.parseDouble(config.getProperty("storage")));
                            break;
                        }
                        case "storageuser": {
                            data.put("storageuser", Double.parseDouble(config.getProperty("storageuser")));
                            break;
                        }
                        case "configurado": {
                            data.put("configurado", Boolean.valueOf(config.getProperty("configurado")));
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DocumentManagerConfigServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }


    @Override
    public void updateConfiguration(HashMap data) {
        if (new File(CONFIGURATION_FILE).exists()) {
            try {
                Properties config = new Properties();
                OutputStream output = new FileOutputStream(CONFIGURATION_FILE);
                String nuxeourl = (String) data.get("nuxeourl");
                config.setProperty("nuxeourl", nuxeourl);
                String user = (String) data.get("user");
                config.setProperty("user", user);
                String password = (String) data.get("password");
                config.setProperty("password", password);
                String dominio = (String) data.get("dominio");
                config.setProperty("dominio", dominio);
                String onlyoffice = (String) data.get("onlyoffice");
                config.setProperty("onlyoffice", onlyoffice);
                Double storage = Double.parseDouble((String)data.get("storage"));
                config.setProperty("storage", String.valueOf(storage));
                Double storageuser = Double.parseDouble((String)data.get("storageuser"));
                config.setProperty("storageuser", String.valueOf(storageuser));
                config.setProperty("configurado", "true");

                config.store(output, null);

                //OCULTAR LAS CARPETAS DEL SISTEMA
                   /* Path path = new File(ruta_directorio).toPath();
                    Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);*/

            } catch (IOException ex) {
                Logger.getLogger(DocumentManagerConfigServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //No existe el archivo de configuracion o ha sido movido
            throw exception(ExceptionType.CONFIG_FILE_NOT_FOUND);
        }
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.DocumentManagerConfig, exceptionType, args);
    }

}