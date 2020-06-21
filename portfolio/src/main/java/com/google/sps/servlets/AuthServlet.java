// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet(urlPatterns = "/auth/*")
public class AuthServlet extends HttpServlet {

  private static final String LOGIN_STATUS = "/status";
  private static final String LOGIN = "/login";

  private UserService userService;

  @Override
  public void init() {
      userService = UserServiceFactory.getUserService();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    processRequest(request, response);
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String path = request.getPathInfo();
    switch(path) {
        case LOGIN_STATUS:
            if (userService.isUserLoggedIn()) {
                response.sendError(HttpServletResponse.SC_ACCEPTED);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
            break;
        case LOGIN:
            String urlToRedirectToAfterUserLogsIn = "/";
            String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

            response.setContentType("text/html;");
            response.getWriter().println(loginUrl);
        default:
            break;
    }
  }

}
