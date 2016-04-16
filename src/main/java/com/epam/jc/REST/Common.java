package com.epam.jc.REST;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created on 14/04/16.
 *
 * @author Vladislav Boboshko
 */
public class Common {
    public static String getRequestBody(HttpServletRequest request) {
        StringBuffer income = new StringBuffer();
        try(ServletInputStream br = request.getInputStream()) {
            Character symbol;
            while ((symbol = (char)br.read()) != 65535) {
                income.append(symbol);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return income.toString();
    }
}