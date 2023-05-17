package utilities;

import framework.utilities.Debug;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class Web {
    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean openWebpage(URL url) {
        try {
            boolean success = openWebpage(url.toURI());
            Debug.Log("Opened webpage in browser");
            return success;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Debug.Log("Webpage open failed");
        return false;
    }
}
