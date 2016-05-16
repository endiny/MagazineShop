package com.epam.jc.Common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created on 14/04/16.
 *
 * @author Vladislav Boboshko
 */
public class RequestService {
    private static final Logger logger = LogManager.getLogger(RequestService.class.getName());
    public static String getRequestBody(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ignored) {} //UTF-8 exists anywhere, i suppose
        StringBuffer income = new StringBuffer();
        try(ServletInputStream br = request.getInputStream()) {
            Character symbol;
            while ((symbol = (char)br.read()) != 65535) {
                income.append(symbol);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            return "";
        }
        return income.toString();
    }
}
