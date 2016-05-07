/*
    Jerrydog, a lightweight web application server in Java
    Copyright (C) 2015-2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.jerrydog;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public abstract class RestCallback extends RequestCallback
{
	/**
	 * The HTTP method that this callback listens to
	 */
	protected Method m_method;
	
	/**
	 * The path that this callback listens to
	 */
	protected String m_path;
	
	/**
	 * Creates a REST callback
	 * @param m The HTTP method this callback listens to
	 * @param path The path this callback listens to
	 */
	public RestCallback(Method m, String path)
	{
		super();
		m_method = m;
		m_path = path;
	}
	
	/**
	 * Sets the method for this callback
	 * @param m The method
	 * @return This callback
	 */
	public RestCallback setMethod(Method m)
	{
		m_method = m;
		return this;
	}

	@Override
	public final boolean fire(HttpExchange t)
	{
    URI u = t.getRequestURI();
    String path = u.getPath();
    String method = t.getRequestMethod();
    return (method.compareToIgnoreCase(methodToString(m_method)) == 0) 
        && path.compareTo(m_path) == 0;
	}

	public Map<String,String> getParameters(HttpExchange t)
	{
		String data = null;
		if (m_method == Method.GET)
		{
	    // Read GET data
			URI u = t.getRequestURI();
			data = u.getQuery();
		}
		else
		{
	    // Read POST data
	    InputStream is_post = t.getRequestBody();
	    data = Server.streamToString(is_post);
		}
    Map<String,String> params = Server.queryToMap(data, m_method);
    return params;
	}	
}
