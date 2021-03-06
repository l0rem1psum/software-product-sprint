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

import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

	private ArrayList<String> greets;

    @Override
  	public void init() {
    	greets = new ArrayList<>();
    	greets.add("Hi, welcome to Wenxuan's portfolio site. Glad that you are here:)");
		greets.add("Hi there, welcome to Wenxuan's portfolio site. So happy that you are here:)");
		greets.add("Hi, this is Wenxuan's portfolio site. Welcome!");
  	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String greet = greets.get((int) (Math.random() * greets.size()));
		response.setContentType("text/html;");
		response.getWriter().println(greet);
	}

}
