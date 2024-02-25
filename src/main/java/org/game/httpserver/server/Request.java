package org.game.httpserver.server;

import org.game.httpserver.http.Method;

import java.util.ArrayList;
import java.util.List;

public class Request {
    private Method method;
    private String urlContent;
    private String pathname;
    private List<String> pathParts;
    private String params;
    private HeaderMap headerMap =  new HeaderMap();
    private String body;

    public String getServiceRoute(){
        if (this.pathParts == null ||
                this.pathParts.isEmpty()) {
            return null;
        }
        //ein kleiner hack für die längeren paths
        if(this.pathParts.size()>1) {
            if (!this.pathParts.get(1).startsWith(" ")) {
                if (this.pathParts.get(0).equals("tradings")) {
                    return '/' + this.pathParts.get(0) + "/{tradingdealid}";
                }
                if (this.pathParts.get(0).equals("transactions")) {
                    return '/' + this.pathParts.get(0) + "/packages";
                }
                if (this.pathParts.get(0).equals("users")) {
                    return '/' + this.pathParts.get(0) + "/{username}";
                }
            }
        }
        return '/' + this.pathParts.get(0);
    }

    public String getUrlContent(){
        return this.urlContent;
    }

    public void setUrlContent(String urlContent) {
        this.urlContent = urlContent;
        Boolean hasParams = urlContent.indexOf("?") != -1;

        if (hasParams) {
            String[] pathParts =  urlContent.split("\\?");
            this.setPathname(pathParts[0]);
            this.setParams(pathParts[1]);
        }
        else
        {
            this.setPathname(urlContent);
            this.setParams(null);
        }
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPathname() {
        return pathname;
    }


    public void setPathname(String pathname) {
        this.pathname = pathname;
//        //es scheint nur mit trailing / am pfadende zu gehn, wsl wg url encoding, aber der hack sollt derweil passen
//        // Check if the pathname ends with "/"
//        if (!pathname.endsWith("/")) {
//            // If it doesn't, append a "/" to the pathname
//            pathname += "/";
//        }
//
//        // Find the index of the first space or encoded space ("%20")
//        int index = pathname.indexOf(" ");
//        if (index == -1) {
//            index = pathname.indexOf("%20");
//        }
//
//        // If a space or encoded space is found, insert a "/" before it
//        if (index != -1) {
//            pathname = pathname.substring(0, index) + "/" + pathname.substring(index);
//        }

        String[] stringParts = pathname.split("/");
        this.pathParts = new ArrayList<>();
        for (String part :stringParts)
        {
                if (part != null && part.length() > 0) {
                    this.pathParts.add(part);
                }
        }
    }
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public HeaderMap getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(HeaderMap headerMap) {
        this.headerMap = headerMap;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getPathParts() {
        return pathParts;
    }

    public void setPathParts(List<String> pathParts) {
        this.pathParts = pathParts;
    }
}
