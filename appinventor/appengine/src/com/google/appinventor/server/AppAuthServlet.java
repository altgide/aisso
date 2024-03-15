package com.google.appinventor.server;

import com.google.appinventor.server.flags.Flag;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import java.util.logging.Logger;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.appinventor.server.flags.Flag;

import com.google.appinventor.server.storage.StorageIo;
import com.google.appinventor.server.storage.StorageIoInstanceHolder;
import com.google.appinventor.server.storage.StoredData.PWData;

import com.google.appinventor.server.tokens.Token;
import com.google.appinventor.server.tokens.TokenException;
import com.google.appinventor.server.tokens.TokenProto;

import com.google.appinventor.server.util.PasswordHash;
import com.google.appinventor.server.util.UriBuilder;

import com.google.appinventor.shared.rpc.user.User;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import javax.servlet.http.Cookie;

@SuppressWarnings("unchecked")
@WebServlet("/AppAuthServlet")
public class AppAuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final StorageIo storageIo = StorageIoInstanceHolder.getInstance();
    //private static final UserService userService = UserServiceFactory.getUserService();
    private static final boolean DEBUG = Flag.createFlag("appinventor.debugging", false).get();

    //private final PolicyFactory sanitizer = new HtmlPolicyBuilder().allowElements("p").toFactory();
    private static final Logger LOG = Logger.getLogger(LoginServlet.class.getName());

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");

        PrintWriter out;

        //String page = getPage(req);

        req.setCharacterEncoding("UTF-8");

        try {
            req.getRequestDispatcher("/appAuth.jsp").forward(req, resp);
        } catch (ServletException e) {
            throw new IOException(e);
        }
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String iemail = request.getParameter("email");
        //String ipassword = request.getParameter("password");

        LOG.info("iemail = " + iemail);
        //LOG.info("ipassword = " + ipassword);

        BufferedReader input = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String queryString = input.readLine();

        PrintWriter out;

        OdeAuthFilter.UserInfo userInfo = OdeAuthFilter.getUserInfo(request);

        if (userInfo == null) {
            userInfo = new OdeAuthFilter.UserInfo();
        }

        /*
        if (queryString == null) {
            out = setCookieOutput(userInfo, response);
            out.println("queryString is null");
            return;
        }

        HashMap<String, String> params = getQueryMap(queryString);
        String page = getPage(request);
        String locale = params.get("locale");
        String repo = params.get("repo");
        String galleryId = params.get("galleryId");
        String newGalleryId = params.get("ng");
        String redirect = params.get("redirect");
        String autoload = params.get("autoload");
        */


        String locale ="";
        String redirect = "";

        if (locale == null) {
            locale = "en";
        }

        ResourceBundle bundle = ResourceBundle.getBundle("com/google/appinventor/server/loginmessages", new Locale(locale));

        if (DEBUG) {
            LOG.info("locale = " + locale + " bundle: " + new Locale(locale));
        }





        // JDBC URL, username, and password of PostgreSQL server
        String url = "jdbc:postgresql://localhost:5432/userdb";
        String username = "postgres";
        String password = "admin";

        // JDBC variables for opening and managing connection
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try  {

            // Register JDBC driver
            Class.forName("org.postgresql.Driver");

            // Open a connection
            connection = DriverManager.getConnection(url, username, password);

            //String sql = "SELECT * FROM aiuser WHERE email = ? AND REPLACE(password , CHR(13), '') = ?";

            String sql = "SELECT * FROM aiuser WHERE email = ? ";

            LOG.info("sql = " + sql);
            LOG.info("iemail2 = " + iemail);
            //LOG.info("ipassword2 = " + ipassword);

             preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, iemail);
            //preparedStatement.setString(2, ipassword);

            ResultSet resultSet = preparedStatement.executeQuery();

            User user = storageIo.getUserFromEmail(iemail);

            if (DEBUG) {
                LOG.info("userInfo = " + userInfo + " user = " + user);
            }
            userInfo.setUserId(user.getUserId());
            userInfo.setIsAdmin(user.getIsAdmin());
            String newCookie = userInfo.buildCookie(false);
            if (DEBUG) {
                LOG.info("newCookie = " + newCookie);
            }
            if (newCookie != null) {
                Cookie cook = new Cookie("AppInventor", newCookie);
                cook.setPath("/");
                response.addCookie(cook);
            }

            String uri = "/";
            if (redirect != null && !redirect.equals("")) {
                uri = redirect;
            }
            uri = new UriBuilder(uri)
                    .add("locale", locale)
                    //.add("autoload", autoload)
                    //.add("repo", repo)
                   // .add("ng", newGalleryId)
                    //.add("galleryId", galleryId)
                    .build();
            //response.sendRedirect(uri);

            if (resultSet.next()) {
                // Login successful
                //request.setAttribute("message", iemail + " login success");
                //request.getRequestDispatcher("/loginsuccess.jsp").forward(request, response);
                response.sendRedirect(uri);
            } else {
                // Invalid credentials
                request.setAttribute("errorMessage", "Invalid credentials");
                request.getRequestDispatcher("/appAuth.jsp").forward(request, response);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Finally block to close resources
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

 
 
  
}

