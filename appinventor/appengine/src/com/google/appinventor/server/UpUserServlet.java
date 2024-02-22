
package com.google.appinventor.server;

import com.google.appinventor.server.flags.Flag;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

@SuppressWarnings("unchecked")
@WebServlet("/UpUserServlet")
@MultipartConfig
public class UpUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final boolean DEBUG = Flag.createFlag("appinventor.debugging", false).get();

    private static final Logger LOG = Logger.getLogger(LoginServlet.class.getName());

    private static final UserService userService = UserServiceFactory.getUserService();


    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");

        PrintWriter out;

        String [] components = req.getRequestURI().split("/");

        if (DEBUG) {
            LOG.info("requestURI = " + req.getRequestURI());
        }

        String page = getPage(req);

        OdeAuthFilter.UserInfo userInfo = OdeAuthFilter.getUserInfo(req);

        String queryString = req.getQueryString();
        HashMap<String, String> params = getQueryMap(queryString);

        String locale = params.get("locale");
        String repo = params.get("repo");
        String galleryId = params.get("galleryId");
        String redirect = params.get("redirect");
        String autoload = params.get("autoload");
        String newGalleryId = params.get("ng");

        if (DEBUG) {
            LOG.info("locale = " + locale + " bundle: " + new Locale(locale));
        }
        ResourceBundle bundle;
        if (locale == null) {
            bundle = ResourceBundle.getBundle("com/google/appinventor/server/loginmessages", new Locale("en"));
        } else {
            bundle = ResourceBundle.getBundle("com/google/appinventor/server/loginmessages", new Locale(locale));
        }

        String emailAddress = bundle.getString("emailaddress");
        String password = bundle.getString("password");
        String login = bundle.getString("login");
        String passwordclickhere = bundle.getString("passwordclickhere");

        try {
            req.getRequestDispatcher("/UploadUser.jsp").forward(req, resp);
        } catch (ServletException e) {
            throw new IOException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        InputStream inputStream = null;
        try {
            inputStream = request.getPart("file").getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\n"); // Use newline character as delimiter
            
            // JDBC URL, username, and password of PostgreSQL server
            String url = "jdbc:postgresql://localhost:5432/userdb";
            String username = "postgres";
            String password = "admin";
            
            // JDBC variables for opening and managing connection
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                // Register JDBC driver
                Class.forName("org.postgresql.Driver");

                // Open a connection
                connection = DriverManager.getConnection(url, username, password);
                
                // SQL query to insert data into users table
                String sql = "INSERT INTO aiuser (email, password) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                
                // Extract data from CSV and insert into database
                while (scanner.hasNext()) {
                    String line = scanner.next();
                    String[] record = line.split(","); // Assuming CSV fields are comma-separated
                    
                    preparedStatement.setString(1, record[0]); // Assuming email is in the first column
                    preparedStatement.setString(2, record[1]); // Assuming password is in the second column
                    preparedStatement.executeUpdate();
                }
                
                // Redirect to a success page
                response.sendRedirect("success.jsp");
                
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPage(HttpServletRequest req) {
        String [] components = req.getRequestURI().split("/");
        return components[components.length-1];
    }

    private static HashMap<String, String> getQueryMap(String query)  {
        HashMap<String, String> map = new HashMap<String, String>();
        if (query == null || query.equals("")) {
            return map;               // Empty map
        }
        String[] params = query.split("&");
        for (String param : params)  {
            String [] nvpair = param.split("=");
            if (nvpair.length <= 1) {
                map.put(nvpair[0], "");
            } else
                map.put(nvpair[0], URLDecoder.decode(nvpair[1]));
        }
        return map;
    }
}
